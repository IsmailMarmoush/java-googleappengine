package com.marmoush.birj.poc.jaxrs.async;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Path("/chat")
public class Chat {

    final static Map<String, AsyncResponse> waiters = new ConcurrentHashMap<>();
    final static ExecutorService ex = Executors.newSingleThreadExecutor();

    @Path("/{nick}")
    @GET
    @Produces("text/plain")
    public void hangUp(@Suspended AsyncResponse asyncResp, @PathParam("nick") String nick) {
    	asyncResp.setTimeout(1, TimeUnit.HOURS);
        waiters.put(nick, asyncResp);
        System.out.println("nickname received:"+nick);
    }

    @Path("/{nick}")
    @POST
    @Produces("text/plain")
    @Consumes("text/plain")
    public String sendMessage(final @PathParam("nick") String nick, final String message) {

        ex.submit(new Runnable() {
            @Override
            public void run() {
                Set<String> nicks = waiters.keySet();
                for (String n : nicks) {
                    // Sends message to all, except sender
                    if (!n.equalsIgnoreCase(nick)){
                        waiters.get(n).resume(nick + " said that: " + message);
                        System.out.println("sending message to others");
                    }
                }
            }
        });

        return "Message is sent..";
    }
}