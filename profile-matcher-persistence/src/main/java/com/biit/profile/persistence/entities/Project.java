package com.biit.profile.persistence.entities;

import com.biit.database.encryption.SHA512HashGenerator;
import com.biit.database.encryption.StringCryptoConverter;
import com.biit.server.persistence.entities.Element;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "projects", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "organization"})})
public class Project extends Element<Long> {

    @Serial
    private static final long serialVersionUID = -6147137272839730064L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @Convert(converter = StringCryptoConverter.class)
    private String name = "";


    @Column(name = "organization")
    @Convert(converter = StringCryptoConverter.class)
    private String organization = null;


    @Column(name = "name_by_hash")
    @Convert(converter = SHA512HashGenerator.class)
    private String nameByHash = "";

    @Column(name = "organization_by_hash")
    @Convert(converter = StringCryptoConverter.class)
    private String organizationByHash = null;

    @Lob
    @Column(name = "description")
    @Convert(converter = StringCryptoConverter.class)
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setNameByHash(name);
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
        setOrganizationByHash(organization);
    }

    public String getNameByHash() {
        return nameByHash;
    }

    public void setNameByHash(String nameByHash) {
        this.nameByHash = nameByHash;
    }

    public String getOrganizationByHash() {
        return organizationByHash;
    }

    public void setOrganizationByHash(String organizationByHash) {
        this.organizationByHash = organizationByHash;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
