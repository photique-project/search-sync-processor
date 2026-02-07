package com.benchpress200.searchsyncprocessor.exhibition.repository.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Conflicts;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.elasticsearch._types.ScriptLanguage;
import co.elastic.clients.elasticsearch._types.ScriptSource;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.UpdateByQueryResponse;
import co.elastic.clients.json.JsonData;
import com.benchpress200.searchsyncprocessor.exhibition.repository.ExhibitionSearchRepositoryCustom;
import com.benchpress200.searchsyncprocessor.exhibition.repository.exception.ElasticsearchNonRetryableException;
import com.benchpress200.searchsyncprocessor.exhibition.repository.exception.ElasticsearchUpdateConflictException;
import com.benchpress200.searchsyncprocessor.user.consumer.payload.UserEventPayload;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ExhibitionSearchRepositoryCustomImpl implements ExhibitionSearchRepositoryCustom {
    private final static String INDEX_NAME = "exhibitions";
    private final static String WRITER_ID_FIELD = "writer.id";
    private final static String NICKNAME_SCRIPT_PARAM = "nickname";
    private final static String PROFILE_IMAGE_SCRIPT_PARAM = "profileImage";
    private final static String EVENT_ID_SCRIPT_PARAM = "eventId";

    private final ElasticsearchClient elasticsearchClient;

    @Override
    public void updateWriter(
            Long eventId,
            UserEventPayload payload
    ) {
        Long writerId = payload.getId();
        String nickname = payload.getNickname();
        String profileImage = payload.getProfileImage();

        Query query = createQuery(writerId);
        Script script = createScript(eventId, nickname, profileImage);

        try {
            UpdateByQueryResponse response = elasticsearchClient.updateByQuery(u -> u
                    .index(INDEX_NAME)
                    .query(query) // 작가 id에 해당하는 문서 조회
                    .conflicts(Conflicts.Proceed) // 버전 충돌 발생 문서는 건너뛰고 나머지는 계속 진행
                    .script(script) // 매칭된 문서마다 스크립트 실행
            );

            // 문서 버전 충돌로 업데이트 실패한 문서가 있을 경우 해당 이벤트 재시도하도록 예외 발생
            Long conflicts = response.versionConflicts();

            if(conflicts != null && conflicts > 0) {
                throw new ElasticsearchUpdateConflictException(eventId, conflicts);
            }

        } catch (IOException | ElasticsearchException e) {
            throw new ElasticsearchNonRetryableException(eventId);
        }
    }

    private Query createQuery(Long writerId) {
        return Query.of(q -> q
                .term(t -> t
                        .field(WRITER_ID_FIELD)
                        .value(writerId)
                )
        );
    }

    private Script createScript(
            Long eventId,
            String nickname,
            String profileImage
    ) {
        // TODO: 스크립트 파일 별도 관리 필요
        Script.Builder b = new Script.Builder()
                .lang(ScriptLanguage.Painless)
                .source(ScriptSource.of(ss -> ss.scriptString(
                                "def last = ctx._source.writer.lastProcessedEventId; " + // 마지막 작가 정보 업데이트 처리한 이벤트 id
                                        "def incoming = params.eventId; " + // 현재 처리하는 이벤트 id
                                        "if (last != null && last >= incoming) { ctx.op = 'none'; return; } " + // null이 아니면서 마지막 적용 이벤트가 더 최신이라면 해당 문서 작업 종료
                                        "ctx._source.writer.nickname = params.nickname; " +
                                        "ctx._source.writer.profileImage = params.profileImage; " +
                                        "ctx._source.writer.lastProcessedEventId = incoming; "
                        )
                ))
                .params(NICKNAME_SCRIPT_PARAM, JsonData.of(nickname))
                .params(EVENT_ID_SCRIPT_PARAM, JsonData.of(eventId));

        if (profileImage != null) {
            b.params(PROFILE_IMAGE_SCRIPT_PARAM, JsonData.of(profileImage));
        }

        return b.build();
    }
}

