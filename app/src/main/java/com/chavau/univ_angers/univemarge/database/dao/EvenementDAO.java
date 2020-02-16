package com.chavau.univ_angers.univemarge.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.chavau.univ_angers.univemarge.database.DBTables;
import com.chavau.univ_angers.univemarge.database.DatabaseHelper;
import com.chavau.univ_angers.univemarge.database.Identifiant;
import com.chavau.univ_angers.univemarge.database.entities.Entity;
import com.chavau.univ_angers.univemarge.database.entities.Evenement;
import com.chavau.univ_angers.univemarge.utils.Utils;

import java.util.ArrayList;

public class EvenementDAO extends DAO<Evenement> implements IMergeable {

    private static final String[] PROJECTION = {
            DBTables.Evenement.COLONNE_ID_EVENEMENT,
            DBTables.Evenement.COLONNE_DATE_DEBUT,
            DBTables.Evenement.COLONNE_DATE_FIN,
            DBTables.Evenement.COLONNE_LIEU,
            DBTables.Evenement.COLONNE_TYPE_EMARGEMENT,
            DBTables.Evenement.COLONNE_LIBELLE_EVENEMENT,
            DBTables.Evenement.COLONNE_ID_COURS,
            DBTables.Evenement.COLONNE_DELETED
    };

    public EvenementDAO(DatabaseHelper helper) {
        super(helper);
    }

    @Override
    public ContentValues getContentValues(Evenement item) {
        ContentValues values = new ContentValues();
        values.put(DBTables.Evenement.COLONNE_ID_EVENEMENT, item.getIdEvenement());
        values.put(DBTables.Evenement.COLONNE_DATE_DEBUT, Utils.convertDateToString(item.getDateDebut()));
        values.put(DBTables.Evenement.COLONNE_DATE_FIN, Utils.convertDateToString(item.getDateFin()));
        values.put(DBTables.Evenement.COLONNE_LIEU, item.getLieu());
        values.put(DBTables.Evenement.COLONNE_TYPE_EMARGEMENT, item.getTypeEmargement());
        values.put(DBTables.Evenement.COLONNE_LIBELLE_EVENEMENT, item.getLibelleEvenement());
        values.put(DBTables.Evenement.COLONNE_ID_COURS, item.getIdCours());
        values.put(DBTables.Evenement.COLONNE_DELETED, item.isDeleted());
        return values;
    }

    @Override
    public long insertItem(Evenement item) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        return db.insert(DBTables.Evenement.TABLE_NAME, null, this.getContentValues(item));
    }

    @Override
    public int updateItem(Identifiant id, Evenement item) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        return db.update(DBTables.Evenement.TABLE_NAME, this.getContentValues(item),
                DBTables.Evenement.COLONNE_ID_EVENEMENT + " = ?",
                new String[]{String.valueOf(id.getId(DBTables.Evenement.COLONNE_ID_EVENEMENT))});
    }

    @Override
    public int removeItem(Identifiant id, Evenement item) {
        item.setDeleted(true);
        return this.updateItem(id, item);
    }

    @Override
    public Evenement getItemById(Identifiant id) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        Cursor cursor = db.query(
                DBTables.Evenement.TABLE_NAME,
                PROJECTION,
                DBTables.Evenement.COLONNE_ID_EVENEMENT + " = ?",
                new String[]{String.valueOf(id.getId(DBTables.Evenement.COLONNE_ID_EVENEMENT))},
                null,
                null,
                DBTables.Evenement.COLONNE_DATE_DEBUT
        );
        return this.cursorToType(cursor);
    }

    @Override
    public Evenement cursorToType(Cursor cursor) {
        int idEvenement = cursor.getColumnIndex(DBTables.Evenement.COLONNE_ID_EVENEMENT);
        int dateDebut = cursor.getColumnIndex(DBTables.Evenement.COLONNE_DATE_DEBUT);
        int dateFin = cursor.getColumnIndex(DBTables.Evenement.COLONNE_DATE_FIN);
        int lieu = cursor.getColumnIndex(DBTables.Evenement.COLONNE_LIEU);
        int typeEmargement = cursor.getColumnIndex(DBTables.Evenement.COLONNE_TYPE_EMARGEMENT);
        int libelleEvenement = cursor.getColumnIndex(DBTables.Evenement.COLONNE_LIBELLE_EVENEMENT);
        int idCours = cursor.getColumnIndex(DBTables.Evenement.COLONNE_ID_COURS);
        int deleted = cursor.getColumnIndex(DBTables.Evenement.COLONNE_DELETED);
        return new Evenement(
                cursor.getInt(idEvenement),
                Utils.convertStringToDate(cursor.getString(dateDebut)),
                Utils.convertStringToDate(cursor.getString(dateFin)),
                cursor.getString(lieu),
                cursor.getInt(typeEmargement),
                cursor.getString(libelleEvenement),
                cursor.getInt(idCours),
                (cursor.getInt(deleted) == 1)
        );
    }

    /**
     * Retourne la liste des evenements pour 1 personnel
     *
     * @param id
     * @return ArrayList
     */
    public ArrayList<Evenement> listeEvenementsPourPersonnel(int id) {
        SQLiteDatabase db = super.helper.getWritableDatabase();

        String requete = "SELECT " +
                " e." + DBTables.Evenement.COLONNE_ID_EVENEMENT + ", " +
                " e." + DBTables.Evenement.COLONNE_DATE_DEBUT + ", " +
                " e." + DBTables.Evenement.COLONNE_DATE_FIN + ", " +
                " e." + DBTables.Evenement.COLONNE_LIEU + ", " +
                " e." + DBTables.Evenement.COLONNE_TYPE_EMARGEMENT + ", " +
                " e." + DBTables.Evenement.COLONNE_LIBELLE_EVENEMENT + ", " +
                " e." + DBTables.Evenement.COLONNE_ID_COURS + ", " +
                " e." + DBTables.Evenement.COLONNE_DELETED + " " +
                " FROM " + DBTables.Evenement.TABLE_NAME + " e " +
                " INNER JOIN " + DBTables.Responsable.TABLE_NAME + " r " +
                " ON e." + DBTables.Evenement.COLONNE_ID_EVENEMENT + " = r." + DBTables.Responsable.COLONNE_ID_EVENEMENT +
                " WHERE r." + DBTables.Responsable.COLONNE_ID_PERSONNEL_RESPONSABLE + " = ? ";

        //requete = "SELECT * From evenement"; // TODO :refaire requete

        Cursor cursor = db.rawQuery(requete, new String[]{String.valueOf(id)});
        //System.out.println("###########################requete : " +requete +String.valueOf(id));


        ArrayList<Evenement> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(this.cursorToType(cursor));
            //System.out.println("###########################cours : " +this.cursorToType(cursor).getLibelleEvenement() + " " + this.cursorToType(cursor).getDateDebut()+ " " + this.cursorToType(cursor).getTypeEmargement());
        }
        return list;
    }

    @Override
    public void merge(Entity[] entities) {
        for (Entity e : entities) {
            Evenement evenement = (Evenement) e;
            deleteItem(evenement.getIdEvenement());
            long res = insertItem(evenement);
            if (res == -1) {
                throw new SQLException("Unable to merge Evenement Table");
            }
        }
    }

    private int deleteItem(int idEvenement) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        return db.delete(DBTables.Evenement.TABLE_NAME, DBTables.Evenement.COLONNE_ID_EVENEMENT + " = ?", new String[]{String.valueOf(idEvenement)});
    }
}