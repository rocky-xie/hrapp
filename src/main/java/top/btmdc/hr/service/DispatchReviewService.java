package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.DispatchReview;
import top.btmdc.hr.repository.DispatchReviewRepository;
import top.btmdc.hr.service.dto.DispatchReviewDTO;
import top.btmdc.hr.service.mapper.DispatchReviewMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.DispatchReview}.
 */
@Service
@Transactional
public class DispatchReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(DispatchReviewService.class);

    private final DispatchReviewRepository dispatchReviewRepository;

    private final DispatchReviewMapper dispatchReviewMapper;

    public DispatchReviewService(DispatchReviewRepository dispatchReviewRepository, DispatchReviewMapper dispatchReviewMapper) {
        this.dispatchReviewRepository = dispatchReviewRepository;
        this.dispatchReviewMapper = dispatchReviewMapper;
    }

    /**
     * Save a dispatchReview.
     *
     * @param dispatchReviewDTO the entity to save.
     * @return the persisted entity.
     */
    public DispatchReviewDTO save(DispatchReviewDTO dispatchReviewDTO) {
        LOG.debug("Request to save DispatchReview : {}", dispatchReviewDTO);
        DispatchReview dispatchReview = dispatchReviewMapper.toEntity(dispatchReviewDTO);
        dispatchReview = dispatchReviewRepository.save(dispatchReview);
        return dispatchReviewMapper.toDto(dispatchReview);
    }

    /**
     * Update a dispatchReview.
     *
     * @param dispatchReviewDTO the entity to save.
     * @return the persisted entity.
     */
    public DispatchReviewDTO update(DispatchReviewDTO dispatchReviewDTO) {
        LOG.debug("Request to update DispatchReview : {}", dispatchReviewDTO);
        DispatchReview dispatchReview = dispatchReviewMapper.toEntity(dispatchReviewDTO);
        dispatchReview = dispatchReviewRepository.save(dispatchReview);
        return dispatchReviewMapper.toDto(dispatchReview);
    }

    /**
     * Partially update a dispatchReview.
     *
     * @param dispatchReviewDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DispatchReviewDTO> partialUpdate(DispatchReviewDTO dispatchReviewDTO) {
        LOG.debug("Request to partially update DispatchReview : {}", dispatchReviewDTO);

        return dispatchReviewRepository
            .findById(dispatchReviewDTO.getId())
            .map(existingDispatchReview -> {
                dispatchReviewMapper.partialUpdate(existingDispatchReview, dispatchReviewDTO);

                return existingDispatchReview;
            })
            .map(dispatchReviewRepository::save)
            .map(dispatchReviewMapper::toDto);
    }

    /**
     * Get one dispatchReview by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DispatchReviewDTO> findOne(Long id) {
        LOG.debug("Request to get DispatchReview : {}", id);
        return dispatchReviewRepository.findById(id).map(dispatchReviewMapper::toDto);
    }

    /**
     * Delete the dispatchReview by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DispatchReview : {}", id);
        dispatchReviewRepository.deleteById(id);
    }
}
