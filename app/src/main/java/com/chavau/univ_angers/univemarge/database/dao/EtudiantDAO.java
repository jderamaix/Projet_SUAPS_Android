package com.chavau.univ_angers.univemarge.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.chavau.univ_angers.univemarge.database.DBTables;
import com.chavau.univ_angers.univemarge.database.DatabaseHelper;
import com.chavau.univ_angers.univemarge.database.Identifiant;
import com.chavau.univ_angers.univemarge.database.entities.Entity;
import com.chavau.univ_angers.univemarge.database.entities.Etudiant;
import com.chavau.univ_angers.univemarge.utils.Utils;

import java.util.ArrayList;

public class EtudiantDAO extends DAO<Etudiant> implements IMergeable {

    private static final String[] PROJECTION = {
            DBTables.Etudiant.COLONNE_NUMERO_ETUDIANT,
            DBTables.Etudiant.COLONNE_ID_ETUDIANT,
            DBTables.Etudiant.COLONNE_NOM,
            DBTables.Etudiant.COLONNE_PRENOM,
            DBTables.Etudiant.COLONNE_NO_MIFARE,
            DBTables.Etudiant.COLONNE_EMAIL,
            DBTables.Etudiant.COLONNE_PHOTO,
            DBTables.Etudiant.COLONNE_DELETED
    };

    public EtudiantDAO(DatabaseHelper helper) {
        super(helper);
    }

    @Override
    public ContentValues getContentValues(Etudiant item) {
        ContentValues values = new ContentValues();
        values.put(DBTables.Etudiant.COLONNE_NUMERO_ETUDIANT, item.getNumeroEtudiant());
        values.put(DBTables.Etudiant.COLONNE_ID_ETUDIANT, item.getIdEtudiant());
        values.put(DBTables.Etudiant.COLONNE_NOM, item.getNom());
        values.put(DBTables.Etudiant.COLONNE_PRENOM, item.getPrenom());
        values.put(DBTables.Etudiant.COLONNE_NO_MIFARE, item.getNo_mifare());
        values.put(DBTables.Etudiant.COLONNE_EMAIL, item.getEmail());
        values.put(DBTables.Etudiant.COLONNE_PHOTO, Utils.convertBlobToString(item.getPhoto()));
        values.put(DBTables.Etudiant.COLONNE_DELETED, item.isDeleted());
        return values;
    }

