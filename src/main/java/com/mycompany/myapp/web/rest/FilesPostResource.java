package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.FilesPostService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.FilesPostDTO;
import com.mycompany.myapp.service.dto.FilesPostCriteria;
import com.mycompany.myapp.service.FilesPostQueryService;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.FilesPost}.
 */
@RestController
@RequestMapping("/api")
public class FilesPostResource {

    private final Logger log = LoggerFactory.getLogger(FilesPostResource.class);

    private static final String ENTITY_NAME = "filesPost";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FilesPostService filesPostService;

    private final FilesPostQueryService filesPostQueryService;

    public FilesPostResource(FilesPostService filesPostService, FilesPostQueryService filesPostQueryService) {
        this.filesPostService = filesPostService;
        this.filesPostQueryService = filesPostQueryService;
    }

    /**
     * {@code POST  /files-posts} : Create a new filesPost.
     *
     * @param filesPostDTO the filesPostDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new filesPostDTO, or with status {@code 400 (Bad Request)} if the filesPost has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/files-posts")
    public ResponseEntity<FilesPostDTO> createFilesPost(@RequestBody FilesPostDTO filesPostDTO) throws URISyntaxException {
        log.debug("REST request to save FilesPost : {}", filesPostDTO);
        if (filesPostDTO.getId() != null) {
            throw new BadRequestAlertException("A new filesPost cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FilesPostDTO result = filesPostService.save(filesPostDTO);
        return ResponseEntity.created(new URI("/api/files-posts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /files-posts} : Updates an existing filesPost.
     *
     * @param filesPostDTO the filesPostDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filesPostDTO,
     * or with status {@code 400 (Bad Request)} if the filesPostDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the filesPostDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/files-posts")
    public ResponseEntity<FilesPostDTO> updateFilesPost(@RequestBody FilesPostDTO filesPostDTO) throws URISyntaxException {
        log.debug("REST request to update FilesPost : {}", filesPostDTO);
        if (filesPostDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FilesPostDTO result = filesPostService.save(filesPostDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, filesPostDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /files-posts} : get all the filesPosts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of filesPosts in body.
     */
    @GetMapping("/files-posts")
    public ResponseEntity<List<FilesPostDTO>> getAllFilesPosts(FilesPostCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FilesPosts by criteria: {}", criteria);
        Page<FilesPostDTO> page = filesPostQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /files-posts/count} : count all the filesPosts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/files-posts/count")
    public ResponseEntity<Long> countFilesPosts(FilesPostCriteria criteria) {
        log.debug("REST request to count FilesPosts by criteria: {}", criteria);
        return ResponseEntity.ok().body(filesPostQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /files-posts/:id} : get the "id" filesPost.
     *
     * @param id the id of the filesPostDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the filesPostDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/files-posts/{id}")
    public ResponseEntity<FilesPostDTO> getFilesPost(@PathVariable Long id) {
        log.debug("REST request to get FilesPost : {}", id);
        Optional<FilesPostDTO> filesPostDTO = filesPostService.findOne(id);
        return ResponseUtil.wrapOrNotFound(filesPostDTO);
    }

    /**
     * {@code DELETE  /files-posts/:id} : delete the "id" filesPost.
     *
     * @param id the id of the filesPostDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/files-posts/{id}")
    public ResponseEntity<Void> deleteFilesPost(@PathVariable Long id) {
        log.debug("REST request to delete FilesPost : {}", id);
        filesPostService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/files-posts?query=:query} : search for the filesPost corresponding
     * to the query.
     *
     * @param query the query of the filesPost search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/files-posts")
    public ResponseEntity<List<FilesPostDTO>> searchFilesPosts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of FilesPosts for query {}", query);
        Page<FilesPostDTO> page = filesPostService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
