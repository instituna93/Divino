package com.instituna.service.impl;

import com.instituna.domain.Member;
import com.instituna.repository.MemberRepository;
import com.instituna.service.MemberService;
import com.instituna.service.UserService;
import com.instituna.service.dto.MemberDTO;
import com.instituna.service.mapper.MemberMapper;
import java.time.Instant;
import java.util.Date;
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

    private final UserService userService;

    public MemberServiceImpl(MemberRepository memberRepository, MemberMapper memberMapper, UserService userService) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
        this.userService = userService;
    }

    @Override
    public Mono<MemberDTO> save(MemberDTO memberDTO) {
        log.debug("Request to save Member : {}", memberDTO);

        return userService
            .getUserWithAuthorities()
            .flatMap(user -> {
                Instant timeStamp = (new Date()).toInstant();
                memberDTO.setCreatedBy(user.getId());
                memberDTO.setCreatedOn(timeStamp);
                memberDTO.setUpdatedBy(user.getId());
                memberDTO.setUpdatedOn(timeStamp);

                return memberRepository.save(memberMapper.toEntity(memberDTO)).map(memberMapper::toDto);
            });
    }

    @Override
    public Mono<MemberDTO> update(MemberDTO memberDTO) {
        log.debug("Request to update Member : {}", memberDTO);

        return userService
            .getUserWithAuthorities()
            .flatMap(user -> {
                Instant timeStamp = (new Date()).toInstant();
                memberDTO.setUpdatedBy(user.getId());
                memberDTO.setUpdatedOn(timeStamp);

                return memberRepository.save(memberMapper.toEntity(memberDTO)).map(memberMapper::toDto);
            });
    }

    @Override
    public Mono<MemberDTO> partialUpdate(MemberDTO memberDTO) {
        log.debug("Request to partially update Member : {}", memberDTO);

        return userService
            .getUserWithAuthorities()
            .flatMap(user -> {
                Instant timeStamp = (new Date()).toInstant();
                memberDTO.setUpdatedBy(user.getId());
                memberDTO.setUpdatedOn(timeStamp);

                return memberRepository
                    .findById(memberDTO.getId())
                    .map(existingMember -> {
                        memberMapper.partialUpdate(existingMember, memberDTO);

                        return existingMember;
                    })
                    .flatMap(memberRepository::save)
                    .map(memberMapper::toDto);
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<MemberDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Members");
        return memberRepository.findAllBy(pageable).map(memberMapper::toDto);
    }

    public Flux<MemberDTO> findAllWithEagerRelationships(Pageable pageable) {
        return memberRepository.findAllWithEagerRelationships(pageable).map(memberMapper::toDto);
    }

    public Mono<Long> countAll() {
        return memberRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<MemberDTO> findOne(Long id) {
        log.debug("Request to get Member : {}", id);
        return memberRepository.findOneWithEagerRelationships(id).map(memberMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Member : {}", id);
        return memberRepository.deleteById(id);
    }
}
