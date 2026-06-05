package top.btmdc.hr.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import top.btmdc.hr.domain.Person;

/**
 * Spring Data JPA repository for the Person entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {}
