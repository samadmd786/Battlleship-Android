package edu.msu.leemyou1.project1;

import java.util.ArrayList;

public class Player {

    private final String name;

    private ArrayList<Ship> pieces = new ArrayList<>();

    public Player(String playerName) {
        name = playerName;
    }

    public void setShip(ArrayList<Ship> myShip) {
        pieces = myShip;
    }

    public ArrayList<Ship> getShip() {
        return pieces;
    }

    public String getName() {
        return name;
    }
}