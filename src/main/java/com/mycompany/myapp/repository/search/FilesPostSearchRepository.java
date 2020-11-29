package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.FilesPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link FilesPost} entity.
 */
public interface FilesPostSearchRepository extends ElasticsearchRepository<FilesPost, Long> {
}
