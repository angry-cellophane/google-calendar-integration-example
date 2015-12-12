package com.helppen.integration.calendar.google.service

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.calendar.CalendarScopes
import org.springframework.beans.factory.FactoryBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

@Component
@PropertySource([
        'classpath:/properties/app.properties',
        'classpath:/properties/google-oauth-credentials.properties'
])
class GoogleAuthorizationCodeFlowFactory implements FactoryBean<GoogleAuthorizationCodeFlow>{

    @Value("app.google.passwords.dir")
    String dataStoreDirectoryName

    private File dataStoreDirectory;
    private JsonFactory jsonFactory
    private GoogleClientSecrets clientSecrets

    /** Global instance of the {@link com.google.api.client.util.store.FileDataStoreFactory}. */
    private FileDataStoreFactory dataStoreFactory;

    /** Global instance of the HTTP transport. */
    private HttpTransport httpTransport;

    @Value('${app.clientId}')
    private String clientId

    private final List<String> SCOPES = [CalendarScopes.CALENDAR_READONLY];

    @PostConstruct
    void init() {
        try {
            jsonFactory = JacksonFactory.getDefaultInstance()
            dataStoreDirectory = new File(System.getProperty("user.home"), ".credentials/myApp")

            def secret = GoogleCalendar.class.getResourceAsStream("/client_secret.json")
            clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(secret));

            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(new File(dataStoreDirectoryName));
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }


    @Override
    GoogleAuthorizationCodeFlow getObject() throws Exception {
        new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientSecrets, SCOPES)
                        .setScopes([CalendarScopes.CALENDAR_READONLY])
                        .setClientId(clientId)
                        .setDataStoreFactory(dataStoreFactory)
                        .build();
    }

    @Override
    Class<?> getObjectType() {
        GoogleAuthorizationCodeFlow.class
    }

    @Override
    boolean isSingleton() {
        true
    }
}
