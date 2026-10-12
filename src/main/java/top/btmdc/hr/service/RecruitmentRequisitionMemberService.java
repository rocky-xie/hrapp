package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.RecruitmentRequisitionMember;
import top.btmdc.hr.repository.RecruitmentRequisitionMemberRepository;
import top.btmdc.hr.service.dto.RecruitmentRequisitionMemberDTO;
import top.btmdc.hr.service.mapper.RecruitmentRequisitionMemberMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.RecruitmentRequisitionMember}.
 */
@Service
@Transactional
public class RecruitmentRequisitionMemberService {

    private static final Logger LOG = LoggerFactory.getLogger(RecruitmentRequisitionMemberService.class);

    private final RecruitmentRequisitionMemberRepository recruitmentRequisitionMemberRepository;

    private final RecruitmentRequisitionMemberMapper recruitmentRequisitionMemberMapper;

    public RecruitmentRequisitionMemberService(
        RecruitmentRequisitionMemberRepository recruitmentRequisitionMemberRepository,
        RecruitmentRequisitionMemberMapper recruitmentRequisitionMemberMapper
    ) {
        this.recruitmentRequisitionMemberRepository = recruitmentRequisitionMemberRepository;
        this.recruitmentRequisitionMemberMapper = recruitmentRequisitionMemberMapper;
    }

    /**
     * Save a recruitmentRequisitionMember.
     *
     * @param recruitmentRequisitionMemberDTO the entity to save.
     * @return the persisted entity.
     */
    public RecruitmentRequisitionMemberDTO save(RecruitmentRequisitionMemberDTO recruitmentRequisitionMemberDTO) {
        LOG.debug("Request to save RecruitmentRequisitionMember : {}", recruitmentRequisitionMemberDTO);
        RecruitmentRequisitionMember recruitmentRequisitionMember = recruitmentRequisitionMemberMapper.toEntity(
            recruitmentRequisitionMemberDTO
        );
        recruitmentRequisitionMember = recruitmentRequisitionMemberRepository.save(recruitmentRequisitionMember);
        return recruitmentRequisitionMemberMapper.toDto(recruitmentRequisitionMember);
    }

    /**
     * Update a recruitmentRequisitionMember.
     *
     * @param recruitmentRequisitionMemberDTO the entity to save.
     * @return the persisted entity.
     */
    public RecruitmentRequisitionMemberDTO update(RecruitmentRequisitionMemberDTO recruitmentRequisitionMemberDTO) {
        LOG.debug("Request to update RecruitmentRequisitionMember : {}", recruitmentRequisitionMemberDTO);
        RecruitmentRequisitionMember recruitmentRequisitionMember = recruitmentRequisitionMemberMapper.toEntity(
            recruitmentRequisitionMemberDTO
        );
        recruitmentRequisitionMember = recruitmentRequisitionMemberRepository.save(recruitmentRequisitionMember);
        return recruitmentRequisitionMemberMapper.toDto(recruitmentRequisitionMember);
    }

    /**
     * Partially update a recruitmentRequisitionMember.
     *
     * @param recruitmentRequisitionMemberDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RecruitmentRequisitionMemberDTO> partialUpdate(RecruitmentRequisitionMemberDTO recruitmentRequisitionMemberDTO) {
        LOG.debug("Request to partially update RecruitmentRequisitionMember : {}", recruitmentRequisitionMemberDTO);

        return recruitmentRequisitionMemberRepository
            .findById(recruitmentRequisitionMemberDTO.getId())
            .map(existingRecruitmentRequisitionMember -> {
                recruitmentRequisitionMemberMapper.partialUpdate(existingRecruitmentRequisitionMember, recruitmentRequisitionMemberDTO);

                return existingRecruitmentRequisitionMember;
            })
            .map(recruitmentRequisitionMemberRepository::save)
            .map(recruitmentRequisitionMemberMapper::toDto);
    }

    /**
     * Get one recruitmentRequisitionMember by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RecruitmentRequisitionMemberDTO> findOne(Long id) {
        LOG.debug("Request to get RecruitmentRequisitionMember : {}", id);
        return recruitmentRequisitionMemberRepository.findById(id).map(recruitmentRequisitionMemberMapper::toDto);
    }

    /**
     * Delete the recruitmentRequisitionMember by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete RecruitmentRequisitionMember : {}", id);
        recruitmentRequisitionMemberRepository.deleteById(id);
    }
}
