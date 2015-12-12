package com.helppen.integration.calendar.google.service

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.calendar.Calendar
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

@Component
@PropertySource("classpath:/properties/app.properties")
class GoogleCalendarFactory {

    @Value('${app.name}')
    String applicationName

    @Autowired
    GoogleAuthorizationCodeFlow flow

    @Autowired
    GoogleCalendarService calendarService

    private File dataStoreDirectory;
    private JsonFactory jsonFactory

    private HttpTransport httpTransport;

    @PostConstruct
    void init() {
        try {
            jsonFactory = JacksonFactory.getDefaultInstance()
            dataStoreDirectory = new File(System.getProperty("user.home"), ".credentials/myApp")

            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    GoogleCalendar getCalendarFor(String userName) {
        def credential = flow.loadCredential(userName)
        def cal = null
        if (calendarService.isAuthenticated(credential)) {
            cal = new Calendar.Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName(applicationName)
                    .build()
        }

        new GoogleCalendar(cal)
    }

}
