package com.helppen.integration.calendar.google.rest

import com.google.api.services.calendar.model.Event
import com.helppen.integration.calendar.google.service.GoogleCalendar
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AppMainController {


    @RequestMapping("/events")
    List<Event> event() {

    }
}
