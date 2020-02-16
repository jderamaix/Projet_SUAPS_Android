package com.chavau.univ_angers.univemarge.database.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class PresenceRoulant extends Entity {

    @JsonProperty("")
    private int idRoulant;
    private int idEvenement;
    private int numeroEtudiant;
    private Date temps;
    private Date dateEntree;
    private Date dateSortie;
    private int idPersonnel;
    private int idAutre;

    // needed for jackson parser
    public PresenceRoulant() {}

    public PresenceRoulant(int idEvenement, int numeroEtudiant, Date temps, Date dateEntree, Date dateSortie, int idPersonnel, int idAutre) {
        this.idEvenement = idEvenement;
        this.numeroEtudiant = numeroEtudiant;
        this.temps = temps;
        this.dateEntree = dateEntree;
        this.dateSortie = dateSortie;
        this.idPersonnel = idPersonnel;
        this.idAutre = idAutre;
    }

    public PresenceRoulant(int idRoulant, int idEvenement, int numeroEtudiant, Date temps, Date dateEntree, Date dateSortie, int idPersonnel, int idAutre) {
        this.idRoulant = idRoulant;
        this.idEvenement = idEvenement;
        this.numeroEtudiant = numeroEtudiant;
        this.temps = temps;
        this.dateEntree = dateEntree;
        this.dateSortie = dateSortie;
        this.idPersonnel = idPersonnel;
        this.idAutre = idAutre;
    }

    public int getIdRoulant() {
        return idRoulant;
    }

    public int getIdEvenement() {
        return idEvenement;
    }

    public int getNumeroEtudiant() {
        return numeroEtudiant;
    }

    public Date getTemps() {
        return temps;
    }

    public Date getDateEntree() {
        return dateEntree;
    }

    public Date getDateSortie() {
        return dateSortie;
    }

    public int getIdPersonnel() {
        return idPersonnel;
    }

    public int getIdAutre() {
        return idAutre;
    }
}
