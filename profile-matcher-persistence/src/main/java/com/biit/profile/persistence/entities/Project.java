package com.biit.profile.persistence.entities;

/*-
 * #%L
 * Profile Matcher (Persistence)
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
@Table(name = "projects", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "created_on"})})
public class Project extends Element<Long> {

    @Serial
    private static final long serialVersionUID = -6147137272839730064L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @Convert(converter = StringCryptoConverter.class)
    private String name = "";


    @Column(name = "name_by_hash")
    @Convert(converter = SHA512HashGenerator.class)
    private String nameByHash = "";

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

    public String getNameByHash() {
        return nameByHash;
    }

    public void setNameByHash(String nameByHash) {
        this.nameByHash = nameByHash;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
