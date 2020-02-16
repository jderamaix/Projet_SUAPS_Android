package com.chavau.univ_angers.univemarge.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.chavau.univ_angers.univemarge.database.DBTables;
import com.chavau.univ_angers.univemarge.database.DatabaseHelper;
import com.chavau.univ_angers.univemarge.database.Identifiant;
import com.chavau.univ_angers.univemarge.database.entities.Autre;
import com.chavau.univ_angers.univemarge.database.entities.Entity;
import com.chavau.univ_angers.univemarge.database.entities.Personnel;
import com.chavau.univ_angers.univemarge.utils.Utils;

import java.util.ArrayList;

public class PersonnelDAO extends DAO<Personnel> implements IMergeable {
    private static final String[] PROJECTION = {
            DBTables.Personnel.COLONNE_ID_PERSONNEL,
            DBTables.Personnel.COLONNE_NOM,
            DBTables.Personnel.COLONNE_PRENOM,
            DBTables.Personnel.COLONNE_LOGIN,
            DBTables.Personnel.COLONNE_EMAIL,
            DBTables.Personnel.COLONNE_PHOTO,
            DBTables.Personnel.COLONNE_NO_MIFARE,
            DBTables.Personnel.COLONNE_PIN,
            DBTables.Personnel.COLONNE_DELETED
    };

    public PersonnelDAO(DatabaseHelper helper) {
        super(helper);
    }

    @Override
    public ContentValues getContentValues(Personnel item) {
        ContentValues values = new ContentValues();
        values.put(DBTables.Personnel.COLONNE_ID_PERSONNEL, item.getIdPersonnel());
        values.put(DBTables.Personnel.COLONNE_NOM, item.getNom());
        values.put(DBTables.Personnel.COLONNE_PRENOM, item.getPrenom());
        values.put(DBTables.Personnel.COLONNE_LOGIN, item.getLogin());
        values.put(DBTables.Personnel.COLONNE_EMAIL, item.getEmail());
        values.put(DBTables.Personnel.COLONNE_PHOTO, Utils.convertBlobToString(item.getPhoto()));
        values.put(DBTables.Personnel.COLONNE_NO_MIFARE, item.getNo_mifare());
        values.put(DBTables.Personnel.COLONNE_PIN, item.getPin());
        values.put(DBTables.Personnel.COLONNE_DELETED, item.isDeleted());
        return values;

    }

