package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.ApplicationUserService;
import com.mycompany.myapp.domain.ApplicationUser;
import com.mycompany.myapp.repository.ApplicationUserRepository;
import com.mycompany.myapp.repository.search.ApplicationUserSearchRepository;
import com.mycompany.myapp.service.dto.ApplicationUserDTO;
import com.mycompany.myapp.service.mapper.ApplicationUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link ApplicationUser}.
 */
@Service
@Transactional
public class ApplicationUserServiceImpl implements ApplicationUserService {

    private final Logger log = LoggerFactory.getLogger(ApplicationUserServiceImpl.class);

    private final ApplicationUserRepository applicationUserRepository;

    private final ApplicationUserMapper applicationUserMapper;

    private final ApplicationUserSearchRepository applicationUserSearchRepository;

    public ApplicationUserServiceImpl(ApplicationUserRepository applicationUserRepository, ApplicationUserMapper applicationUserMapper, ApplicationUserSearchRepository applicationUserSearchRepository) {
        this.applicationUserRepository = applicationUserRepository;
        this.applicationUserMapper = applicationUserMapper;
        this.applicationUserSearchRepository = applicationUserSearchRepository;
    }

    @Override
    public ApplicationUserDTO save(ApplicationUserDTO applicationUserDTO) {
        log.debug("Request to save ApplicationUser : {}", applicationUserDTO);
        ApplicationUser applicationUser = applicationUserMapper.toEntity(applicationUserDTO);
        applicationUser = applicationUserRepository.save(applicationUser);
        ApplicationUserDTO result = applicationUserMapper.toDto(applicationUser);
        applicationUserSearchRepository.save(applicationUser);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationUserDTO> findAll() {
        log.debug("Request to get all ApplicationUsers");
        return applicationUserRepository.findAll().stream()
            .map(applicationUserMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ApplicationUserDTO> findOne(Long id) {
        log.debug("Request to get ApplicationUser : {}", id);
        return applicationUserRepository.findById(id)
            .map(applicationUserMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ApplicationUser : {}", id);
        applicationUserRepository.deleteById(id);
        applicationUserSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationUserDTO> search(String query) {
        log.debug("Request to search ApplicationUsers for query {}", query);
        return StreamSupport
            .stream(applicationUserSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(applicationUserMapper::toDto)
        .collect(Collectors.toList());
    }
}
