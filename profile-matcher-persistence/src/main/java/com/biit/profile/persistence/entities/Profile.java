package com.biit.profile.persistence.entities;

import com.biit.database.encryption.SHA512HashGenerator;
import com.biit.database.encryption.StringCryptoConverter;
import com.biit.drools.form.DroolsSubmittedCategory;
import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.drools.form.DroolsSubmittedQuestion;
import com.biit.form.submitted.implementation.SubmittedObject;
import com.biit.kafka.config.ObjectMapperFactory;
import com.biit.profile.logger.ProfileLogger;
import com.biit.profile.persistence.entities.cadt.CadtArchetype;
import com.biit.profile.persistence.entities.cadt.CadtCompetence;
import com.biit.profile.persistence.entities.exceptions.InvalidProfileValueException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "profile", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "organization"})})
public class Profile extends SearchableCompetences<Long> {
    private static final int MAX_JSON_LENGTH = 10 * 1024 * 1024;
    public static final String CADT_PROFILE_FORM = "CADT_Profile_Creator";

    @Serial
    private static final long serialVersionUID = 4648629193294794935L;

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

    @Lob
    @Column(name = "description")
    @Convert(converter = StringCryptoConverter.class)
    private String description;


    @Column(name = "tracking_code")
    @Convert(converter = StringCryptoConverter.class)
    private String trackingCode;

    @Column(name = "tracking_code_by_hash")
    @Convert(converter = SHA512HashGenerator.class)
    private String trackingCodeByHash;


    @Column(name = "type")
    @Convert(converter = StringCryptoConverter.class)
    private String type;

    @Column(name = "type_by_hash")
    @Convert(converter = SHA512HashGenerator.class)
    private String typeByHash;

    @Transient
    private transient DroolsSubmittedForm entity;

    @Column(name = "content", length = MAX_JSON_LENGTH)
    @Convert(converter = StringCryptoConverter.class)
    private String content;

    @Column(name = "drools_id")
    private String droolsId;

    @Column(name = "form_version", nullable = false)
    private int formVersion;

    @Column(name = "session")
    private UUID session;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.nameByHash = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
        this.trackingCodeByHash = trackingCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        this.typeByHash = type;
    }

    public void setContent(String content) {
        this.content = content;
        this.entity = null;
    }


    public String getContent() {
        return content == null ? "" : content;
    }

    public String getNameByHash() {
        return nameByHash;
    }

    public void setNameByHash(String nameByHash) {
        this.nameByHash = nameByHash;
    }

    public String getTrackingCodeByHash() {
        return trackingCodeByHash;
    }

    public void setTrackingCodeByHash(String trackingCodeByHash) {
        this.trackingCodeByHash = trackingCodeByHash;
    }

    public String getTypeByHash() {
        return typeByHash;
    }

    public void setTypeByHash(String typeByHash) {
        this.typeByHash = typeByHash;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getDroolsId() {
        return droolsId;
    }

    public void setDroolsId(String droolsId) {
        this.droolsId = droolsId;
    }

    public int getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(int formVersion) {
        this.formVersion = formVersion;
    }

    public UUID getSession() {
        return session;
    }

    public void setSession(UUID session) {
        this.session = session;
    }

    @JsonIgnore
    public void setEntity(DroolsSubmittedForm entity) {
        try {
            setContent(ObjectMapperFactory.getObjectMapper().writeValueAsString(entity));
            this.entity = entity;
        } catch (JsonProcessingException e) {
            throw new InvalidProfileValueException(e);
        }
    }


    @JsonIgnore
    public DroolsSubmittedForm getEntity() {
        if (entity == null && !getContent().isEmpty()) {
            try {
                entity = ObjectMapperFactory.getObjectMapper().readValue(getContent(), new TypeReference<>() {
                });
            } catch (JsonProcessingException e) {
                ProfileLogger.errorMessage(this.getClass(), e);
                throw new InvalidProfileValueException(e);
            }
        }
        return entity;
    }


    public List<CadtCompetence> getDesiredCompetences() {
        final DroolsSubmittedForm submittedForm = getEntity();
        if (submittedForm == null) {
            throw new InvalidProfileValueException("No drools form found!");
        }

        if (!Objects.equals(submittedForm.getTag(), CADT_PROFILE_FORM)) {
            throw new InvalidProfileValueException("Invalid drools form found! Expected title is '"
                    + CADT_PROFILE_FORM + "' and current one is '" + submittedForm.getTag() + "'.");
        }

        final DroolsSubmittedCategory category = submittedForm
                .getChild(DroolsSubmittedCategory.class, "profile");
        final List<CadtCompetence> competences = new ArrayList<>();
        for (SubmittedObject question : category.getChildren()) {
            if (question instanceof DroolsSubmittedQuestion) {
                for (String answer : ((DroolsSubmittedQuestion) question).getAnswers()) {
                    final CadtArchetype archetype = CadtArchetype.fromTag(answer);
                    if (archetype == null) {
                        final CadtCompetence competence = CadtCompetence.fromTag(answer);
                        if (competence == null) {
                            ProfileLogger.warning(this.getClass(), "Invalid competence '{}' found!", answer);
                        } else {
                            competences.add(competence);
                        }
                    } else {
                        ProfileLogger.debug(this.getClass(), "Obtained archetype '{}'. Ignoring.", archetype);
                    }
                }
            }
        }
        return competences;
    }

    public void populateFields() {
        setAllCompetences(false);
        for (CadtCompetence competence : getDesiredCompetences()) {
            assignCompetence(competence);
        }
    }

    @Override
    public void validate() throws InvalidProfileValueException {

    }

}
