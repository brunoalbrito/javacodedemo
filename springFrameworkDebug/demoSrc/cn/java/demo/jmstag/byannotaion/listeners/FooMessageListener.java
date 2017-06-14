package cn.java.demo.jmstag.byannotaion.listeners;

import org.springframework.jms.annotation.JmsListener;

import cn.java.demo.jmstag.message.Email;

public class FooMessageListener {
    @JmsListener(destination = "mailbox", containerFactory = "myFactory")
    public void receiveMessage(Email email) {
        System.out.println("Received <" + email + ">");
    }
}