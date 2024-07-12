package com.biit.profile.core.converters;

import com.biit.profile.core.converters.models.ProfileCandidateCommentConverterRequest;
import com.biit.profile.core.models.ProfileCandidateCommentDTO;
import com.biit.profile.persistence.entities.ProfileCandidateComment;
import com.biit.server.controller.converters.ElementConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ProfileCandidateCommentConverter extends ElementConverter<ProfileCandidateComment, ProfileCandidateCommentDTO,
        ProfileCandidateCommentConverterRequest> {


    @Override
    protected ProfileCandidateCommentDTO convertElement(ProfileCandidateCommentConverterRequest from) {
        final ProfileCandidateCommentDTO profileDTO = new ProfileCandidateCommentDTO();
        BeanUtils.copyProperties(from.getEntity(), profileDTO);
        return profileDTO;
    }

    @Override
    public ProfileCandidateComment reverse(ProfileCandidateCommentDTO to) {
        if (to == null) {
            return null;
        }
        final ProfileCandidateComment profile = new ProfileCandidateComment();
        BeanUtils.copyProperties(to, profile);
        return profile;
    }
}
