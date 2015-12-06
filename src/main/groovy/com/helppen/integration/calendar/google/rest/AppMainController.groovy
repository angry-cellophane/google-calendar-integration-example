package com.helppen.integration.calendar.google.rest
import com.fasterxml.jackson.core.JsonFactory
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.DateTime
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.Events
import com.helppen.integration.calendar.google.service.CalendarService
import org.eclipse.jetty.server.HttpTransport
import org.eclipse.jetty.util.security.Credential
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AppMainController {

    @Autowired
    private CalendarService calendarService

    @RequestMapping("/")
    String index() {
        "index"
    }

    @RequestMapping("/events")
    List<Event> event() {
        calendarService.getEvents()
    }
}
