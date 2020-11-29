package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.CommentsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Comments}.
 */
public interface CommentsService {

    /**
     * Save a comments.
     *
     * @param commentsDTO the entity to save.
     * @return the persisted entity.
     */
    CommentsDTO save(CommentsDTO commentsDTO);

    /**
     * Get all the comments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CommentsDTO> findAll(Pageable pageable);


    /**
     * Get the "id" comments.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CommentsDTO> findOne(Long id);

    /**
     * Delete the "id" comments.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the comments corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CommentsDTO> search(String query, Pageable pageable);
}
