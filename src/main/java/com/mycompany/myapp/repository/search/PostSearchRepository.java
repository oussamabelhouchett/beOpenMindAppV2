package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Post;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Post} entity.
 */
public interface PostSearchRepository extends ElasticsearchRepository<Post, Long> {
}
