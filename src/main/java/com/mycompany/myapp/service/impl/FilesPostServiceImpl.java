package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.FilesPostService;
import com.mycompany.myapp.domain.FilesPost;
import com.mycompany.myapp.repository.FilesPostRepository;
import com.mycompany.myapp.repository.search.FilesPostSearchRepository;
import com.mycompany.myapp.service.dto.FilesPostDTO;
import com.mycompany.myapp.service.mapper.FilesPostMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link FilesPost}.
 */
@Service
@Transactional
public class FilesPostServiceImpl implements FilesPostService {

    private final Logger log = LoggerFactory.getLogger(FilesPostServiceImpl.class);

    private final FilesPostRepository filesPostRepository;

    private final FilesPostMapper filesPostMapper;

    private final FilesPostSearchRepository filesPostSearchRepository;

    public FilesPostServiceImpl(FilesPostRepository filesPostRepository, FilesPostMapper filesPostMapper, FilesPostSearchRepository filesPostSearchRepository) {
        this.filesPostRepository = filesPostRepository;
        this.filesPostMapper = filesPostMapper;
        this.filesPostSearchRepository = filesPostSearchRepository;
    }

    @Override
    public FilesPostDTO save(FilesPostDTO filesPostDTO) {
        log.debug("Request to save FilesPost : {}", filesPostDTO);
        FilesPost filesPost = filesPostMapper.toEntity(filesPostDTO);
        filesPost = filesPostRepository.save(filesPost);
        FilesPostDTO result = filesPostMapper.toDto(filesPost);
        filesPostSearchRepository.save(filesPost);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FilesPostDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FilesPosts");
        return filesPostRepository.findAll(pageable)
            .map(filesPostMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<FilesPostDTO> findOne(Long id) {
        log.debug("Request to get FilesPost : {}", id);
        return filesPostRepository.findById(id)
            .map(filesPostMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FilesPost : {}", id);
        filesPostRepository.deleteById(id);
        filesPostSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FilesPostDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FilesPosts for query {}", query);
        return filesPostSearchRepository.search(queryStringQuery(query), pageable)
            .map(filesPostMapper::toDto);
    }
}
