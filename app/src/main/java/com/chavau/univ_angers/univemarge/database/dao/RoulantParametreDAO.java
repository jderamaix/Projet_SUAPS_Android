package com.chavau.univ_angers.univemarge.database.dao;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.chavau.univ_angers.univemarge.database.DBTables;
import com.chavau.univ_angers.univemarge.database.DatabaseHelper;
import com.chavau.univ_angers.univemarge.database.Identifiant;
import com.chavau.univ_angers.univemarge.database.entities.Entity;
import com.chavau.univ_angers.univemarge.database.entities.RoulantParametre;
import com.chavau.univ_angers.univemarge.utils.Utils;

import java.util.Date;

public class RoulantParametreDAO extends DAO<RoulantParametre> implements IMergeable {
    private static final String[] PROJECTION = {
            DBTables.RoulantParametre.COLONNE_ID_COUR,
            DBTables.RoulantParametre.COLONNE_TEMPS_SEANCE,
            DBTables.RoulantParametre.COLONNE_MAX_PERSONNES
    };

    public RoulantParametreDAO(DatabaseHelper helper) {
        super(helper);
    }

    @Override
    public ContentValues getContentValues(RoulantParametre item) {
        ContentValues values = new ContentValues();
        values.put(DBTables.RoulantParametre.COLONNE_ID_COUR, item.getIdCours());
        values.put(DBTables.RoulantParametre.COLONNE_TEMPS_SEANCE, Utils.convertDateToString(item.getTempsSeance()));
        values.put(DBTables.RoulantParametre.COLONNE_MAX_PERSONNES, item.getMaxPersonnes());
        return values;
    }

    @Override
    public long insertItem(RoulantParametre item) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        return db.insert(DBTables.RoulantParametre.TABLE_NAME, null, this.getContentValues(item));
    }

    @Override
    public int updateItem(Identifiant id, RoulantParametre item) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        return db.update(DBTables.RoulantParametre.TABLE_NAME, this.getContentValues(item),
                DBTables.RoulantParametre.COLONNE_ID_COUR + " = ?",
                new String[]{String.valueOf(id.getId(DBTables.RoulantParametre.COLONNE_ID_COUR))});
    }

    @Override
    public int removeItem(Identifiant id, RoulantParametre item) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        return db.delete(DBTables.RoulantParametre.TABLE_NAME,
                DBTables.RoulantParametre.COLONNE_ID_COUR + " = ?",
                new String[]{String.valueOf(id.getId(DBTables.RoulantParametre.COLONNE_ID_COUR))});
    }

    @Override
    public RoulantParametre getItemById(Identifiant id) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        Cursor cursor = db.query(
                DBTables.RoulantParametre.TABLE_NAME,
                PROJECTION,
                DBTables.RoulantParametre.COLONNE_ID_COUR + " = ?",
                new String[]{String.valueOf(id.getId(DBTables.RoulantParametre.COLONNE_ID_COUR))},
                null,
                null,
                DBTables.RoulantParametre.COLONNE_ID_COUR
        );
        return this.cursorToType(cursor);
    }

    @Override
    public RoulantParametre cursorToType(Cursor cursor) {
        int idCours = cursor.getColumnIndex(DBTables.RoulantParametre.COLONNE_ID_COUR);
        int tempsSeance = cursor.getColumnIndex(DBTables.RoulantParametre.COLONNE_TEMPS_SEANCE);
        int maxPersonnes = cursor.getColumnIndex(DBTables.RoulantParametre.COLONNE_MAX_PERSONNES);

        return new RoulantParametre(
                cursor.getInt(idCours),
                Utils.convertStringToDate(cursor.getString(tempsSeance)),
                cursor.getInt(maxPersonnes)
        );
    }

    @Override
    public void merge(Entity[] entities) {
        for (Entity e : entities) {
            RoulantParametre roulantParametre = (RoulantParametre) e;
            deleteItem(roulantParametre.getIdCours(), roulantParametre.getTempsSeance(), roulantParametre.getMaxPersonnes());
            long res = insertItem(roulantParametre);
            if (res == -1) {
                throw new SQLException("Unable to merge RoulantParametre Table");
            }
        }
    }

    private int deleteItem(int idCours, Date tempsSeance, int maxPersonnes) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        return db.delete(DBTables.RoulantParametre.TABLE_NAME,
                DBTables.RoulantParametre.COLONNE_ID_COUR + " = ? AND " +
                        DBTables.RoulantParametre.COLONNE_TEMPS_SEANCE + " = ? AND " +
                        DBTables.RoulantParametre.COLONNE_MAX_PERSONNES + " = ?",
                new String[]{String.valueOf(idCours), String.valueOf(tempsSeance), String.valueOf(maxPersonnes)});
    }
}