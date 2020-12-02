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

import com.mycompany.myapp.domain.Reaction;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.ReactionRepository;
import com.mycompany.myapp.repository.search.ReactionSearchRepository;
import com.mycompany.myapp.service.dto.ReactionCriteria;
import com.mycompany.myapp.service.dto.ReactionDTO;
import com.mycompany.myapp.service.mapper.ReactionMapper;

/**
 * Service for executing complex queries for {@link Reaction} entities in the database.
 * The main input is a {@link ReactionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReactionDTO} or a {@link Page} of {@link ReactionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReactionQueryService extends QueryService<Reaction> {

    private final Logger log = LoggerFactory.getLogger(ReactionQueryService.class);

    private final ReactionRepository reactionRepository;

    private final ReactionMapper reactionMapper;

    private final ReactionSearchRepository reactionSearchRepository;

    public ReactionQueryService(ReactionRepository reactionRepository, ReactionMapper reactionMapper, ReactionSearchRepository reactionSearchRepository) {
        this.reactionRepository = reactionRepository;
        this.reactionMapper = reactionMapper;
        this.reactionSearchRepository = reactionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ReactionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReactionDTO> findByCriteria(ReactionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Reaction> specification = createSpecification(criteria);
        return reactionMapper.toDto(reactionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ReactionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReactionDTO> findByCriteria(ReactionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Reaction> specification = createSpecification(criteria);
        return reactionRepository.findAll(specification, page)
            .map(reactionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReactionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Reaction> specification = createSpecification(criteria);
        return reactionRepository.count(specification);
    }

    /**
     * Function to convert {@link ReactionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Reaction> createSpecification(ReactionCriteria criteria) {
        Specification<Reaction> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Reaction_.id));
            }
            if (criteria.getIsComment() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIsComment(), Reaction_.isComment));
            }
            if (criteria.getIsLike() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIsLike(), Reaction_.isLike));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Reaction_.users, JoinType.LEFT).get(ApplicationUser_.id)));
            }
            if (criteria.getPostId() != null) {
                specification = specification.and(buildSpecification(criteria.getPostId(),
                    root -> root.join(Reaction_.post, JoinType.LEFT).get(Post_.id)));
            }
        }
        return specification;
    }
}
