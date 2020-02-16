package com.chavau.univ_angers.univemarge.database.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@JsonIgnoreProperties(value = { "dateMaj" })
public class Evenement extends Entity {

    @JsonProperty("id")
    private int idEvenement;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss.S", timezone="Europe/Paris")
    @JsonProperty("dateDebut")
    private Date dateDebut;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss.S", timezone="Europe/Paris")
    @JsonProperty("dateFin")
    private Date dateFin;

    @JsonProperty("lieu")
    private String lieu;

    @JsonIgnore
    private int temoinRoulant;

    @JsonProperty("libelleEvenement")
    private String libelleEvenement;

    @JsonProperty("idCours")
    private int idCours;

    @JsonProperty("idTypeEmargement")
    private int typeEmargement;

    @JsonDeserialize(using = NumericBooleanDeserializer.class)
    @JsonProperty("deleted")
    private boolean deleted;

    // needed for jackson parser
    public Evenement() {}

    public Evenement(int idEvenement, Date dateDebut, Date dateFin, String lieu, int typeEmargement, String libelleEvenement, int idCours, boolean deleted) {
        this.idEvenement = idEvenement;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.lieu = lieu;
        this.typeEmargement = typeEmargement;
        this.libelleEvenement = libelleEvenement;
        this.idCours = idCours;
        this.deleted = deleted;
    }

    public int getIdEvenement() {
        return idEvenement;
    }

    public Date getDateDebut() {
        return dateDebut;
    }
    public String getDateDebutToString() {
        String pattern = "dd/MM/yyyy";

        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(dateDebut);

    }

    public Date getDateFin() {
        return dateFin;
    }

    public String getDateFinToString() {
        String pattern = "dd/MM/yyyy";

        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(dateFin);

    }

    public String getLieu() {
        return lieu;
    }

    public int getTypeEmargement() {
        return typeEmargement;
    }

    public String getLibelleEvenement() {
        return libelleEvenement;
    }

    public int getIdCours() {
        return idCours;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}