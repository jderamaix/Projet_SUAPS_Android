package com.chavau.univ_angers.univemarge.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;

@JsonIgnoreProperties(value = { "dateMaj" })

public class Responsable extends Entity {

    @JsonProperty("id_evenement")
    private int idEvenement;

    @JsonProperty("id_responsable")
    private int idPersonnelResponsable;

    //@JsonProperty("")
    //private Date dateMaj;

    @JsonDeserialize(using = NumericBooleanDeserializer.class)
    @JsonProperty("deleted")
    private boolean deleted;

    // needed for jackson parser
    public Responsable() {}

    public Responsable(int idEvenement, int idPersonnelResponsable, boolean deleted) {
        this.idEvenement = idEvenement;
        this.idPersonnelResponsable = idPersonnelResponsable;
        this.deleted = deleted;
    }

    public int getIdEvenement() {
        return idEvenement;
    }

    public int getIdPersonnelResponsable() {
        return idPersonnelResponsable;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
