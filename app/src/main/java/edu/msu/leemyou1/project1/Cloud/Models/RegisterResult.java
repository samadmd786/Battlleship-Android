package edu.msu.leemyou1.project1.Cloud.Models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Element;

@Root(name = "battleship")
public class RegisterResult {
    public RegisterResult(String status, String msg) {
        this.status = status;
        this.message = msg;
    }

    public RegisterResult() {}

    @Attribute(name = "status", required = false)
    private String status;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Attribute(name = "msg", required = false)
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}