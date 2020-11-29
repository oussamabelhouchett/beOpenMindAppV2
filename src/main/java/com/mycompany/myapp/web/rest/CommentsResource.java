package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.CommentsService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.CommentsDTO;
import com.mycompany.myapp.service.dto.CommentsCriteria;
import com.mycompany.myapp.service.CommentsQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Comments}.
 */
@RestController
@RequestMapping("/api")
public class CommentsResource {

    private final Logger log = LoggerFactory.getLogger(CommentsResource.class);

    private static final String ENTITY_NAME = "comments";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommentsService commentsService;

    private final CommentsQueryService commentsQueryService;

    public CommentsResource(CommentsService commentsService, CommentsQueryService commentsQueryService) {
        this.commentsService = commentsService;
        this.commentsQueryService = commentsQueryService;
    }

    /**
     * {@code POST  /comments} : Create a new comments.
     *
     * @param commentsDTO the commentsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commentsDTO, or with status {@code 400 (Bad Request)} if the comments has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/comments")
    public ResponseEntity<CommentsDTO> createComments(@RequestBody CommentsDTO commentsDTO) throws URISyntaxException {
        log.debug("REST request to save Comments : {}", commentsDTO);
        if (commentsDTO.getId() != null) {
            throw new BadRequestAlertException("A new comments cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommentsDTO result = commentsService.save(commentsDTO);
        return ResponseEntity.created(new URI("/api/comments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /comments} : Updates an existing comments.
     *
     * @param commentsDTO the commentsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commentsDTO,
     * or with status {@code 400 (Bad Request)} if the commentsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commentsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/comments")
    public ResponseEntity<CommentsDTO> updateComments(@RequestBody CommentsDTO commentsDTO) throws URISyntaxException {
        log.debug("REST request to update Comments : {}", commentsDTO);
        if (commentsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CommentsDTO result = commentsService.save(commentsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, commentsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /comments} : get all the comments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of comments in body.
     */
    @GetMapping("/comments")
    public ResponseEntity<List<CommentsDTO>> getAllComments(CommentsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Comments by criteria: {}", criteria);
        Page<CommentsDTO> page = commentsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /comments/count} : count all the comments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/comments/count")
    public ResponseEntity<Long> countComments(CommentsCriteria criteria) {
        log.debug("REST request to count Comments by criteria: {}", criteria);
        return ResponseEntity.ok().body(commentsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /comments/:id} : get the "id" comments.
     *
     * @param id the id of the commentsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commentsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/comments/{id}")
    public ResponseEntity<CommentsDTO> getComments(@PathVariable Long id) {
        log.debug("REST request to get Comments : {}", id);
        Optional<CommentsDTO> commentsDTO = commentsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commentsDTO);
    }

    /**
     * {@code DELETE  /comments/:id} : delete the "id" comments.
     *
     * @param id the id of the commentsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComments(@PathVariable Long id) {
        log.debug("REST request to delete Comments : {}", id);
        commentsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/comments?query=:query} : search for the comments corresponding
     * to the query.
     *
     * @param query the query of the comments search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/comments")
    public ResponseEntity<List<CommentsDTO>> searchComments(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Comments for query {}", query);
        Page<CommentsDTO> page = commentsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
