package cn.java.demo.jmstag.byxml.listeners;

import cn.java.demo.jmstag.message.Email;

public class FooMessageListener {
	
    public void receiveMessage(Email email) {
        System.out.println("Received <" + email + ">");
    }
}