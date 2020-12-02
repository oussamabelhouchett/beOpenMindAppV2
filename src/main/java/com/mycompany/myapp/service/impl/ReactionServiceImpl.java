package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.ReactionService;
import com.mycompany.myapp.domain.Reaction;
import com.mycompany.myapp.repository.ReactionRepository;
import com.mycompany.myapp.repository.search.ReactionSearchRepository;
import com.mycompany.myapp.service.dto.ReactionDTO;
import com.mycompany.myapp.service.mapper.ReactionMapper;
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
 * Service Implementation for managing {@link Reaction}.
 */
@Service
@Transactional
public class ReactionServiceImpl implements ReactionService {

    private final Logger log = LoggerFactory.getLogger(ReactionServiceImpl.class);

    private final ReactionRepository reactionRepository;

    private final ReactionMapper reactionMapper;

    private final ReactionSearchRepository reactionSearchRepository;

    public ReactionServiceImpl(ReactionRepository reactionRepository, ReactionMapper reactionMapper, ReactionSearchRepository reactionSearchRepository) {
        this.reactionRepository = reactionRepository;
        this.reactionMapper = reactionMapper;
        this.reactionSearchRepository = reactionSearchRepository;
    }

    @Override
    public ReactionDTO save(ReactionDTO reactionDTO) {
        log.debug("Request to save Reaction : {}", reactionDTO);
        Reaction reaction = reactionMapper.toEntity(reactionDTO);
        reaction = reactionRepository.save(reaction);
        ReactionDTO result = reactionMapper.toDto(reaction);
        reactionSearchRepository.save(reaction);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReactionDTO> findAll() {
        log.debug("Request to get all Reactions");
        return reactionRepository.findAll().stream()
            .map(reactionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ReactionDTO> findOne(Long id) {
        log.debug("Request to get Reaction : {}", id);
        return reactionRepository.findById(id)
            .map(reactionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Reaction : {}", id);
        reactionRepository.deleteById(id);
        reactionSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReactionDTO> search(String query) {
        log.debug("Request to search Reactions for query {}", query);
        return StreamSupport
            .stream(reactionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(reactionMapper::toDto)
        .collect(Collectors.toList());
    }
}
