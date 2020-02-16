package com.chavau.univ_angers.univemarge.database.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.chavau.univ_angers.univemarge.database.DatabaseHelper;
import com.chavau.univ_angers.univemarge.database.Identifiant;
import com.chavau.univ_angers.univemarge.database.entities.Autre;
import com.chavau.univ_angers.univemarge.database.entities.Etudiant;
import com.chavau.univ_angers.univemarge.database.entities.Personne;
import com.chavau.univ_angers.univemarge.database.entities.Personnel;

import java.util.ArrayList;

public abstract class DAO<Type> {

    protected DatabaseHelper helper;

    public DAO(DatabaseHelper helper) {
        this.helper = helper;
    }

    /**
     * Transforme un item en ContentValues
     *
     * @param item
     * @return ContentValues
     */
    public abstract ContentValues getContentValues(Type item);

    /**
     * Insert un item
     *
     * @param item
     * @return long
     */
    public abstract long insertItem(Type item);

    /**
     * Met a jour un item
     *
     * @param id
     * @param item
     * @return int
     */
    public abstract int updateItem(Identifiant id, Type item);

    /**
     * Supprime un item
     *
     * @param id
     * @param item
     * @return int
     */
    public abstract int removeItem(Identifiant id, Type item);

    /**
     * Retourne un item selon ca clee primaire
     *
     * @param id clee(s) primaire(s)
     * @return Type
     */
    public abstract Type getItemById(Identifiant id);

    /**
     * Transforme un curseur en son type
     *
     * @param cursor
     * @return Type
     */
    public abstract Type cursorToType(Cursor cursor);

    /**
     * Fusionne les listes d'etudiants, de personnels et d'autres en une seul de personnes
     *
     * @param etudiants  {@link ArrayList}
     * @param personnels {@link ArrayList}
     * @param autres     {@link ArrayList}
     * @return ArrayList
     */
    public final ArrayList<Personne> listePersonne(ArrayList<Etudiant> etudiants,
                                                   ArrayList<Personnel> personnels,
                                                   ArrayList<Autre> autres) {
        ArrayList<Personne> list = new ArrayList<>();
        list.addAll(etudiants);
        list.addAll(personnels);
        list.addAll(autres);
        return list;
    }
}
