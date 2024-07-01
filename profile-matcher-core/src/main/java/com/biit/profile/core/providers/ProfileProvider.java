package com.biit.profile.core.providers;

import com.biit.server.providers.ElementProvider;
import com.biit.profile.persistence.entities.Profile;
import com.biit.profile.persistence.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileProvider extends ElementProvider<Profile, Long, ProfileRepository> {

    @Autowired
    public ProfileProvider(ProfileRepository repository) {
        super(repository);
    }

    public Optional<Profile> findByName(String name) {
        return getRepository().findByName(name);
    }
}
