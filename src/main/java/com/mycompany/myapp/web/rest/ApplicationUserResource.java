package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.ApplicationUserService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.ApplicationUserDTO;
import com.mycompany.myapp.service.dto.ApplicationUserCriteria;
import com.mycompany.myapp.service.ApplicationUserQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ApplicationUser}.
 */
@RestController
@RequestMapping("/api")
public class ApplicationUserResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationUserResource.class);

    private static final String ENTITY_NAME = "applicationUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApplicationUserService applicationUserService;

    private final ApplicationUserQueryService applicationUserQueryService;

    public ApplicationUserResource(ApplicationUserService applicationUserService, ApplicationUserQueryService applicationUserQueryService) {
        this.applicationUserService = applicationUserService;
        this.applicationUserQueryService = applicationUserQueryService;
    }

    /**
     * {@code POST  /application-users} : Create a new applicationUser.
     *
     * @param applicationUserDTO the applicationUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new applicationUserDTO, or with status {@code 400 (Bad Request)} if the applicationUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/application-users")
    public ResponseEntity<ApplicationUserDTO> createApplicationUser(@Valid @RequestBody ApplicationUserDTO applicationUserDTO) throws URISyntaxException {
        log.debug("REST request to save ApplicationUser : {}", applicationUserDTO);
        if (applicationUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new applicationUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApplicationUserDTO result = applicationUserService.save(applicationUserDTO);
        return ResponseEntity.created(new URI("/api/application-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /application-users} : Updates an existing applicationUser.
     *
     * @param applicationUserDTO the applicationUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationUserDTO,
     * or with status {@code 400 (Bad Request)} if the applicationUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the applicationUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/application-users")
    public ResponseEntity<ApplicationUserDTO> updateApplicationUser(@Valid @RequestBody ApplicationUserDTO applicationUserDTO) throws URISyntaxException {
        log.debug("REST request to update ApplicationUser : {}", applicationUserDTO);
        if (applicationUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ApplicationUserDTO result = applicationUserService.save(applicationUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, applicationUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /application-users} : get all the applicationUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of applicationUsers in body.
     */
    @GetMapping("/application-users")
    public ResponseEntity<List<ApplicationUserDTO>> getAllApplicationUsers(ApplicationUserCriteria criteria) {
        log.debug("REST request to get ApplicationUsers by criteria: {}", criteria);
        List<ApplicationUserDTO> entityList = applicationUserQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /application-users/count} : count all the applicationUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/application-users/count")
    public ResponseEntity<Long> countApplicationUsers(ApplicationUserCriteria criteria) {
        log.debug("REST request to count ApplicationUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(applicationUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /application-users/:id} : get the "id" applicationUser.
     *
     * @param id the id of the applicationUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the applicationUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/application-users/{id}")
    public ResponseEntity<ApplicationUserDTO> getApplicationUser(@PathVariable Long id) {
        log.debug("REST request to get ApplicationUser : {}", id);
        Optional<ApplicationUserDTO> applicationUserDTO = applicationUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(applicationUserDTO);
    }

    /**
     * {@code DELETE  /application-users/:id} : delete the "id" applicationUser.
     *
     * @param id the id of the applicationUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/application-users/{id}")
    public ResponseEntity<Void> deleteApplicationUser(@PathVariable Long id) {
        log.debug("REST request to delete ApplicationUser : {}", id);
        applicationUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/application-users?query=:query} : search for the applicationUser corresponding
     * to the query.
     *
     * @param query the query of the applicationUser search.
     * @return the result of the search.
     */
    @GetMapping("/_search/application-users")
    public List<ApplicationUserDTO> searchApplicationUsers(@RequestParam String query) {
        log.debug("REST request to search ApplicationUsers for query {}", query);
        return applicationUserService.search(query);
    }
}
