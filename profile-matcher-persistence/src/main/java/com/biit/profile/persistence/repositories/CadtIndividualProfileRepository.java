package com.biit.profile.persistence.repositories;

import com.biit.profile.persistence.entities.cadt.CadtIndividualProfile;
import com.biit.server.persistence.repositories.ElementRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface CadtIndividualProfileRepository extends ElementRepository<CadtIndividualProfile, Long> {

}
