package com.chavau.univ_angers.univemarge.database.entities;

/**
 * La classe personne nous permet de regrouper la table Personnel, Etudiant et Autre
 */
public interface Personne {
    String getNom();
    String getPrenom();
    String getEmail();
}
