package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.RecruitmentEvaluation;
import top.btmdc.hr.repository.RecruitmentEvaluationRepository;
import top.btmdc.hr.service.dto.RecruitmentEvaluationDTO;
import top.btmdc.hr.service.mapper.RecruitmentEvaluationMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.RecruitmentEvaluation}.
 */
@Service
@Transactional
public class RecruitmentEvaluationService {

    private static final Logger LOG = LoggerFactory.getLogger(RecruitmentEvaluationService.class);

    private final RecruitmentEvaluationRepository recruitmentEvaluationRepository;

    private final RecruitmentEvaluationMapper recruitmentEvaluationMapper;

    public RecruitmentEvaluationService(
        RecruitmentEvaluationRepository recruitmentEvaluationRepository,
        RecruitmentEvaluationMapper recruitmentEvaluationMapper
    ) {
        this.recruitmentEvaluationRepository = recruitmentEvaluationRepository;
        this.recruitmentEvaluationMapper = recruitmentEvaluationMapper;
    }

    /**
     * Save a recruitmentEvaluation.
     *
     * @param recruitmentEvaluationDTO the entity to save.
     * @return the persisted entity.
     */
    public RecruitmentEvaluationDTO save(RecruitmentEvaluationDTO recruitmentEvaluationDTO) {
        LOG.debug("Request to save RecruitmentEvaluation : {}", recruitmentEvaluationDTO);
        RecruitmentEvaluation recruitmentEvaluation = recruitmentEvaluationMapper.toEntity(recruitmentEvaluationDTO);
        recruitmentEvaluation = recruitmentEvaluationRepository.save(recruitmentEvaluation);
        return recruitmentEvaluationMapper.toDto(recruitmentEvaluation);
    }

    /**
     * Update a recruitmentEvaluation.
     *
     * @param recruitmentEvaluationDTO the entity to save.
     * @return the persisted entity.
     */
    public RecruitmentEvaluationDTO update(RecruitmentEvaluationDTO recruitmentEvaluationDTO) {
        LOG.debug("Request to update RecruitmentEvaluation : {}", recruitmentEvaluationDTO);
        RecruitmentEvaluation recruitmentEvaluation = recruitmentEvaluationMapper.toEntity(recruitmentEvaluationDTO);
        recruitmentEvaluation = recruitmentEvaluationRepository.save(recruitmentEvaluation);
        return recruitmentEvaluationMapper.toDto(recruitmentEvaluation);
    }

    /**
     * Partially update a recruitmentEvaluation.
     *
     * @param recruitmentEvaluationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RecruitmentEvaluationDTO> partialUpdate(RecruitmentEvaluationDTO recruitmentEvaluationDTO) {
        LOG.debug("Request to partially update RecruitmentEvaluation : {}", recruitmentEvaluationDTO);

        return recruitmentEvaluationRepository
            .findById(recruitmentEvaluationDTO.getId())
            .map(existingRecruitmentEvaluation -> {
                recruitmentEvaluationMapper.partialUpdate(existingRecruitmentEvaluation, recruitmentEvaluationDTO);

                return existingRecruitmentEvaluation;
            })
            .map(recruitmentEvaluationRepository::save)
            .map(recruitmentEvaluationMapper::toDto);
    }

    /**
     * Get one recruitmentEvaluation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RecruitmentEvaluationDTO> findOne(Long id) {
        LOG.debug("Request to get RecruitmentEvaluation : {}", id);
        return recruitmentEvaluationRepository.findById(id).map(recruitmentEvaluationMapper::toDto);
    }

    /**
     * Delete the recruitmentEvaluation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete RecruitmentEvaluation : {}", id);
        recruitmentEvaluationRepository.deleteById(id);
    }
}
