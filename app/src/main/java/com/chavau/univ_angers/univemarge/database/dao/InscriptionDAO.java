package com.chavau.univ_angers.univemarge.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.chavau.univ_angers.univemarge.database.DBTables;
import com.chavau.univ_angers.univemarge.database.DatabaseHelper;
import com.chavau.univ_angers.univemarge.database.Identifiant;
import com.chavau.univ_angers.univemarge.database.entities.Entity;
import com.chavau.univ_angers.univemarge.database.entities.Inscription;

import java.sql.ResultSet;

public class InscriptionDAO extends DAO<Inscription> implements IMergeable {
    private static final String[] PROJECTION = {
            DBTables.Inscription.COLONNE_ID_PERSONNEL,
            DBTables.Inscription.COLONNE_ID_INSCRIPTION,
            DBTables.Inscription.COLONNE_ID_EVENEMENT,
            DBTables.Inscription.COLONNE_NUMERO_ETUDIANT,
            DBTables.Inscription.COLONNE_DELETED,
            DBTables.Inscription.COLONNE_TYPE_INSCRIPTION,
            DBTables.Inscription.COLONNE_ID_AUTRE
    };

    public InscriptionDAO(DatabaseHelper helper) {
        super(helper);
    }

    @Override
    public ContentValues getContentValues(Inscription item) {
        ContentValues values = new ContentValues();
        values.put(DBTables.Inscription.COLONNE_ID_PERSONNEL, item.getIdPersonnel());
        values.put(DBTables.Inscription.COLONNE_ID_INSCRIPTION, item.getIdInscription());
        values.put(DBTables.Inscription.COLONNE_ID_EVENEMENT, item.getIdEvenement());
        values.put(DBTables.Inscription.COLONNE_NUMERO_ETUDIANT, item.getNumeroEtudiant());
        values.put(DBTables.Inscription.COLONNE_DELETED, item.isDeleted());
        values.put(DBTables.Inscription.COLONNE_TYPE_INSCRIPTION, item.getTypeInscription());
        values.put(DBTables.Inscription.COLONNE_ID_AUTRE, item.getIdAutre());
        return values;
    }

