package com.chavau.univ_angers.univemarge.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = { "dateMaj" })
public class Autre extends Entity implements Personne {

    @JsonProperty("id")
    private int idAutre;

    @JsonProperty("nom")
    private String nom;

    @JsonProperty("prenom")
    private String prenom;

    @JsonProperty("email")
    private String email;

    public Autre(String nom, String prenom, String email, int idAutre) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.idAutre = idAutre;
    }

    public int getIdAutre() {
        return idAutre;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }
}
