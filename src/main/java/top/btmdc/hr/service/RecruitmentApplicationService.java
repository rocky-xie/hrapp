package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.RecruitmentApplication;
import top.btmdc.hr.repository.RecruitmentApplicationRepository;
import top.btmdc.hr.service.dto.RecruitmentApplicationDTO;
import top.btmdc.hr.service.mapper.RecruitmentApplicationMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.RecruitmentApplication}.
 */
@Service
@Transactional
public class RecruitmentApplicationService {

    private static final Logger LOG = LoggerFactory.getLogger(RecruitmentApplicationService.class);

    private final RecruitmentApplicationRepository recruitmentApplicationRepository;

    private final RecruitmentApplicationMapper recruitmentApplicationMapper;

    public RecruitmentApplicationService(
        RecruitmentApplicationRepository recruitmentApplicationRepository,
        RecruitmentApplicationMapper recruitmentApplicationMapper
    ) {
        this.recruitmentApplicationRepository = recruitmentApplicationRepository;
        this.recruitmentApplicationMapper = recruitmentApplicationMapper;
    }

    /**
     * Save a recruitmentApplication.
     *
     * @param recruitmentApplicationDTO the entity to save.
     * @return the persisted entity.
     */
    public RecruitmentApplicationDTO save(RecruitmentApplicationDTO recruitmentApplicationDTO) {
        LOG.debug("Request to save RecruitmentApplication : {}", recruitmentApplicationDTO);
        RecruitmentApplication recruitmentApplication = recruitmentApplicationMapper.toEntity(recruitmentApplicationDTO);
        recruitmentApplication = recruitmentApplicationRepository.save(recruitmentApplication);
        return recruitmentApplicationMapper.toDto(recruitmentApplication);
    }

    /**
     * Update a recruitmentApplication.
     *
     * @param recruitmentApplicationDTO the entity to save.
     * @return the persisted entity.
     */
    public RecruitmentApplicationDTO update(RecruitmentApplicationDTO recruitmentApplicationDTO) {
        LOG.debug("Request to update RecruitmentApplication : {}", recruitmentApplicationDTO);
        RecruitmentApplication recruitmentApplication = recruitmentApplicationMapper.toEntity(recruitmentApplicationDTO);
        recruitmentApplication = recruitmentApplicationRepository.save(recruitmentApplication);
        return recruitmentApplicationMapper.toDto(recruitmentApplication);
    }

    /**
     * Partially update a recruitmentApplication.
     *
     * @param recruitmentApplicationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RecruitmentApplicationDTO> partialUpdate(RecruitmentApplicationDTO recruitmentApplicationDTO) {
        LOG.debug("Request to partially update RecruitmentApplication : {}", recruitmentApplicationDTO);

        return recruitmentApplicationRepository
            .findById(recruitmentApplicationDTO.getId())
            .map(existingRecruitmentApplication -> {
                recruitmentApplicationMapper.partialUpdate(existingRecruitmentApplication, recruitmentApplicationDTO);

                return existingRecruitmentApplication;
            })
            .map(recruitmentApplicationRepository::save)
            .map(recruitmentApplicationMapper::toDto);
    }

    /**
     * Get one recruitmentApplication by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RecruitmentApplicationDTO> findOne(Long id) {
        LOG.debug("Request to get RecruitmentApplication : {}", id);
        return recruitmentApplicationRepository.findById(id).map(recruitmentApplicationMapper::toDto);
    }

    /**
     * Delete the recruitmentApplication by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete RecruitmentApplication : {}", id);
        recruitmentApplicationRepository.deleteById(id);
    }
}
