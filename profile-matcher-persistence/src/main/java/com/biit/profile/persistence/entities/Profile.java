package com.biit.profile.persistence.entities;

import com.biit.database.encryption.StringCryptoConverter;
import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.kafka.config.ObjectMapperFactory;
import com.biit.profile.logger.ProfileLogger;
import com.biit.profile.persistence.entities.exceptions.InvalidProfileValueException;
import com.biit.server.persistence.entities.Element;
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
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "profile")
public class Profile extends Element<Long> {
    private static final int MAX_JSON_LENGTH = 10 * 1024 * 1024;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "name", nullable = false, unique = true)
    @Convert(converter = StringCryptoConverter.class)
    private String name = "";


    @Lob
    @Column(name = "description")
    @Convert(converter = StringCryptoConverter.class)
    private String description;


    @Column(name = "tracking_code")
    @Convert(converter = StringCryptoConverter.class)
    private String trackingCode;


    @Column(name = "type")
    @Convert(converter = StringCryptoConverter.class)
    private String type;

    @Transient
    private transient DroolsSubmittedForm entity;

    @Column(name = "content", length = MAX_JSON_LENGTH)
    @Convert(converter = StringCryptoConverter.class)
    private String content;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
        this.entity = null;
    }


    public String getContent() {
        return content == null ? "" : content;
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
        if (getContent() != null && !getContent().isEmpty()) {
            try {
                entity = ObjectMapperFactory.getObjectMapper().readValue(getContent(), getJsonParser());
            } catch (JsonProcessingException e) {
                ProfileLogger.errorMessage(this.getClass(), e);
                throw new InvalidProfileValueException(e);
            }
        }
        return entity;
    }


    protected TypeReference<DroolsSubmittedForm> getJsonParser() {
        return new TypeReference<>() {
        };
    }
}
