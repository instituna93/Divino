package com.instituna.service.impl;

import com.instituna.domain.MemberTag;
import com.instituna.repository.MemberTagRepository;
import com.instituna.service.MemberTagService;
import com.instituna.service.dto.MemberTagDTO;
import com.instituna.service.mapper.MemberTagMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link MemberTag}.
 */
@Service
@Transactional
public class MemberTagServiceImpl implements MemberTagService {

    private final Logger log = LoggerFactory.getLogger(MemberTagServiceImpl.class);

    private final MemberTagRepository memberTagRepository;

    private final MemberTagMapper memberTagMapper;

    public MemberTagServiceImpl(MemberTagRepository memberTagRepository, MemberTagMapper memberTagMapper) {
        this.memberTagRepository = memberTagRepository;
        this.memberTagMapper = memberTagMapper;
    }

    @Override
    public Mono<MemberTagDTO> save(MemberTagDTO memberTagDTO) {
        log.debug("Request to save MemberTag : {}", memberTagDTO);
        return memberTagRepository.save(memberTagMapper.toEntity(memberTagDTO)).map(memberTagMapper::toDto);
    }

    @Override
    public Mono<MemberTagDTO> update(MemberTagDTO memberTagDTO) {
        log.debug("Request to update MemberTag : {}", memberTagDTO);
        return memberTagRepository.save(memberTagMapper.toEntity(memberTagDTO)).map(memberTagMapper::toDto);
    }

    @Override
    public Mono<MemberTagDTO> partialUpdate(MemberTagDTO memberTagDTO) {
        log.debug("Request to partially update MemberTag : {}", memberTagDTO);

        return memberTagRepository
            .findById(memberTagDTO.getId())
            .map(existingMemberTag -> {
                memberTagMapper.partialUpdate(existingMemberTag, memberTagDTO);

                return existingMemberTag;
            })
            .flatMap(memberTagRepository::save)
            .map(memberTagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<MemberTagDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MemberTags");
        return memberTagRepository.findAllBy(pageable).map(memberTagMapper::toDto);
    }

    public Flux<MemberTagDTO> findAllWithEagerRelationships(Pageable pageable) {
        return memberTagRepository.findAllWithEagerRelationships(pageable).map(memberTagMapper::toDto);
    }

    public Mono<Long> countAll() {
        return memberTagRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<MemberTagDTO> findOne(Long id) {
        log.debug("Request to get MemberTag : {}", id);
        return memberTagRepository.findOneWithEagerRelationships(id).map(memberTagMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete MemberTag : {}", id);
        return memberTagRepository.deleteById(id);
    }
}
