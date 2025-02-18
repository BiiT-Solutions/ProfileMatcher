package com.biit.profile.core.providers;

import com.biit.profile.persistence.entities.Profile;
import com.biit.profile.persistence.repositories.ProfileRepository;
import com.biit.server.providers.ElementProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.biit.database.encryption.KeyProperty.getEncryptionKey;

@Service
public class ProfileProvider extends ElementProvider<Profile, Long, ProfileRepository> {

    @Autowired
    public ProfileProvider(ProfileRepository repository) {
        super(repository);
    }

    @Override
    public Optional<Profile> findById(Long id) {
        final Optional<Profile> profile = super.findById(id);
        profile.ifPresent(this::populateHash);
        return profile;
    }

    @Override
    public Profile save(Profile entity) {
        populateHash(entity);
        final Profile entityStored = super.save(entity);
        populateHash(entityStored);
        return entityStored;
    }

    @Override
    public List<Profile> saveAll(Collection<Profile> entities) {
        entities.forEach(this::populateHash);
        final List<Profile> storedEntities = super.saveAll(entities);
        storedEntities.forEach(this::populateHash);
        return storedEntities;
    }

    public Optional<Profile> findByName(String name) {
        final Optional<Profile> profile;
        if (getEncryptionKey() != null && !getEncryptionKey().isBlank()) {
            profile = getRepository().findByNameByHash(name);
        } else {
            profile = getRepository().findByName(name);
        }
        return profile;
    }

    public Set<Profile> findByTrackingCode(String name) {
        final Set<Profile> profiles;
        if (getEncryptionKey() != null && !getEncryptionKey().isBlank()) {
            profiles = getRepository().findByTrackingCodeByHash(name);
        } else {
            profiles = getRepository().findByTrackingCode(name);
        }
        return profiles;
    }

    public Set<Profile> findByType(String name) {
        final Set<Profile> profiles;
        if (getEncryptionKey() != null && !getEncryptionKey().isBlank()) {
            profiles = getRepository().findByTypeByHash(name);
        } else {
            profiles = getRepository().findByType(name);
        }
        return profiles;
    }

    public List<Profile> findByCompetencesIn(Collection<String> competences, int threshold) {
        return getRepository().findByCompetencesIn(competences, threshold);
    }

    private void populateHash(Profile profile) {
        profile.setCreatedByHash(profile.getCreatedBy());
        profile.setNameByHash(profile.getName());
        profile.setTypeByHash(profile.getType());
        profile.setTrackingCodeByHash(profile.getTrackingCode());
    }
}
