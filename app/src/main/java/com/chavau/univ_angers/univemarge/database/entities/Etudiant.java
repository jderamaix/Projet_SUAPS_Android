package com.chavau.univ_angers.univemarge.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.sql.Blob;

@JsonIgnoreProperties(value = { "dateMaj" })
public class Etudiant extends Entity implements Personne {

    @JsonProperty("numero")
    private int numeroEtudiant;

    @JsonProperty("id_etudiant")
    private int idEtudiant;

    @JsonProperty("nom")
    private String nom;

    @JsonProperty("prenom")
    private String prenom;

    @JsonProperty("no_mifare")
    private String no_mifare;

    @JsonProperty("email")
    private String email;

    @JsonIgnore
    private Blob photo;

    @JsonDeserialize(using = NumericBooleanDeserializer.class)
    @JsonProperty("deleted")
    private boolean deleted;

    // needed for jackson parser
    public Etudiant() {}

    public Etudiant(String nom, String prenom, String email, int numeroEtudiant,int idEtudiant, String no_mifare, Blob photo, boolean deleted) {
        this.numeroEtudiant = numeroEtudiant;
        this.idEtudiant = idEtudiant;
        this.nom = nom;
        this.prenom = prenom;
        this.no_mifare = no_mifare;
        this.email = email;
        this.photo = photo;
        this.deleted = deleted;
    }

    public int getNumeroEtudiant() {
        return numeroEtudiant;
    }

    public int getIdEtudiant() {
        return idEtudiant;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNo_mifare() {
        return no_mifare;
    }

    public String getEmail() {
        return email;
    }

    public Blob getPhoto() {
        return photo;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
