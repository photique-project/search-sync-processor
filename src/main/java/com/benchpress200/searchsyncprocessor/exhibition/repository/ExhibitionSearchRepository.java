package com.benchpress200.searchsyncprocessor.exhibition.repository;

import com.benchpress200.searchsyncprocessor.exhibition.document.ExhibitionSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ExhibitionSearchRepository extends ElasticsearchRepository<ExhibitionSearch, Long>, ExhibitionSearchRepositoryCustom {
}
