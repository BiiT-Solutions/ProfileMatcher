package com.biit.profile.core.handlers;


import com.biit.profile.core.controllers.ProfileController;
import org.springframework.stereotype.Service;

@Service
public class ProfileCadtHandler extends CadtHandler {

    public ProfileCadtHandler(ProfileController profileController) {
        super(profileController);
    }

}
