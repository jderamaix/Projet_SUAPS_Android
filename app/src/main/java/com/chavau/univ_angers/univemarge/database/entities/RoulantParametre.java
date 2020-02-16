package com.chavau.univ_angers.univemarge.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(value = { "dateMaj" })
public class RoulantParametre extends Entity {

    @JsonProperty("id")
    private int idCours;

    @JsonProperty("")
    private Date tempsSeance;

    @JsonProperty("")
    private int maxPersonnes;

    // needed for jackson parser
    public RoulantParametre() {}

    public RoulantParametre(int idCours, Date tempsSeance, int maxPersonnes) {
        this.idCours = idCours;
        this.tempsSeance = tempsSeance;
        this.maxPersonnes = maxPersonnes;
    }

    public int getIdCours() {
        return idCours;
    }

    public Date getTempsSeance() {
        return tempsSeance;
    }

    public void setTempsSeance(Date tempsSeance) {
        this.tempsSeance = tempsSeance;
    }

    public int getMaxPersonnes() {
        return maxPersonnes;
    }

    public void setMaxPersonnes(int maxPersonnes) {
        this.maxPersonnes = maxPersonnes;
    }
}
