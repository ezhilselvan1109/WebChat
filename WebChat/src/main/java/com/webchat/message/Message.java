package com.webchat.message;

public class Message {
    private  int message_id;
    private  int incoming_id;
    private  int outgoing_id;
    private String message;

    public Message(int message_id, int incoming_id, int outgoing_id, String message) {
        this.message_id = message_id;
        this.incoming_id = incoming_id;
        this.outgoing_id = outgoing_id;
        this.message = message;
    }

    public Message(int incoming_id, int outgoing_id, String message) {
        this.incoming_id = incoming_id;
        this.outgoing_id = outgoing_id;
        this.message = message;
    }
}