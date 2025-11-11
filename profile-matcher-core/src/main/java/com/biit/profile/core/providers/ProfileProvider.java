package com.biit.profile.core.providers;

/*-
 * #%L
 * Profile Matcher (Core)
 * %%
 * Copyright (C) 2024 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.profile.core.exceptions.InvalidFormException;
import com.biit.profile.persistence.entities.Profile;
import com.biit.profile.persistence.repositories.ProfileRepository;
import com.biit.profile.persistence.repositories.ProjectProfileRepository;
import com.biit.profile.persistence.repositories.UserProfileRepository;
import com.biit.profile.persistence.repositories.UserProjectProfileRepository;
import com.biit.server.providers.ElementProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.biit.database.encryption.KeyProperty.getEncryptionKey;

@Service
public class ProfileProvider extends ElementProvider<Profile, Long, ProfileRepository> {

    private final UserProjectProfileRepository userProjectProfileRepository;
    private final ProjectProfileRepository projectProfileRepository;
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public ProfileProvider(ProfileRepository repository,
                           UserProjectProfileRepository userProjectProfileRepository,
                           ProjectProfileRepository projectProfileRepository,
                           UserProfileRepository userProfileRepository) {
        super(repository);
        this.userProjectProfileRepository = userProjectProfileRepository;
        this.projectProfileRepository = projectProfileRepository;
        this.userProfileRepository = userProfileRepository;
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

    @Override
    protected void populateHash(Profile profile) {
        super.populateHash(profile);
        profile.setNameByHash(profile.getName());
        profile.setTypeByHash(profile.getType());
        profile.setTrackingCodeByHash(profile.getTrackingCode());
    }


    public Profile create(DroolsSubmittedForm droolsSubmittedForm, UUID session) {
        if (!Objects.equals(droolsSubmittedForm.getName(), Profile.CADT_PROFILE_FORM)) {
            throw new InvalidFormException("Form '" + droolsSubmittedForm.getName() + "' is not the correct form.");
        }
        final Profile vacancyProfile = new Profile();
        vacancyProfile.setCreatedBy(droolsSubmittedForm.getSubmittedBy());
        vacancyProfile.setCreatedAt(droolsSubmittedForm.getSubmittedAt());
        vacancyProfile.setOrganization(droolsSubmittedForm.getOrganization());
        vacancyProfile.setCreatedOn(droolsSubmittedForm.getOrganization());
        vacancyProfile.setContent(droolsSubmittedForm.toJson());
        vacancyProfile.setDroolsId(droolsSubmittedForm.getId());
        vacancyProfile.setFormVersion(droolsSubmittedForm.getVersion());
        vacancyProfile.setSession(session);

        final String formName;
        final Iterator<String> iterator = droolsSubmittedForm.getQuestion("metadata", "name").getAnswers().iterator();
        if (iterator.hasNext()) {
            formName = iterator.next();
        } else {
            formName = UUID.randomUUID().toString();
        }

        vacancyProfile.setName(formName);
        vacancyProfile.populateFields();

        return vacancyProfile;
    }


    @Override
    public void delete(Profile entity) {
        projectProfileRepository.deleteByIdProfileId(entity.getId());
        userProjectProfileRepository.deleteByIdProfileId(entity.getId());
        userProfileRepository.deleteByIdProfileId(entity.getId());
        super.delete(entity);
    }


    @Override
    public void deleteById(Long id) {
        projectProfileRepository.deleteByIdProfileId(id);
        userProjectProfileRepository.deleteByIdProfileId(id);
        userProfileRepository.deleteByIdProfileId(id);
        super.deleteById(id);
    }


    @Override
    public void deleteAll() {
        projectProfileRepository.deleteByIdProfileIdNotNull();
        userProjectProfileRepository.deleteByIdProfileIdNotNull();
        userProfileRepository.deleteAll();
        super.deleteAll();
    }


    @Override
    public void deleteAll(Collection<Profile> profiles) {
        profiles.forEach(p -> {
            projectProfileRepository.deleteByIdProfileId(p.getId());
            userProjectProfileRepository.deleteByIdProfileId(p.getId());
            userProfileRepository.deleteByIdProfileId(p.getId());
        });
        super.deleteAll(profiles);
    }
}
