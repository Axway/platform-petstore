package com.axway.resources;

import com.axway.api.Event;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Resource class based around {@link Event} models.
 */
@Path("/api/v1/event")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EventResource {

    /**
     * Receives an event from the PubSub service.
     *
     * @param event
     *      an event instance published by PubSub.
     */
    @POST
    public void receive(Event event) {
        // TODO: do something with a received PubSub event
    }
}
