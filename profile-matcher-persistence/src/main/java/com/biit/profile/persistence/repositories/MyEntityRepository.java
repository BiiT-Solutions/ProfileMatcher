package com.biit.profile.persistence.repositories;

import com.biit.profile.persistence.entities.MyEntity;
import com.biit.server.persistence.repositories.ElementRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface MyEntityRepository extends ElementRepository<MyEntity, Long> {

    Optional<MyEntity> findByName(String name);

}
