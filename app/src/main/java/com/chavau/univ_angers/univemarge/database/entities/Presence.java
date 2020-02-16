package com.chavau.univ_angers.univemarge.database.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;

public class Presence extends Entity {
    private int idPresence;
    private int idEvenement;

    @JsonProperty("idSeance")
    private int idSeance;

    @JsonProperty("idInscription")
    private int idInscription;

    @JsonProperty("numeroEtudiant")
    private int numeroEtudiant;

    @JsonProperty("statutPresence")
    private StatutPresence statutPresence;

    @JsonProperty("dateMaj")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss.S", timezone="Europe/Paris")
    private Date dateMaj;

    @JsonDeserialize(using = NumericBooleanDeserializer.class)
    @JsonProperty("deleted")
    private boolean deleted;

    private int idPersonnel;
    private int idAutre;

    // needed for jackson parser
    public Presence() {}

    public Presence(int idEvenement, int numeroEtudiant, StatutPresence statutPresence, boolean deleted, int idPersonnel, int idAutre) {
        this.idEvenement = idEvenement;
        this.numeroEtudiant = numeroEtudiant;
        this.statutPresence = statutPresence;
        this.deleted = deleted;
        this.idPersonnel = idPersonnel;
        this.idAutre = idAutre;
    }

    public Presence(int idPresence, int idEvenement, int numeroEtudiant, StatutPresence statutPresence, boolean deleted, int idPersonnel, int idAutre, Date dateMaj) {
        this.idPresence = idPresence;
        this.idEvenement = idEvenement;
        this.numeroEtudiant = numeroEtudiant;
        this.statutPresence = statutPresence;
        this.deleted = deleted;
        this.idPersonnel = idPersonnel;
        this.idAutre = idAutre;
        this.dateMaj = dateMaj;
    }

    public int getIdPresence() {
        return idPresence;
    }

    public int getIdEvenement() {
        return idEvenement;
    }

    public int getNumeroEtudiant() {
        return numeroEtudiant;
    }

    public StatutPresence getStatutPresence() {
        return statutPresence;
    }

    public int getIdPersonnel() {
        return idPersonnel;
    }

    public int getIdAutre() {
        return idAutre;
    }

    public Date getDateMaj() {
        return dateMaj;
    }

    public void setDateMaj(Date dateMaj) {
        this.dateMaj = dateMaj;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
