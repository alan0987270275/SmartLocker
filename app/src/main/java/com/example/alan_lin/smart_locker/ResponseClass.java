package com.example.alan_lin.smart_locker;

import java.util.HashMap;
import java.util.Map;

public class ResponseClass {
    String greetings;
    Map returnmap = new HashMap();

    public String getGreetings() {
        return greetings;
    }
    public Map getMap(){return returnmap;}

    public void setGreetings(String greetings) {
        this.greetings = greetings;
    }

    public void setMap(Map returnmap) {
        this.returnmap = returnmap;
    }

    public ResponseClass(String greetings) {
        this.greetings = greetings;
        this.returnmap=returnmap;
    }

    public ResponseClass() {
    }
}
