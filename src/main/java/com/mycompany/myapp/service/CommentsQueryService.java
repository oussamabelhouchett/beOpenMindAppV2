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

import com.mycompany.myapp.domain.Comments;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.CommentsRepository;
import com.mycompany.myapp.repository.search.CommentsSearchRepository;
import com.mycompany.myapp.service.dto.CommentsCriteria;
import com.mycompany.myapp.service.dto.CommentsDTO;
import com.mycompany.myapp.service.mapper.CommentsMapper;

/**
 * Service for executing complex queries for {@link Comments} entities in the database.
 * The main input is a {@link CommentsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CommentsDTO} or a {@link Page} of {@link CommentsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CommentsQueryService extends QueryService<Comments> {

    private final Logger log = LoggerFactory.getLogger(CommentsQueryService.class);

    private final CommentsRepository commentsRepository;

    private final CommentsMapper commentsMapper;

    private final CommentsSearchRepository commentsSearchRepository;

    public CommentsQueryService(CommentsRepository commentsRepository, CommentsMapper commentsMapper, CommentsSearchRepository commentsSearchRepository) {
        this.commentsRepository = commentsRepository;
        this.commentsMapper = commentsMapper;
        this.commentsSearchRepository = commentsSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CommentsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CommentsDTO> findByCriteria(CommentsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Comments> specification = createSpecification(criteria);
        return commentsMapper.toDto(commentsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CommentsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CommentsDTO> findByCriteria(CommentsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Comments> specification = createSpecification(criteria);
        return commentsRepository.findAll(specification, page)
            .map(commentsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CommentsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Comments> specification = createSpecification(criteria);
        return commentsRepository.count(specification);
    }

    /**
     * Function to convert {@link CommentsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Comments> createSpecification(CommentsCriteria criteria) {
        Specification<Comments> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Comments_.id));
            }
            if (criteria.getContentText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContentText(), Comments_.contentText));
            }
            if (criteria.getDatePub() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDatePub(), Comments_.datePub));
            }
            if (criteria.getTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTime(), Comments_.time));
            }
            if (criteria.getParentId() != null) {
                specification = specification.and(buildSpecification(criteria.getParentId(),
                    root -> root.join(Comments_.parent, JoinType.LEFT).get(Comments_.id)));
            }
            if (criteria.getCommentsId() != null) {
                specification = specification.and(buildSpecification(criteria.getCommentsId(),
                    root -> root.join(Comments_.comments, JoinType.LEFT).get(Post_.id)));
            }
        }
        return specification;
    }
}
