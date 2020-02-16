package com.chavau.univ_angers.univemarge.intermediaire;

import java.util.ArrayList;

public class MusculationData {
    private int capacite;
    private String tempsMinimum;
    private String occupation;
    private int heure;
    private int minute;

    public MusculationData(int capacite) {
        this.capacite = capacite;
        this.tempsMinimum = "";
        this.occupation = "";
        this.heure = 0;
        this.minute = 0;
    }

    public int getCapacite() {
        return capacite;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getTempsMinimum() {
        return tempsMinimum;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public void setOccupation(int courant, int capacite) {
        this.occupation = courant+"/"+capacite;
    }

    public void setTempsMinimum(int heure, int minute) {
        this.heure = heure;
        this.minute = minute;
        this.tempsMinimum = heure+"h"+minute;
    }

    public int getHeure() {
        return heure;
    }

    public int getMinute() {
        return minute;
    }
}
