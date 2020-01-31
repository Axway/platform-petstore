package com.axway.resources;

import com.axway.api.Event;
// start:pubsub
// import com.axway.client.socket.WebSocketClient;
// end:pubsub

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
        // This API will fire once an event has been received from the PubSub service.
        //
        // In our application, we can use this to know when a Pet has been added or removed,
        // and so we can notify any attached browsers to reload their UI to display the latest
        // set of pets. This allows the UI to be much more "reactive", and avoids having to poll.
        //
        // To enable this behaviour, you can emit a reload message to connected UIs:
        //
        // start:pubsub
        // WebSocketClient.send("RELOAD");
        // end:pubsub
    }
}
