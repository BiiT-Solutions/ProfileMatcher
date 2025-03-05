package com.biit.profile.core.models;

import com.biit.server.controllers.models.ElementDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serial;

public class ProfileDTO extends ElementDTO<Long> {

    @Serial
    private static final long serialVersionUID = 1549251750715528563L;

    private Long id;

    private String name = "";

    private String description;

    private String trackingCode;

    private String type;

    private String organization = null;

    @JsonSerialize(using = JsonValueSerializer.class)
    @JsonDeserialize(using = JsonValueDeserializer.class)
    private String content;

    public ProfileDTO() {
        super();
    }

    public ProfileDTO(String name) {
        this();
        this.name = name;
    }

    public ProfileDTO(String name, String content) {
        this();
        setName(name);
        setContent(content);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
}
