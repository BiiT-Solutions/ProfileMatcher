package com.biit.profile.persistence.repositories;

import com.biit.profile.persistence.entities.cadt.CadtIndividualProfile;
import com.biit.server.persistence.repositories.ElementRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface CadtIndividualProfileRepository extends ElementRepository<CadtIndividualProfile, Long> {

}
