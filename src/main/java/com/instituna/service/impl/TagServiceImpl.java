package com.instituna.service.impl;

import com.instituna.domain.Tag;
import com.instituna.repository.TagRepository;
import com.instituna.service.TagService;
import com.instituna.service.dto.TagDTO;
import com.instituna.service.mapper.TagMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Tag}.
 */
@Service
@Transactional
public class TagServiceImpl implements TagService {

    private final Logger log = LoggerFactory.getLogger(TagServiceImpl.class);

    private final TagRepository tagRepository;

    private final TagMapper tagMapper;

    public TagServiceImpl(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    @Override
    public Mono<TagDTO> save(TagDTO tagDTO) {
        log.debug("Request to save Tag : {}", tagDTO);
        return tagRepository.save(tagMapper.toEntity(tagDTO)).map(tagMapper::toDto);
    }

    @Override
    public Mono<TagDTO> update(TagDTO tagDTO) {
        log.debug("Request to update Tag : {}", tagDTO);
        return tagRepository.save(tagMapper.toEntity(tagDTO)).map(tagMapper::toDto);
    }

    @Override
    public Mono<TagDTO> partialUpdate(TagDTO tagDTO) {
        log.debug("Request to partially update Tag : {}", tagDTO);

        return tagRepository
            .findById(tagDTO.getId())
            .map(existingTag -> {
                tagMapper.partialUpdate(existingTag, tagDTO);

                return existingTag;
            })
            .flatMap(tagRepository::save)
            .map(tagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<TagDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tags");
        return tagRepository.findAllBy(pageable).map(tagMapper::toDto);
    }

    public Flux<TagDTO> findAllWithEagerRelationships(Pageable pageable) {
        return tagRepository.findAllWithEagerRelationships(pageable).map(tagMapper::toDto);
    }

    public Mono<Long> countAll() {
        return tagRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<TagDTO> findOne(Long id) {
        log.debug("Request to get Tag : {}", id);
        return tagRepository.findOneWithEagerRelationships(id).map(tagMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Tag : {}", id);
        return tagRepository.deleteById(id);
    }
}
