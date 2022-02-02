package edu.msu.leemyou1.project1.Cloud.Models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "battleship")
public class TurnResult {
    @Attribute(name = "msg", required = false)
    private String activeGame;

    public String getActiveGame() {
        return activeGame;
    }

    public void setActiveGame(String activeGame) {
        this.activeGame = activeGame;
    }


    @Attribute(name = "status", required = false)
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TurnResult() {
    }

    public TurnResult(String status, String msg) {
        this.status = status;
        this.activeGame = activeGame;
    }
}
