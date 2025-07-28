package com.biit.profile.persistence.entities;

import com.biit.server.persistence.entities.StorableObject;
import jakarta.persistence.Cacheable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "project_profiles", indexes = {
        @Index(name = "ind_projects", columnList = "project_id"),
        @Index(name = "ind_profiles", columnList = "profile_id"),
})
public class ProjectProfile extends StorableObject {

    @Serial
    private static final long serialVersionUID = -6286998724134389762L;

    @EmbeddedId
    private ProjectProfileId id;

    public ProjectProfile() {
        super();
    }

    public ProjectProfile(Long projectId, Long profileId) {
        this();
        setId(new ProjectProfileId(projectId, profileId));
    }


    public ProjectProfileId getId() {
        return id;
    }

    public void setId(ProjectProfileId id) {
        this.id = id;
    }
}
