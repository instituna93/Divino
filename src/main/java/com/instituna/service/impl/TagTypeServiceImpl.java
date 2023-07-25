package com.instituna.service.impl;

import com.instituna.domain.TagType;
import com.instituna.repository.TagTypeRepository;
import com.instituna.service.TagTypeService;
import com.instituna.service.dto.TagTypeDTO;
import com.instituna.service.mapper.TagTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link TagType}.
 */
@Service
@Transactional
public class TagTypeServiceImpl implements TagTypeService {

    private final Logger log = LoggerFactory.getLogger(TagTypeServiceImpl.class);

    private final TagTypeRepository tagTypeRepository;

    private final TagTypeMapper tagTypeMapper;

    public TagTypeServiceImpl(TagTypeRepository tagTypeRepository, TagTypeMapper tagTypeMapper) {
        this.tagTypeRepository = tagTypeRepository;
        this.tagTypeMapper = tagTypeMapper;
    }

    @Override
    public Mono<TagTypeDTO> save(TagTypeDTO tagTypeDTO) {
        log.debug("Request to save TagType : {}", tagTypeDTO);
        return tagTypeRepository.save(tagTypeMapper.toEntity(tagTypeDTO)).map(tagTypeMapper::toDto);
    }

    @Override
    public Mono<TagTypeDTO> update(TagTypeDTO tagTypeDTO) {
        log.debug("Request to update TagType : {}", tagTypeDTO);
        return tagTypeRepository.save(tagTypeMapper.toEntity(tagTypeDTO)).map(tagTypeMapper::toDto);
    }

    @Override
    public Mono<TagTypeDTO> partialUpdate(TagTypeDTO tagTypeDTO) {
        log.debug("Request to partially update TagType : {}", tagTypeDTO);

        return tagTypeRepository
            .findById(tagTypeDTO.getId())
            .map(existingTagType -> {
                tagTypeMapper.partialUpdate(existingTagType, tagTypeDTO);

                return existingTagType;
            })
            .flatMap(tagTypeRepository::save)
            .map(tagTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<TagTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TagTypes");
        return tagTypeRepository.findAllBy(pageable).map(tagTypeMapper::toDto);
    }

    public Flux<TagTypeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return tagTypeRepository.findAllWithEagerRelationships(pageable).map(tagTypeMapper::toDto);
    }

    public Mono<Long> countAll() {
        return tagTypeRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<TagTypeDTO> findOne(Long id) {
        log.debug("Request to get TagType : {}", id);
        return tagTypeRepository.findOneWithEagerRelationships(id).map(tagTypeMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete TagType : {}", id);
        return tagTypeRepository.deleteById(id);
    }
}
