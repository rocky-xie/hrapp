package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.HiringDecision;
import top.btmdc.hr.repository.HiringDecisionRepository;
import top.btmdc.hr.service.dto.HiringDecisionDTO;
import top.btmdc.hr.service.mapper.HiringDecisionMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.HiringDecision}.
 */
@Service
@Transactional
public class HiringDecisionService {

    private static final Logger LOG = LoggerFactory.getLogger(HiringDecisionService.class);

    private final HiringDecisionRepository hiringDecisionRepository;

    private final HiringDecisionMapper hiringDecisionMapper;

    public HiringDecisionService(HiringDecisionRepository hiringDecisionRepository, HiringDecisionMapper hiringDecisionMapper) {
        this.hiringDecisionRepository = hiringDecisionRepository;
        this.hiringDecisionMapper = hiringDecisionMapper;
    }

    /**
     * Save a hiringDecision.
     *
     * @param hiringDecisionDTO the entity to save.
     * @return the persisted entity.
     */
    public HiringDecisionDTO save(HiringDecisionDTO hiringDecisionDTO) {
        LOG.debug("Request to save HiringDecision : {}", hiringDecisionDTO);
        HiringDecision hiringDecision = hiringDecisionMapper.toEntity(hiringDecisionDTO);
        hiringDecision = hiringDecisionRepository.save(hiringDecision);
        return hiringDecisionMapper.toDto(hiringDecision);
    }

    /**
     * Update a hiringDecision.
     *
     * @param hiringDecisionDTO the entity to save.
     * @return the persisted entity.
     */
    public HiringDecisionDTO update(HiringDecisionDTO hiringDecisionDTO) {
        LOG.debug("Request to update HiringDecision : {}", hiringDecisionDTO);
        HiringDecision hiringDecision = hiringDecisionMapper.toEntity(hiringDecisionDTO);
        hiringDecision = hiringDecisionRepository.save(hiringDecision);
        return hiringDecisionMapper.toDto(hiringDecision);
    }

    /**
     * Partially update a hiringDecision.
     *
     * @param hiringDecisionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HiringDecisionDTO> partialUpdate(HiringDecisionDTO hiringDecisionDTO) {
        LOG.debug("Request to partially update HiringDecision : {}", hiringDecisionDTO);

        return hiringDecisionRepository
            .findById(hiringDecisionDTO.getId())
            .map(existingHiringDecision -> {
                hiringDecisionMapper.partialUpdate(existingHiringDecision, hiringDecisionDTO);

                return existingHiringDecision;
            })
            .map(hiringDecisionRepository::save)
            .map(hiringDecisionMapper::toDto);
    }

    /**
     * Get one hiringDecision by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HiringDecisionDTO> findOne(Long id) {
        LOG.debug("Request to get HiringDecision : {}", id);
        return hiringDecisionRepository.findById(id).map(hiringDecisionMapper::toDto);
    }

    /**
     * Delete the hiringDecision by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HiringDecision : {}", id);
        hiringDecisionRepository.deleteById(id);
    }
}
