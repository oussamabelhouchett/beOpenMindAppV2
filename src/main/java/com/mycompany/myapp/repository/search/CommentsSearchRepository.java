package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Comments;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Comments} entity.
 */
public interface CommentsSearchRepository extends ElasticsearchRepository<Comments, Long> {
}