    @Override
    public long insertItem(Personnel item) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        return db.insert(DBTables.Personnel.TABLE_NAME, null, this.getContentValues(item));
    }

    @Override
    public int updateItem(Identifiant id, Personnel item) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        return db.update(DBTables.Personnel.TABLE_NAME, this.getContentValues(item),
                DBTables.Personnel.COLONNE_ID_PERSONNEL + " = ?",
                new String[]{String.valueOf(id.getId(DBTables.Personnel.COLONNE_ID_PERSONNEL))});
    }

    @Override
    public int removeItem(Identifiant id, Personnel item) {
        item.setDeleted(true);
        return this.updateItem(id, item);
    }

    @Override
    public Personnel getItemById(Identifiant id) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        Cursor cursor = db.query(
                DBTables.Personnel.TABLE_NAME,
                PROJECTION,
                DBTables.Personnel.COLONNE_ID_PERSONNEL + " = ?",
                new String[]{String.valueOf(id.getId(DBTables.Personnel.COLONNE_ID_PERSONNEL))},
                null,
                null,
                DBTables.Personnel.COLONNE_NOM
        );
        return this.cursorToType(cursor);
    }

    @Override
    public Personnel cursorToType(Cursor cursor) {
        int idPersonnel = cursor.getColumnIndex(DBTables.Personnel.COLONNE_ID_PERSONNEL);
        int nom = cursor.getColumnIndex(DBTables.Personnel.COLONNE_NOM);
        int prenom = cursor.getColumnIndex(DBTables.Personnel.COLONNE_PRENOM);
        int login = cursor.getColumnIndex(DBTables.Personnel.COLONNE_LOGIN);
        int email = cursor.getColumnIndex(DBTables.Personnel.COLONNE_EMAIL);
        int photo = cursor.getColumnIndex(DBTables.Personnel.COLONNE_PHOTO);
        int no_mifare = cursor.getColumnIndex(DBTables.Personnel.COLONNE_NO_MIFARE);
        int pin = cursor.getColumnIndex(DBTables.Personnel.COLONNE_PIN);
        int deleted = cursor.getColumnIndex(DBTables.Personnel.COLONNE_DELETED);

        return new Personnel(
                cursor.getString(nom),
                cursor.getString(prenom),
                cursor.getString(email),
                cursor.getInt(idPersonnel),
                cursor.getString(login),
                Utils.convertByteToBlob(cursor.getBlob(photo)),
                cursor.getString(no_mifare),
                cursor.getString(pin),
                (cursor.getInt(deleted) == 1)
        );
    }

    /**
     * Retourne la liste du personnel inscrit à un cour
     *
     * @param id
     * @return ArrayList
     */
    public ArrayList<Personnel> listePersonnelInscritCour(Identifiant id) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " +
                        " p." + DBTables.Personnel.COLONNE_ID_PERSONNEL + ", " +
                        " p." + DBTables.Personnel.COLONNE_NOM + ", " +
                        " p." + DBTables.Personnel.COLONNE_PRENOM + ", " +
                        " p." + DBTables.Personnel.COLONNE_LOGIN + ", " +
                        " p." + DBTables.Personnel.COLONNE_EMAIL + ", " +
                        " p." + DBTables.Personnel.COLONNE_PHOTO + ", " +
                        " p." + DBTables.Personnel.COLONNE_NO_MIFARE + ", " +
                        " p." + DBTables.Personnel.COLONNE_PIN + ", " +
                        " p." + DBTables.Personnel.COLONNE_DELETED +
                        " FROM " + DBTables.Personnel.TABLE_NAME + " p " +
                        " INNER JOIN " + DBTables.Inscription.TABLE_NAME + " i " +
                        " ON p." + DBTables.Personnel.COLONNE_ID_PERSONNEL + " = i." + DBTables.Inscription.COLONNE_ID_PERSONNEL +
                        " INNER JOIN " + DBTables.Evenement.TABLE_NAME + " e " +
                        " ON e." + DBTables.Evenement.COLONNE_ID_EVENEMENT + " = i." + DBTables.Inscription.COLONNE_ID_EVENEMENT +
                        " WHERE " + DBTables.Evenement.COLONNE_ID_COURS + " = ? ",
                new String[]{String.valueOf(id.getId(DBTables.Evenement.COLONNE_ID_COURS))});

        ArrayList<Personnel> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(this.cursorToType(cursor));
        }
        return list;
    }

    /**
     * Retourne la liste des etudiants inscrit à un evenement
     *
     * @param id
     * @return
     */
    public ArrayList<Personnel> listePersonnelInscrit(Identifiant id) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " +
                        " p." + DBTables.Personnel.COLONNE_ID_PERSONNEL + ", " +
                        " p." + DBTables.Personnel.COLONNE_NOM + ", " +
                        " p." + DBTables.Personnel.COLONNE_PRENOM + ", " +
                        " p." + DBTables.Personnel.COLONNE_LOGIN + ", " +
                        " p." + DBTables.Personnel.COLONNE_EMAIL + ", " +
                        " p." + DBTables.Personnel.COLONNE_PHOTO + ", " +
                        " p." + DBTables.Personnel.COLONNE_NO_MIFARE + ", " +
                        " p." + DBTables.Personnel.COLONNE_PIN + ", " +
                        " p." + DBTables.Personnel.COLONNE_DELETED +
                        " FROM " + DBTables.Personnel.TABLE_NAME + " p " +
                        " INNER JOIN " + DBTables.Inscription.TABLE_NAME + " i " +
                        " ON p." + DBTables.Personnel.COLONNE_ID_PERSONNEL + " = i." + DBTables.Inscription.COLONNE_ID_PERSONNEL +
                        " WHERE " + DBTables.Personnel.COLONNE_ID_PERSONNEL + " = ? ",
                new String[]{String.valueOf(id.getId(DBTables.Inscription.COLONNE_ID_PERSONNEL))});

        ArrayList<Personnel> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(this.cursorToType(cursor));
        }
        return list;
    }

    public int getIdFromLogin(String login) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " +
                        DBTables.Personnel.COLONNE_ID_PERSONNEL +
                        " FROM " + DBTables.Personnel.TABLE_NAME +
                        " WHERE " + DBTables.Personnel.COLONNE_LOGIN + " = ? ",
                new String[]{login});
        System.out.println(cursor == null);
        if (cursor != null && cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(DBTables.Personnel.COLONNE_ID_PERSONNEL);
            if (index != -1) {
                try {
                    return cursor.getInt(index);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
            cursor.close();
        }
        return -1;
    }

    @Override
    public void merge(Entity[] entities) {
        for (Entity e : entities) {
            Personnel personnel = (Personnel) e;
            deleteItem(personnel.getIdPersonnel());
            long res = insertItem(personnel);
            if (res == -1) {
                throw new SQLException("Unable to merge Personnel Table");
            }
        }
    }

    public int deleteItem(int idPersonnel) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        return db.delete(DBTables.Personnel.TABLE_NAME, DBTables.Personnel.COLONNE_ID_PERSONNEL + " = ?", new String[]{String.valueOf(idPersonnel)});
    }
}