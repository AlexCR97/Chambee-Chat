package com.example.chambeechat.models;

import java.util.Date;

public class Mensaje {

    private String sender;
    private String reciever;
    private String message;
    private Date time;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReceiver(String reciever) {
        this.reciever = reciever;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "sender='" + sender + '\'' +
                ", reciever='" + reciever + '\'' +
                ", message='" + message + '\'' +
                ", time=" + time +
                '}';
    }
}
