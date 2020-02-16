package com.chavau.univ_angers.univemarge.intermediaire;

import android.os.Parcel;
import android.os.Parcelable;

public class Personnel implements Parcelable {
    private String nom;
    private String prenom;
    private String heurePassee;
    private int heure;
    private int minute;

    public Personnel(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
        this.heurePassee = "00h00";
        this.heure = 0;
        this.minute = 0;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getHeurePassee() {
        return heurePassee;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setHeurePassee(int heure, int minute) {
        this.heure = heure;
        this.minute = minute;

        if (heure > 0 && minute > 10)
            this.heurePassee = heure+" h "+minute;
        else if (heure > 0 && minute < 10)
            this.heurePassee = heure+" h 0"+minute;
        else
            this.heurePassee = minute+" min";
    }

    public int getHeure() {
        return heure;
    }

    public int getMinute() {
        return minute;
    }

    /**************************PARCELABLE**************************/

    public Personnel(Parcel parcel) {
        nom = parcel.readString();
        prenom = parcel.readString();
        heurePassee = parcel.readString();
    }

    public static final Creator<Personnel> CREATOR = new Creator<Personnel>() {
        @Override
        public Personnel createFromParcel(Parcel parcel) {
            return new Personnel(parcel);
        }

        @Override
        public Personnel[] newArray(int i) {
            return new Personnel[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getNom());
        parcel.writeString(getPrenom());
        parcel.writeString(getHeurePassee());
    }
}
