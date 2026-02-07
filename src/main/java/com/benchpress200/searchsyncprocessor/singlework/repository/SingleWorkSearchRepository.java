package com.benchpress200.searchsyncprocessor.singlework.repository;

import com.benchpress200.searchsyncprocessor.singlework.document.SingleWorkSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SingleWorkSearchRepository extends ElasticsearchRepository<SingleWorkSearch, Long>, SingleWorkSearchRepositoryCustom {
}
