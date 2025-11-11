package com.biit.profile.core.exceptions;

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

import com.biit.logger.ExceptionType;
import com.biit.server.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CandidateNotFoundException extends NotFoundException {
    private static final long serialVersionUID = 7232994111675694372L;

    public CandidateNotFoundException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public CandidateNotFoundException(Class<?> clazz, String message) {
        super(clazz, message);
    }

    public CandidateNotFoundException(Class<?> clazz) {
        this(clazz, "Candidate not found");
    }

    public CandidateNotFoundException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}
