package com.mycompany.myapp.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.mycompany.myapp.domain.FilesPost;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.FilesPostRepository;
import com.mycompany.myapp.repository.search.FilesPostSearchRepository;
import com.mycompany.myapp.service.dto.FilesPostCriteria;
import com.mycompany.myapp.service.dto.FilesPostDTO;
import com.mycompany.myapp.service.mapper.FilesPostMapper;

/**
 * Service for executing complex queries for {@link FilesPost} entities in the database.
 * The main input is a {@link FilesPostCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FilesPostDTO} or a {@link Page} of {@link FilesPostDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FilesPostQueryService extends QueryService<FilesPost> {

    private final Logger log = LoggerFactory.getLogger(FilesPostQueryService.class);

    private final FilesPostRepository filesPostRepository;

    private final FilesPostMapper filesPostMapper;

    private final FilesPostSearchRepository filesPostSearchRepository;

    public FilesPostQueryService(FilesPostRepository filesPostRepository, FilesPostMapper filesPostMapper, FilesPostSearchRepository filesPostSearchRepository) {
        this.filesPostRepository = filesPostRepository;
        this.filesPostMapper = filesPostMapper;
        this.filesPostSearchRepository = filesPostSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FilesPostDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FilesPostDTO> findByCriteria(FilesPostCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FilesPost> specification = createSpecification(criteria);
        return filesPostMapper.toDto(filesPostRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FilesPostDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FilesPostDTO> findByCriteria(FilesPostCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FilesPost> specification = createSpecification(criteria);
        return filesPostRepository.findAll(specification, page)
            .map(filesPostMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FilesPostCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FilesPost> specification = createSpecification(criteria);
        return filesPostRepository.count(specification);
    }

    /**
     * Function to convert {@link FilesPostCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FilesPost> createSpecification(FilesPostCriteria criteria) {
        Specification<FilesPost> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FilesPost_.id));
            }
            if (criteria.getPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPath(), FilesPost_.path));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), FilesPost_.type));
            }
            if (criteria.getFilesPostId() != null) {
                specification = specification.and(buildSpecification(criteria.getFilesPostId(),
                    root -> root.join(FilesPost_.filesPost, JoinType.LEFT).get(Post_.id)));
            }
        }
        return specification;
    }
}
