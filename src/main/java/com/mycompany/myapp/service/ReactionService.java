package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ReactionDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Reaction}.
 */
public interface ReactionService {

    /**
     * Save a reaction.
     *
     * @param reactionDTO the entity to save.
     * @return the persisted entity.
     */
    ReactionDTO save(ReactionDTO reactionDTO);

    /**
     * Get all the reactions.
     *
     * @return the list of entities.
     */
    List<ReactionDTO> findAll();


    /**
     * Get the "id" reaction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReactionDTO> findOne(Long id);

    /**
     * Delete the "id" reaction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the reaction corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<ReactionDTO> search(String query);
}
