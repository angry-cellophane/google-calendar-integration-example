package com.helppen.integration.calendar.google.service

import com.google.api.client.util.DateTime
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.Event

class GoogleCalendar {

    private final Calendar calendar

    GoogleCalendar(Calendar calendar) {
        this.calendar = calendar
    }

    List<Event> getEvents() {
        def currentDateTime = new DateTime(System.currentTimeMillis())
        if (!calendar) {
            []
        } else {
            calendar.events()
                    .list("primary")
                    .setMaxResults(10)
                    .setTimeMin(currentDateTime)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute().getItems()
        }
    }
}
