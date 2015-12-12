package com.helppen.integration.calendar.google.service

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class GoogleCalendarService {

    @Autowired
    GoogleAuthorizationCodeFlow flow

    Credential getCredentialFor(String userName) {
        flow.loadCredential(userName)
    }


    boolean isAuthenticated(Credential credential) {
        credential != null && (credential.getRefreshToken() != null || credential.getExpiresInSeconds() > 60)
    }


}
