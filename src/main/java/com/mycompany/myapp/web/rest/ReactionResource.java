package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.ReactionService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.ReactionDTO;
import com.mycompany.myapp.service.dto.ReactionCriteria;
import com.mycompany.myapp.service.ReactionQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Reaction}.
 */
@RestController
@RequestMapping("/api")
public class ReactionResource {

    private final Logger log = LoggerFactory.getLogger(ReactionResource.class);

    private static final String ENTITY_NAME = "reaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReactionService reactionService;

    private final ReactionQueryService reactionQueryService;

    public ReactionResource(ReactionService reactionService, ReactionQueryService reactionQueryService) {
        this.reactionService = reactionService;
        this.reactionQueryService = reactionQueryService;
    }

    /**
     * {@code POST  /reactions} : Create a new reaction.
     *
     * @param reactionDTO the reactionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reactionDTO, or with status {@code 400 (Bad Request)} if the reaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reactions")
    public ResponseEntity<ReactionDTO> createReaction(@RequestBody ReactionDTO reactionDTO) throws URISyntaxException {
        log.debug("REST request to save Reaction : {}", reactionDTO);
        if (reactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new reaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReactionDTO result = reactionService.save(reactionDTO);
        return ResponseEntity.created(new URI("/api/reactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reactions} : Updates an existing reaction.
     *
     * @param reactionDTO the reactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reactionDTO,
     * or with status {@code 400 (Bad Request)} if the reactionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reactions")
    public ResponseEntity<ReactionDTO> updateReaction(@RequestBody ReactionDTO reactionDTO) throws URISyntaxException {
        log.debug("REST request to update Reaction : {}", reactionDTO);
        if (reactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ReactionDTO result = reactionService.save(reactionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, reactionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /reactions} : get all the reactions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reactions in body.
     */
    @GetMapping("/reactions")
    public ResponseEntity<List<ReactionDTO>> getAllReactions(ReactionCriteria criteria) {
        log.debug("REST request to get Reactions by criteria: {}", criteria);
        List<ReactionDTO> entityList = reactionQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /reactions/count} : count all the reactions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/reactions/count")
    public ResponseEntity<Long> countReactions(ReactionCriteria criteria) {
        log.debug("REST request to count Reactions by criteria: {}", criteria);
        return ResponseEntity.ok().body(reactionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /reactions/:id} : get the "id" reaction.
     *
     * @param id the id of the reactionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reactionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reactions/{id}")
    public ResponseEntity<ReactionDTO> getReaction(@PathVariable Long id) {
        log.debug("REST request to get Reaction : {}", id);
        Optional<ReactionDTO> reactionDTO = reactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reactionDTO);
    }

    /**
     * {@code DELETE  /reactions/:id} : delete the "id" reaction.
     *
     * @param id the id of the reactionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reactions/{id}")
    public ResponseEntity<Void> deleteReaction(@PathVariable Long id) {
        log.debug("REST request to delete Reaction : {}", id);
        reactionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/reactions?query=:query} : search for the reaction corresponding
     * to the query.
     *
     * @param query the query of the reaction search.
     * @return the result of the search.
     */
    @GetMapping("/_search/reactions")
    public List<ReactionDTO> searchReactions(@RequestParam String query) {
        log.debug("REST request to search Reactions for query {}", query);
        return reactionService.search(query);
    }
}
