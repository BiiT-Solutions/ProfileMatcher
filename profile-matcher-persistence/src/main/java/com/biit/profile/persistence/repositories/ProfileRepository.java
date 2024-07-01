package com.biit.profile.persistence.repositories;

import com.biit.profile.persistence.entities.Profile;
import com.biit.server.persistence.repositories.ElementRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface ProfileRepository extends ElementRepository<Profile, Long> {

    Optional<Profile> findByName(String name);

}
