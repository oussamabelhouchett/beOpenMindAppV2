package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.CommentsService;
import com.mycompany.myapp.domain.Comments;
import com.mycompany.myapp.repository.CommentsRepository;
import com.mycompany.myapp.repository.search.CommentsSearchRepository;
import com.mycompany.myapp.service.dto.CommentsDTO;
import com.mycompany.myapp.service.mapper.CommentsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Comments}.
 */
@Service
@Transactional
public class CommentsServiceImpl implements CommentsService {

    private final Logger log = LoggerFactory.getLogger(CommentsServiceImpl.class);

    private final CommentsRepository commentsRepository;

    private final CommentsMapper commentsMapper;

    private final CommentsSearchRepository commentsSearchRepository;

    public CommentsServiceImpl(CommentsRepository commentsRepository, CommentsMapper commentsMapper, CommentsSearchRepository commentsSearchRepository) {
        this.commentsRepository = commentsRepository;
        this.commentsMapper = commentsMapper;
        this.commentsSearchRepository = commentsSearchRepository;
    }

    @Override
    public CommentsDTO save(CommentsDTO commentsDTO) {
        log.debug("Request to save Comments : {}", commentsDTO);
        Comments comments = commentsMapper.toEntity(commentsDTO);
        comments = commentsRepository.save(comments);
        CommentsDTO result = commentsMapper.toDto(comments);
        commentsSearchRepository.save(comments);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Comments");
        return commentsRepository.findAll(pageable)
            .map(commentsMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<CommentsDTO> findOne(Long id) {
        log.debug("Request to get Comments : {}", id);
        return commentsRepository.findById(id)
            .map(commentsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Comments : {}", id);
        commentsRepository.deleteById(id);
        commentsSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Comments for query {}", query);
        return commentsSearchRepository.search(queryStringQuery(query), pageable)
            .map(commentsMapper::toDto);
    }
}
