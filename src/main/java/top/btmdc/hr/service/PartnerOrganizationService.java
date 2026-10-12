package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.PartnerOrganization;
import top.btmdc.hr.repository.PartnerOrganizationRepository;
import top.btmdc.hr.service.dto.PartnerOrganizationDTO;
import top.btmdc.hr.service.mapper.PartnerOrganizationMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.PartnerOrganization}.
 */
@Service
@Transactional
public class PartnerOrganizationService {

    private static final Logger LOG = LoggerFactory.getLogger(PartnerOrganizationService.class);

    private final PartnerOrganizationRepository partnerOrganizationRepository;

    private final PartnerOrganizationMapper partnerOrganizationMapper;

    public PartnerOrganizationService(
        PartnerOrganizationRepository partnerOrganizationRepository,
        PartnerOrganizationMapper partnerOrganizationMapper
    ) {
        this.partnerOrganizationRepository = partnerOrganizationRepository;
        this.partnerOrganizationMapper = partnerOrganizationMapper;
    }

    /**
     * Save a partnerOrganization.
     *
     * @param partnerOrganizationDTO the entity to save.
     * @return the persisted entity.
     */
    public PartnerOrganizationDTO save(PartnerOrganizationDTO partnerOrganizationDTO) {
        LOG.debug("Request to save PartnerOrganization : {}", partnerOrganizationDTO);
        PartnerOrganization partnerOrganization = partnerOrganizationMapper.toEntity(partnerOrganizationDTO);
        partnerOrganization = partnerOrganizationRepository.save(partnerOrganization);
        return partnerOrganizationMapper.toDto(partnerOrganization);
    }

    /**
     * Update a partnerOrganization.
     *
     * @param partnerOrganizationDTO the entity to save.
     * @return the persisted entity.
     */
    public PartnerOrganizationDTO update(PartnerOrganizationDTO partnerOrganizationDTO) {
        LOG.debug("Request to update PartnerOrganization : {}", partnerOrganizationDTO);
        PartnerOrganization partnerOrganization = partnerOrganizationMapper.toEntity(partnerOrganizationDTO);
        partnerOrganization = partnerOrganizationRepository.save(partnerOrganization);
        return partnerOrganizationMapper.toDto(partnerOrganization);
    }

    /**
     * Partially update a partnerOrganization.
     *
     * @param partnerOrganizationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PartnerOrganizationDTO> partialUpdate(PartnerOrganizationDTO partnerOrganizationDTO) {
        LOG.debug("Request to partially update PartnerOrganization : {}", partnerOrganizationDTO);

        return partnerOrganizationRepository
            .findById(partnerOrganizationDTO.getId())
            .map(existingPartnerOrganization -> {
                partnerOrganizationMapper.partialUpdate(existingPartnerOrganization, partnerOrganizationDTO);

                return existingPartnerOrganization;
            })
            .map(partnerOrganizationRepository::save)
            .map(partnerOrganizationMapper::toDto);
    }

    /**
     * Get one partnerOrganization by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PartnerOrganizationDTO> findOne(Long id) {
        LOG.debug("Request to get PartnerOrganization : {}", id);
        return partnerOrganizationRepository.findById(id).map(partnerOrganizationMapper::toDto);
    }

    /**
     * Delete the partnerOrganization by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PartnerOrganization : {}", id);
        partnerOrganizationRepository.deleteById(id);
    }
}
