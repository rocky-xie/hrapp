package top.btmdc.hr.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.PersonSkill;
import top.btmdc.hr.repository.PersonSkillRepository;
import top.btmdc.hr.service.dto.PersonSkillDTO;
import top.btmdc.hr.service.mapper.PersonSkillMapper;

/**
 * Service Implementation for managing {@link top.btmdc.hr.domain.PersonSkill}.
 */
@Service
@Transactional
public class PersonSkillService {

    private static final Logger LOG = LoggerFactory.getLogger(PersonSkillService.class);

    private final PersonSkillRepository personSkillRepository;

    private final PersonSkillMapper personSkillMapper;

    public PersonSkillService(PersonSkillRepository personSkillRepository, PersonSkillMapper personSkillMapper) {
        this.personSkillRepository = personSkillRepository;
        this.personSkillMapper = personSkillMapper;
    }

    /**
     * Save a personSkill.
     *
     * @param personSkillDTO the entity to save.
     * @return the persisted entity.
     */
    public PersonSkillDTO save(PersonSkillDTO personSkillDTO) {
        LOG.debug("Request to save PersonSkill : {}", personSkillDTO);
        PersonSkill personSkill = personSkillMapper.toEntity(personSkillDTO);
        personSkill = personSkillRepository.save(personSkill);
        return personSkillMapper.toDto(personSkill);
    }

    /**
     * Update a personSkill.
     *
     * @param personSkillDTO the entity to save.
     * @return the persisted entity.
     */
    public PersonSkillDTO update(PersonSkillDTO personSkillDTO) {
        LOG.debug("Request to update PersonSkill : {}", personSkillDTO);
        PersonSkill personSkill = personSkillMapper.toEntity(personSkillDTO);
        personSkill = personSkillRepository.save(personSkill);
        return personSkillMapper.toDto(personSkill);
    }

    /**
     * Partially update a personSkill.
     *
     * @param personSkillDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PersonSkillDTO> partialUpdate(PersonSkillDTO personSkillDTO) {
        LOG.debug("Request to partially update PersonSkill : {}", personSkillDTO);

        return personSkillRepository
            .findById(personSkillDTO.getId())
            .map(existingPersonSkill -> {
                personSkillMapper.partialUpdate(existingPersonSkill, personSkillDTO);

                return existingPersonSkill;
            })
            .map(personSkillRepository::save)
            .map(personSkillMapper::toDto);
    }

    /**
     * Get all the personSkills with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PersonSkillDTO> findAllWithEagerRelationships(Pageable pageable) {
        return personSkillRepository.findAllWithEagerRelationships(pageable).map(personSkillMapper::toDto);
    }

    /**
     * Get one personSkill by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PersonSkillDTO> findOne(Long id) {
        LOG.debug("Request to get PersonSkill : {}", id);
        return personSkillRepository.findOneWithEagerRelationships(id).map(personSkillMapper::toDto);
    }

    /**
     * Delete the personSkill by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PersonSkill : {}", id);
        personSkillRepository.deleteById(id);
    }
}
