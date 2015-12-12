package com.helppen.integration.calendar.google.controller

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl
import com.google.api.services.calendar.CalendarScopes
import com.helppen.integration.calendar.google.service.GoogleCalendarFactory
import com.helppen.integration.calendar.google.service.GoogleCalendarService
import com.helppen.integration.calendar.google.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@PropertySource("classpath:/properties/google-oauth-credentials.properties")
class EventsController {

    static final REDIRECT_URI = 'http://localhost:8080/token'

    @Autowired
    GoogleCalendarService calendarService

    @Autowired
    GoogleCalendarFactory calendarFactory

    @Autowired
    AuthorizationCodeFlow flow

    @Autowired
    UserService userService

    @Value('${app.clientId}')
    String clientId

    @RequestMapping("/")
    void googleAuth(HttpServletResponse response) {
        def credentials = calendarService.getCredentialFor(userService.userName)

        if (!calendarService.isAuthenticated(credentials)) {
            def url = flow.newAuthorizationUrl()
            response.sendRedirect(getRedirectUri(url))
        }
        response.sendRedirect('http://localhost:8080/views/events')
    }

    @RequestMapping("/token")
    void token(@RequestParam String code, HttpServletResponse response) {
        println('code = ' + code)
        def responseToken = flow.newTokenRequest(code)
                .setRedirectUri('http://localhost:8080/token')
                .execute()

        flow.createAndStoreCredential(responseToken, userService.userName)

        response.sendRedirect('http://localhost:8080/views/events')
    }

    private String getRedirectUri(AuthorizationCodeRequestUrl url) {
        println("clientId = ${clientId}")
        url.setClientId(clientId)
                .setScopes([CalendarScopes.CALENDAR_READONLY])
                .setRedirectUri(REDIRECT_URI).build()
    }

    @RequestMapping("/views/events")
    String events(Model model) {
        def calendar = calendarFactory.getCalendarFor(userService.userName)
        model.addAttribute("events", calendar.events)
        "events"
    }
}