    @Override
    public long insertItem(Etudiant item) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        return db.insert(DBTables.Etudiant.TABLE_NAME, null, this.getContentValues(item));
    }

    @Override
    public int updateItem(Identifiant id, Etudiant item) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        return db.update(DBTables.Etudiant.TABLE_NAME, this.getContentValues(item),
                DBTables.Etudiant.COLONNE_NUMERO_ETUDIANT + " = ?",
                new String[]{String.valueOf(id.getId(DBTables.Etudiant.COLONNE_NUMERO_ETUDIANT))});
    }

    @Override
    public int removeItem(Identifiant id, Etudiant item) {
        item.setDeleted(true);
        return this.updateItem(id, item);
    }

    @Override
    public Etudiant getItemById(Identifiant id) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        Cursor cursor = db.query(
                DBTables.Etudiant.TABLE_NAME,
                PROJECTION,
                DBTables.Etudiant.COLONNE_NUMERO_ETUDIANT + " = ?",
                new String[]{String.valueOf(id.getId(DBTables.Etudiant.COLONNE_NUMERO_ETUDIANT))},
                null,
                null,
                DBTables.Etudiant.COLONNE_NOM
        );
        return this.cursorToType(cursor);
    }

    @Override
    public Etudiant cursorToType(Cursor cursor) {
        int numeroEtudiant = cursor.getColumnIndex(DBTables.Etudiant.COLONNE_NUMERO_ETUDIANT);
        int idEtudiant = cursor.getColumnIndex(DBTables.Etudiant.COLONNE_ID_ETUDIANT);
        int nom = cursor.getColumnIndex(DBTables.Etudiant.COLONNE_NOM);
        int prenom = cursor.getColumnIndex(DBTables.Etudiant.COLONNE_PRENOM);
        int no_mifare = cursor.getColumnIndex(DBTables.Etudiant.COLONNE_NO_MIFARE);
        int email = cursor.getColumnIndex(DBTables.Etudiant.COLONNE_EMAIL);
        int photo = cursor.getColumnIndex(DBTables.Etudiant.COLONNE_EMAIL);
        int deleted = cursor.getColumnIndex(DBTables.Etudiant.COLONNE_DELETED);

        return new Etudiant(
                cursor.getString(nom),
                cursor.getString(prenom),
                cursor.getString(email),
                cursor.getInt(numeroEtudiant),
                cursor.getInt(idEtudiant),
                cursor.getString(no_mifare),
                Utils.convertByteToBlob(cursor.getBlob(photo)),
                (cursor.getInt(deleted) == 1)
        );
    }

    /**
     * Retourne la liste des etudiants inscrit à un cour
     *
     * @param id
     * @return ArrayList
     */
    public ArrayList<Etudiant> listeEtudiantInscritCours(int id) {
        SQLiteDatabase db = super.helper.getWritableDatabase();

        String requete ="SELECT " +
                " e." + DBTables.Etudiant.COLONNE_NUMERO_ETUDIANT + " , " +
                " e." + DBTables.Etudiant.COLONNE_NOM + " , " +
                " e." + DBTables.Etudiant.COLONNE_PRENOM + " , " +
                " e." + DBTables.Etudiant.COLONNE_NO_MIFARE + " , " +
                " e." + DBTables.Etudiant.COLONNE_EMAIL + " , " +
                " e." + DBTables.Etudiant.COLONNE_PHOTO + " , " +
                " e." + DBTables.Etudiant.COLONNE_DELETED + " " +
                "FROM " + DBTables.Autre.TABLE_NAME + " a " +
                " INNER JOIN " + DBTables.Inscription.TABLE_NAME + " i " +
                " ON a." + DBTables.Autre.COLONNE_ID_AUTRE + " = i." + DBTables.Inscription.COLONNE_ID_AUTRE +
                " INNER JOIN " + DBTables.Evenement.TABLE_NAME + " e " +
                " ON e." + DBTables.Evenement.COLONNE_ID_EVENEMENT + " = i." + DBTables.Inscription.COLONNE_ID_EVENEMENT +
                " WHERE " + DBTables.Evenement.COLONNE_ID_COURS + " = ? " ;

        Cursor cursor = db.rawQuery(requete, new String[]{String.valueOf(id)});

        ArrayList<Etudiant> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(this.cursorToType(cursor));
        }
        return list;
    }

    /**
     * Retourne la liste des etudiants inscrit à un evenement
     *
     * @param id
     * @return ArrayList
     */
    public ArrayList<Etudiant> listeEtudiantInscrit(int id) {
        SQLiteDatabase db = super.helper.getWritableDatabase();

        String requete = "SELECT " +
                " e." + DBTables.Etudiant.COLONNE_NUMERO_ETUDIANT + " , " +
                " e." + DBTables.Etudiant.COLONNE_ID_ETUDIANT + " , " +
                " e." + DBTables.Etudiant.COLONNE_NOM + " , " +
                " e." + DBTables.Etudiant.COLONNE_PRENOM + " , " +
                " e." + DBTables.Etudiant.COLONNE_NO_MIFARE + " , " +
                " e." + DBTables.Etudiant.COLONNE_EMAIL + " , " +
                " e." + DBTables.Etudiant.COLONNE_PHOTO + " , " +
                " e." + DBTables.Etudiant.COLONNE_DELETED + " " +
                " FROM " + DBTables.Etudiant.TABLE_NAME + " e " +
                " INNER JOIN " + DBTables.Inscription.TABLE_NAME + " i " +
                " ON e." + DBTables.Etudiant.COLONNE_ID_ETUDIANT + " = i." + DBTables.Inscription.COLONNE_NUMERO_ETUDIANT +
                " WHERE " + DBTables.Inscription.COLONNE_ID_EVENEMENT + " = ? " ;

        //requete = "select * from etudiant";
        Cursor cursor = db.rawQuery(requete, new String[]{String.valueOf(id)});
        //Cursor cursor = db.rawQuery(requete, new String[]{});


        ArrayList<Etudiant> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(this.cursorToType(cursor));
            System.out.println(" etudiant : " + cursorToType(cursor).getNom());
        }
        return list;
    }

    @Override
    public void merge(Entity[] entities) {
        for (Entity e : entities) {
            Etudiant etudiant = (Etudiant) e;
            deleteItem(etudiant.getNumeroEtudiant());
            long res = insertItem(etudiant);
            if (res == -1) {
                throw new SQLException("Unable to merge Etudiant Table");
            }
        }
    }

    public int deleteItem(int numeroEtudiant) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        return db.delete(DBTables.Etudiant.TABLE_NAME, DBTables.Etudiant.COLONNE_NUMERO_ETUDIANT + " = ?", new String[]{String.valueOf(numeroEtudiant)});
    }
}