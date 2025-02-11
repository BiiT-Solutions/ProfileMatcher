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

@Service
public class ProfileProvider extends ElementProvider<Profile, Long, ProfileRepository> {

    @Autowired
    public ProfileProvider(ProfileRepository repository) {
        super(repository);
    }

    public Optional<Profile> findByName(String name) {
        return getRepository().findByName(name);
    }

    public Set<Profile> findByTrackingCode(String name) {
        return getRepository().findByTrackingCode(name);
    }

    public Set<Profile> findByType(String name) {
        return getRepository().findByType(name);
    }

    public List<Profile> findByCompetencesIn(Collection<String> competences, int threshold) {
        return getRepository().findByCompetencesIn(competences, threshold);
    }
}
