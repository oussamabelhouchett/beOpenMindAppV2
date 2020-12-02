package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.ApplicationUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link ApplicationUser} entity.
 */
public interface ApplicationUserSearchRepository extends ElasticsearchRepository<ApplicationUser, Long> {
}
