package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.PersonRisk;
import top.btmdc.hr.repository.PersonRiskRepository;
import top.btmdc.hr.service.dto.PersonRiskDTO;
import top.btmdc.hr.service.mapper.PersonRiskMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.PersonRisk}.
 */
@Service
@Transactional
public class PersonRiskService {

    private static final Logger LOG = LoggerFactory.getLogger(PersonRiskService.class);

    private final PersonRiskRepository personRiskRepository;

    private final PersonRiskMapper personRiskMapper;

    public PersonRiskService(PersonRiskRepository personRiskRepository, PersonRiskMapper personRiskMapper) {
        this.personRiskRepository = personRiskRepository;
        this.personRiskMapper = personRiskMapper;
    }

    /**
     * Save a personRisk.
     *
     * @param personRiskDTO the entity to save.
     * @return the persisted entity.
     */
    public PersonRiskDTO save(PersonRiskDTO personRiskDTO) {
        LOG.debug("Request to save PersonRisk : {}", personRiskDTO);
        PersonRisk personRisk = personRiskMapper.toEntity(personRiskDTO);
        personRisk = personRiskRepository.save(personRisk);
        return personRiskMapper.toDto(personRisk);
    }

    /**
     * Update a personRisk.
     *
     * @param personRiskDTO the entity to save.
     * @return the persisted entity.
     */
    public PersonRiskDTO update(PersonRiskDTO personRiskDTO) {
        LOG.debug("Request to update PersonRisk : {}", personRiskDTO);
        PersonRisk personRisk = personRiskMapper.toEntity(personRiskDTO);
        personRisk = personRiskRepository.save(personRisk);
        return personRiskMapper.toDto(personRisk);
    }

    /**
     * Partially update a personRisk.
     *
     * @param personRiskDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PersonRiskDTO> partialUpdate(PersonRiskDTO personRiskDTO) {
        LOG.debug("Request to partially update PersonRisk : {}", personRiskDTO);

        return personRiskRepository
            .findById(personRiskDTO.getId())
            .map(existingPersonRisk -> {
                personRiskMapper.partialUpdate(existingPersonRisk, personRiskDTO);

                return existingPersonRisk;
            })
            .map(personRiskRepository::save)
            .map(personRiskMapper::toDto);
    }

    /**
     * Get all the personRisks with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PersonRiskDTO> findAllWithEagerRelationships(Pageable pageable) {
        return personRiskRepository.findAllWithEagerRelationships(pageable).map(personRiskMapper::toDto);
    }

    /**
     * Get one personRisk by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PersonRiskDTO> findOne(Long id) {
        LOG.debug("Request to get PersonRisk : {}", id);
        return personRiskRepository.findOneWithEagerRelationships(id).map(personRiskMapper::toDto);
    }

    /**
     * Delete the personRisk by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PersonRisk : {}", id);
        personRiskRepository.deleteById(id);
    }
}
