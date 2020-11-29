package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.FilesPostDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.FilesPost}.
 */
public interface FilesPostService {

    /**
     * Save a filesPost.
     *
     * @param filesPostDTO the entity to save.
     * @return the persisted entity.
     */
    FilesPostDTO save(FilesPostDTO filesPostDTO);

    /**
     * Get all the filesPosts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FilesPostDTO> findAll(Pageable pageable);


    /**
     * Get the "id" filesPost.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FilesPostDTO> findOne(Long id);

    /**
     * Delete the "id" filesPost.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the filesPost corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FilesPostDTO> search(String query, Pageable pageable);
}
