package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.RecruitmentRequisition;
import top.btmdc.hr.repository.RecruitmentRequisitionRepository;
import top.btmdc.hr.service.dto.RecruitmentRequisitionDTO;
import top.btmdc.hr.service.mapper.RecruitmentRequisitionMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.RecruitmentRequisition}.
 */
@Service
@Transactional
public class RecruitmentRequisitionService {

    private static final Logger LOG = LoggerFactory.getLogger(RecruitmentRequisitionService.class);

    private final RecruitmentRequisitionRepository recruitmentRequisitionRepository;

    private final RecruitmentRequisitionMapper recruitmentRequisitionMapper;

    public RecruitmentRequisitionService(
        RecruitmentRequisitionRepository recruitmentRequisitionRepository,
        RecruitmentRequisitionMapper recruitmentRequisitionMapper
    ) {
        this.recruitmentRequisitionRepository = recruitmentRequisitionRepository;
        this.recruitmentRequisitionMapper = recruitmentRequisitionMapper;
    }

    /**
     * Save a recruitmentRequisition.
     *
     * @param recruitmentRequisitionDTO the entity to save.
     * @return the persisted entity.
     */
    public RecruitmentRequisitionDTO save(RecruitmentRequisitionDTO recruitmentRequisitionDTO) {
        LOG.debug("Request to save RecruitmentRequisition : {}", recruitmentRequisitionDTO);
        RecruitmentRequisition recruitmentRequisition = recruitmentRequisitionMapper.toEntity(recruitmentRequisitionDTO);
        recruitmentRequisition = recruitmentRequisitionRepository.save(recruitmentRequisition);
        return recruitmentRequisitionMapper.toDto(recruitmentRequisition);
    }

    /**
     * Update a recruitmentRequisition.
     *
     * @param recruitmentRequisitionDTO the entity to save.
     * @return the persisted entity.
     */
    public RecruitmentRequisitionDTO update(RecruitmentRequisitionDTO recruitmentRequisitionDTO) {
        LOG.debug("Request to update RecruitmentRequisition : {}", recruitmentRequisitionDTO);
        RecruitmentRequisition recruitmentRequisition = recruitmentRequisitionMapper.toEntity(recruitmentRequisitionDTO);
        recruitmentRequisition = recruitmentRequisitionRepository.save(recruitmentRequisition);
        return recruitmentRequisitionMapper.toDto(recruitmentRequisition);
    }

    /**
     * Partially update a recruitmentRequisition.
     *
     * @param recruitmentRequisitionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RecruitmentRequisitionDTO> partialUpdate(RecruitmentRequisitionDTO recruitmentRequisitionDTO) {
        LOG.debug("Request to partially update RecruitmentRequisition : {}", recruitmentRequisitionDTO);

        return recruitmentRequisitionRepository
            .findById(recruitmentRequisitionDTO.getId())
            .map(existingRecruitmentRequisition -> {
                recruitmentRequisitionMapper.partialUpdate(existingRecruitmentRequisition, recruitmentRequisitionDTO);

                return existingRecruitmentRequisition;
            })
            .map(recruitmentRequisitionRepository::save)
            .map(recruitmentRequisitionMapper::toDto);
    }

    /**
     * Get one recruitmentRequisition by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RecruitmentRequisitionDTO> findOne(Long id) {
        LOG.debug("Request to get RecruitmentRequisition : {}", id);
        return recruitmentRequisitionRepository.findById(id).map(recruitmentRequisitionMapper::toDto);
    }

    /**
     * Delete the recruitmentRequisition by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete RecruitmentRequisition : {}", id);
        recruitmentRequisitionRepository.deleteById(id);
    }
}
