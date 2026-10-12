package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.RecruitmentRequisitionVersion;
import top.btmdc.hr.repository.RecruitmentRequisitionVersionRepository;
import top.btmdc.hr.service.dto.RecruitmentRequisitionVersionDTO;
import top.btmdc.hr.service.mapper.RecruitmentRequisitionVersionMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.RecruitmentRequisitionVersion}.
 */
@Service
@Transactional
public class RecruitmentRequisitionVersionService {

    private static final Logger LOG = LoggerFactory.getLogger(RecruitmentRequisitionVersionService.class);

    private final RecruitmentRequisitionVersionRepository recruitmentRequisitionVersionRepository;

    private final RecruitmentRequisitionVersionMapper recruitmentRequisitionVersionMapper;

    public RecruitmentRequisitionVersionService(
        RecruitmentRequisitionVersionRepository recruitmentRequisitionVersionRepository,
        RecruitmentRequisitionVersionMapper recruitmentRequisitionVersionMapper
    ) {
        this.recruitmentRequisitionVersionRepository = recruitmentRequisitionVersionRepository;
        this.recruitmentRequisitionVersionMapper = recruitmentRequisitionVersionMapper;
    }

    /**
     * Save a recruitmentRequisitionVersion.
     *
     * @param recruitmentRequisitionVersionDTO the entity to save.
     * @return the persisted entity.
     */
    public RecruitmentRequisitionVersionDTO save(RecruitmentRequisitionVersionDTO recruitmentRequisitionVersionDTO) {
        LOG.debug("Request to save RecruitmentRequisitionVersion : {}", recruitmentRequisitionVersionDTO);
        RecruitmentRequisitionVersion recruitmentRequisitionVersion = recruitmentRequisitionVersionMapper.toEntity(
            recruitmentRequisitionVersionDTO
        );
        recruitmentRequisitionVersion = recruitmentRequisitionVersionRepository.save(recruitmentRequisitionVersion);
        return recruitmentRequisitionVersionMapper.toDto(recruitmentRequisitionVersion);
    }

    /**
     * Update a recruitmentRequisitionVersion.
     *
     * @param recruitmentRequisitionVersionDTO the entity to save.
     * @return the persisted entity.
     */
    public RecruitmentRequisitionVersionDTO update(RecruitmentRequisitionVersionDTO recruitmentRequisitionVersionDTO) {
        LOG.debug("Request to update RecruitmentRequisitionVersion : {}", recruitmentRequisitionVersionDTO);
        RecruitmentRequisitionVersion recruitmentRequisitionVersion = recruitmentRequisitionVersionMapper.toEntity(
            recruitmentRequisitionVersionDTO
        );
        recruitmentRequisitionVersion = recruitmentRequisitionVersionRepository.save(recruitmentRequisitionVersion);
        return recruitmentRequisitionVersionMapper.toDto(recruitmentRequisitionVersion);
    }

    /**
     * Partially update a recruitmentRequisitionVersion.
     *
     * @param recruitmentRequisitionVersionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RecruitmentRequisitionVersionDTO> partialUpdate(RecruitmentRequisitionVersionDTO recruitmentRequisitionVersionDTO) {
        LOG.debug("Request to partially update RecruitmentRequisitionVersion : {}", recruitmentRequisitionVersionDTO);

        return recruitmentRequisitionVersionRepository
            .findById(recruitmentRequisitionVersionDTO.getId())
            .map(existingRecruitmentRequisitionVersion -> {
                recruitmentRequisitionVersionMapper.partialUpdate(existingRecruitmentRequisitionVersion, recruitmentRequisitionVersionDTO);

                return existingRecruitmentRequisitionVersion;
            })
            .map(recruitmentRequisitionVersionRepository::save)
            .map(recruitmentRequisitionVersionMapper::toDto);
    }

    /**
     * Get one recruitmentRequisitionVersion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RecruitmentRequisitionVersionDTO> findOne(Long id) {
        LOG.debug("Request to get RecruitmentRequisitionVersion : {}", id);
        return recruitmentRequisitionVersionRepository.findById(id).map(recruitmentRequisitionVersionMapper::toDto);
    }

    /**
     * Delete the recruitmentRequisitionVersion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete RecruitmentRequisitionVersion : {}", id);
        recruitmentRequisitionVersionRepository.deleteById(id);
    }
}
