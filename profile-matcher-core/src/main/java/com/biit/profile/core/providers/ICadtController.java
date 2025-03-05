package com.biit.profile.core.providers;

import com.biit.drools.form.DroolsSubmittedForm;

import java.util.UUID;

public interface ICadtController {

    void newProfileReceived(DroolsSubmittedForm droolsSubmittedForm, UUID session);
}
