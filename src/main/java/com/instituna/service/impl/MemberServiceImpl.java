package com.instituna.service.impl;

import com.instituna.domain.Member;
import com.instituna.repository.MemberRepository;
import com.instituna.service.MemberService;
import com.instituna.service.dto.MemberDTO;
import com.instituna.service.mapper.MemberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Member}.
 */
@Service
@Transactional
public class MemberServiceImpl implements MemberService {

    private final Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);

    private final MemberRepository memberRepository;

    private final MemberMapper memberMapper;

    public MemberServiceImpl(MemberRepository memberRepository, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }

    @Override
    public Mono<MemberDTO> save(MemberDTO memberDTO) {
        log.debug("Request to save Member : {}", memberDTO);
        return memberRepository.save(memberMapper.toEntity(memberDTO)).map(memberMapper::toDto);
    }

    @Override
    public Mono<MemberDTO> update(MemberDTO memberDTO) {
        log.debug("Request to update Member : {}", memberDTO);
        return memberRepository.save(memberMapper.toEntity(memberDTO)).map(memberMapper::toDto);
    }

    @Override
    public Mono<MemberDTO> partialUpdate(MemberDTO memberDTO) {
        log.debug("Request to partially update Member : {}", memberDTO);

        return memberRepository
            .findById(memberDTO.getId())
            .map(existingMember -> {
                memberMapper.partialUpdate(existingMember, memberDTO);

                return existingMember;
            })
            .flatMap(memberRepository::save)
            .map(memberMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<MemberDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Members");
        return memberRepository.findAllBy(pageable).map(memberMapper::toDto);
    }

    public Mono<Long> countAll() {
        return memberRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<MemberDTO> findOne(Long id) {
        log.debug("Request to get Member : {}", id);
        return memberRepository.findById(id).map(memberMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Member : {}", id);
        return memberRepository.deleteById(id);
    }
}
