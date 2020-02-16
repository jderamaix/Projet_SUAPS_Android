package com.chavau.univ_angers.univemarge.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(value = { "dateMaj" })
public class Inscription extends Entity {

    @JsonProperty("numero_personnel")
    private int idPersonnel;

    @JsonProperty("id")
    private int idInscription;

    @JsonProperty("id_evenement")
    private int idEvenement;

    @JsonProperty("numero_etudiant")
    private int numeroEtudiant;

    @JsonProperty("typeInscription")
    private String typeInscription;

    @JsonDeserialize(using = NumericBooleanDeserializer.class)
    @JsonProperty("deleted")
    private boolean deleted;

    @JsonProperty("id_autre")
    private int idAutre;

    // needed for jackson parser
    public Inscription() {}

    public Inscription(int idPersonnel, int idInscription, int idEvenement, int numeroEtudiant, boolean deleted, String typeInscription, int idAutre) {
        this.idPersonnel = idPersonnel;
        this.idInscription = idInscription;
        this.idEvenement = idEvenement;
        this.numeroEtudiant = numeroEtudiant;
        this.deleted = deleted;
        this.typeInscription = typeInscription;
        this.idAutre = idAutre;
    }

    public int getIdPersonnel() {
        return idPersonnel;
    }

    public int getIdInscription() {
        return idInscription;
    }

    public int getIdEvenement() {
        return idEvenement;
    }

    public int getNumeroEtudiant() {
        return numeroEtudiant;
    }

    public String getTypeInscription() {
        return typeInscription;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getIdAutre() {
        return idAutre;
    }
}
