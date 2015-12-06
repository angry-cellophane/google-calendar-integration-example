package com.helppen.integration.calendar.google.service

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.DateTime
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.model.Event
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

@Component
@PropertySource("classpath:/properties/app.properties")
class CalendarService {

    @Value("{app.name}")
    final String applicationName;

    /** Directory to store user credentials for this application. */

    final File dataStoreDirectory = new File(System.getProperty("user.home"), ".credentials/myApp");

    /** Global instance of the {@link com.google.api.client.util.store.FileDataStoreFactory}. */
    FileDataStoreFactory dataStoreFactory;

    /** Global instance of the JSON factory. */
    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance()
    /** Global instance of the HTTP transport. */
    HttpTransport httpTransport;

    /** Global instance of the scopes required by this quickstart. */
    final List<String> SCOPES = [CalendarScopes.CALENDAR_READONLY];

    private Calendar calendar

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    private Credential authorize() throws IOException {
        // Load client secrets.


        def secret = CalendarService.class.getResourceAsStream("/client_secret.json")
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(jsonFactory, new InputStreamReader(secret));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientSecrets, SCOPES)
                        .setDataStoreFactory(dataStoreFactory)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        println("Credentials saved to ${dataStoreDirectory.getAbsolutePath()}");
        return credential;
    }

    /**
     * Build and return an authorized Calendar client service.
     * @return an authorized Calendar client service
     * @throws IOException
     */
    private Calendar getCalendarService() throws IOException {
        Credential credential = authorize()
        new Calendar.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName(applicationName)
                .build()
    }


    @PostConstruct
    void init() {
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(dataStoreDirectory);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    List<Event> getEvents() {
        def currentDateTime = new DateTime(System.currentTimeMillis())

        def events = calendarService.events()
                .list("primary")
                .setMaxResults(10)
                .setTimeMin(currentDateTime)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute()
        events.getItems()
    }
}