    @Override
    public long insertItem(Inscription item) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        return db.insert(DBTables.Inscription.TABLE_NAME, null, this.getContentValues(item));
    }

    @Override
    public int updateItem(Identifiant id, Inscription item) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        return db.update(DBTables.Inscription.TABLE_NAME, this.getContentValues(item),
                DBTables.Inscription.COLONNE_ID_INSCRIPTION + " = ?",
                new String[]{String.valueOf(id.getId(DBTables.Inscription.COLONNE_ID_INSCRIPTION))});
    }

    @Override
    public int removeItem(Identifiant id, Inscription item) {
        item.setDeleted(true);
        return this.updateItem(id, item);
    }

    @Override
    public Inscription getItemById(Identifiant id) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        Cursor cursor = db.query(
                DBTables.Inscription.TABLE_NAME,
                PROJECTION,
                DBTables.Inscription.COLONNE_ID_INSCRIPTION + " = ?",
                new String[]{String.valueOf(id.getId(DBTables.Inscription.COLONNE_ID_INSCRIPTION))},
                null,
                null,
                null
        );
        return this.cursorToType(cursor);
    }

    @Override
    public Inscription cursorToType(Cursor cursor) {
        int idPersonnel = cursor.getColumnIndex(DBTables.Inscription.COLONNE_ID_PERSONNEL);
        int idInscription = cursor.getColumnIndex(DBTables.Inscription.COLONNE_ID_INSCRIPTION);
        int idEvenement = cursor.getColumnIndex(DBTables.Inscription.COLONNE_ID_EVENEMENT);
        int numeroEtudiant = cursor.getColumnIndex(DBTables.Inscription.COLONNE_NUMERO_ETUDIANT);
        int deleted = cursor.getColumnIndex(DBTables.Inscription.COLONNE_DELETED);
        int typeInscription = cursor.getColumnIndex(DBTables.Inscription.COLONNE_TYPE_INSCRIPTION);
        int idAutre = cursor.getColumnIndex(DBTables.Inscription.COLONNE_ID_AUTRE);

        return new Inscription(
                cursor.getInt(idPersonnel),
                cursor.getInt(idInscription),
                cursor.getInt(idEvenement),
                cursor.getInt(numeroEtudiant),
                (cursor.getInt(deleted) == 1),
                cursor.getString(typeInscription),
                cursor.getInt(idAutre)
        );
    }

    @Override
    public void merge(Entity[] entities) {
        for (Entity e : entities) {
            Inscription inscription = (Inscription) e;
            deleteItem(inscription.getIdInscription());
            long res = insertItem(inscription);
            if (res == -1) {
                throw new SQLException("Unable to merge Inscription Table");
            }
        }
    }

    private int deleteItem(int idInscription) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        return db.delete(DBTables.Inscription.TABLE_NAME, DBTables.Inscription.COLONNE_ID_INSCRIPTION + " = ?", new String[]{String.valueOf(idInscription)});
    }

    /**
     * Cette méthode prend un mifare et modifie la table associée à celui-ci (étudiant ou personnel)
     * et nous donne la colonne ainsi que la valeur associé à l'identifiant de la personne
     * @param mifare
     * @param table
     * @param column
     * @param idPersonne
     */
    public void fromMiFareGetTableAndColumn(String mifare, String table, String column, int idPersonne) {

        // cas pour un étudiant
        SQLiteDatabase db = super.helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + DBTables.Etudiant.COLONNE_NUMERO_ETUDIANT + " FROM " + DBTables.Etudiant.TABLE_NAME +
                    " WHERE " + DBTables.Etudiant.COLONNE_NO_MIFARE + " = ?",
                new String[]{mifare});

        // si le nombre d'entrées est supérieur à 0,
        // ce mifare correspond donc à un étudiant
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            table = DBTables.Etudiant.TABLE_NAME;
            column = DBTables.Etudiant.COLONNE_NUMERO_ETUDIANT;
            idPersonne = cursor.getInt(cursor.getColumnIndex(DBTables.Etudiant.COLONNE_NUMERO_ETUDIANT));
            return;
        }

        // cas pour un personnel
        cursor = db.rawQuery(
                "SELECT " + DBTables.Personnel.COLONNE_ID_PERSONNEL + " FROM " + DBTables.Personnel.TABLE_NAME +
                    " WHERE " + DBTables.Personnel.COLONNE_NO_MIFARE + " = ?",
                new String[]{mifare});

        // si le nombre d'entrées est supérieur à 0,
        // ce mifare correspond donc à un personnel
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            table = DBTables.Personnel.TABLE_NAME;
            column = DBTables.Personnel.COLONNE_ID_PERSONNEL;
            idPersonne = cursor.getInt(cursor.getColumnIndex(DBTables.Personnel.COLONNE_ID_PERSONNEL));
            return;
        }

        throw new IllegalArgumentException("Unknown mifare");
    }

    /**
     * retourne un booléen décrivant si la personne est inscrite au cours
     * @param idPersonne valeur associée à l'identifiant de la personne (soit un num étudiant, soit is personnel)
     * @param columnNameForId Le nom de la colonne associé à la valeur (soit numeroEtudiant soit idPersonnel)
     * @return si la personne est bien inscrite au cours
     */
    public boolean estInscrit(int idEvenement, int idPersonne, String columnNameForId) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DBTables.Inscription.TABLE_NAME +
                    " WHERE " + columnNameForId + " = ?" +
                    " AND " + DBTables.Inscription.COLONNE_ID_EVENEMENT + " = ?",
                new String[]{Integer.toString(idPersonne), Integer.toString(idEvenement)});

        // si le nombre d'entrées est supérieur à 0,
        // la personne est bel et bien inscrite
        return cursor.getCount() > 0;
    }
}