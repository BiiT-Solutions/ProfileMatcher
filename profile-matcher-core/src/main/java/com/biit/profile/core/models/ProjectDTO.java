package com.biit.profile.core.models;

import com.biit.server.controllers.models.ElementDTO;

import java.io.Serial;

public class ProjectDTO extends ElementDTO<Long> {

    @Serial
    private static final long serialVersionUID = -3438561902407112691L;

    private Long id;

    private String name = "";

    private String organization = null;


    private String description;

    public ProjectDTO() {
        super();
    }

    public ProjectDTO(String name, String description, String organization) {
        this();
        this.name = name;
        this.description = description;
        this.organization = organization;
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

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
