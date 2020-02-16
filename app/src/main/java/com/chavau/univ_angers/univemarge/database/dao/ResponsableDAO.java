package com.chavau.univ_angers.univemarge.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.chavau.univ_angers.univemarge.database.DBTables;
import com.chavau.univ_angers.univemarge.database.DatabaseHelper;
import com.chavau.univ_angers.univemarge.database.Identifiant;
import com.chavau.univ_angers.univemarge.database.entities.Entity;
import com.chavau.univ_angers.univemarge.database.entities.Responsable;

import java.util.ArrayList;

public class ResponsableDAO extends DAO<Responsable> implements IMergeable {
    private static final String[] PROJECTION = {
            DBTables.Responsable.COLONNE_ID_EVENEMENT,
            DBTables.Responsable.COLONNE_ID_PERSONNEL_RESPONSABLE,
            DBTables.Responsable.COLONNE_DELETED
    };

    public ResponsableDAO(DatabaseHelper helper) {
        super(helper);
    }

    @Override
    public ContentValues getContentValues(Responsable item) {
        ContentValues values = new ContentValues();
        values.put(DBTables.Responsable.COLONNE_ID_EVENEMENT, item.getIdEvenement());
        values.put(DBTables.Responsable.COLONNE_ID_PERSONNEL_RESPONSABLE, item.getIdPersonnelResponsable());
        values.put(DBTables.Responsable.COLONNE_DELETED, item.isDeleted());
        return values;
    }

    @Override
    public long insertItem(Responsable item) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        return db.insert(DBTables.Responsable.TABLE_NAME, null, this.getContentValues(item));
    }

    @Override
    public int updateItem(Identifiant id, Responsable item) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        return db.update(DBTables.Responsable.TABLE_NAME, this.getContentValues(item),
                DBTables.Responsable.COLONNE_ID_EVENEMENT + " = ? AND " +
                        DBTables.Responsable.COLONNE_ID_PERSONNEL_RESPONSABLE + " = ? ",
                new String[]{String.valueOf(id.getId(DBTables.Responsable.COLONNE_ID_EVENEMENT)),
                        String.valueOf(id.getId(DBTables.Responsable.COLONNE_ID_PERSONNEL_RESPONSABLE))});
    }

    @Override
    public int removeItem(Identifiant id, Responsable item) {
        item.setDeleted(true);
        return this.updateItem(id, item);
    }

    @Override
    public Responsable getItemById(Identifiant id) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        Cursor cursor = db.query(
                DBTables.Responsable.TABLE_NAME,
                PROJECTION,
                DBTables.Responsable.COLONNE_ID_EVENEMENT + " = ? AND " +
                        DBTables.Responsable.COLONNE_ID_PERSONNEL_RESPONSABLE + " = ? ",
                new String[]{String.valueOf(id.getId(DBTables.Responsable.COLONNE_ID_EVENEMENT)),
                        String.valueOf(id.getId(DBTables.Responsable.COLONNE_ID_PERSONNEL_RESPONSABLE))},
                null,
                null,
                null
        );
        return this.cursorToType(cursor);
    }

    @Override
    public Responsable cursorToType(Cursor cursor) {
        int idEvenement = cursor.getColumnIndex(DBTables.Responsable.COLONNE_ID_EVENEMENT);
        int idPersonnelResponsable = cursor.getColumnIndex(DBTables.Responsable.COLONNE_ID_PERSONNEL_RESPONSABLE);
        int deleted = cursor.getColumnIndex(DBTables.Responsable.COLONNE_DELETED);

        return new Responsable(
                cursor.getInt(idEvenement),
                cursor.getInt(idPersonnelResponsable),
                (cursor.getInt(deleted) == 1)
        );
    }

    @Override
    public void merge(Entity[] entities) {
        for(Entity e : entities) {
            Responsable responsable = (Responsable) e;
            deleteItem(responsable.getIdPersonnelResponsable(), responsable.getIdEvenement());
            long res = insertItem(responsable);
            if (res == -1) {
                throw new SQLException("Unable to merge Responsable Table");
            }
        }
    }

    private int deleteItem(int idPersonnelResponsable, int idEvenement) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        return db.delete(DBTables.Responsable.TABLE_NAME, DBTables.Responsable.COLONNE_ID_PERSONNEL_RESPONSABLE + " = ? AND " +DBTables.Responsable.COLONNE_ID_EVENEMENT + " = ? " , new String[]{String.valueOf(idPersonnelResponsable), String.valueOf(idEvenement)});
    }
/*
    public void getAll(int id) {

        SQLiteDatabase db = super.helper.getWritableDatabase();


        String requete = "SELECT * From responsable"; // TODO :refaire requete

        Cursor cursor = db.rawQuery(requete, new String[]{});
        //System.out.println("###########################requete : " +requete +String.valueOf(id));


        while (cursor.moveToNext()) {
            //System.out.println("###########################event : " +this.cursorToType(cursor).getIdEvenement() + " resp :  " + this.cursorToType(cursor).getIdPersonnelResponsable());
        }

    }
    */
}