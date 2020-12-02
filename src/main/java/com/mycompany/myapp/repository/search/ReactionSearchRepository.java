package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Reaction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Reaction} entity.
 */
public interface ReactionSearchRepository extends ElasticsearchRepository<Reaction, Long> {
}
