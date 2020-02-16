package com.chavau.univ_angers.univemarge.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.chavau.univ_angers.univemarge.database.DBTables;
import com.chavau.univ_angers.univemarge.database.DatabaseHelper;
import com.chavau.univ_angers.univemarge.database.Identifiant;
import com.chavau.univ_angers.univemarge.database.entities.Entity;
import com.chavau.univ_angers.univemarge.database.entities.PresenceRoulant;
import com.chavau.univ_angers.univemarge.database.entities.StatutPresence;
import com.chavau.univ_angers.univemarge.utils.Utils;

public class PresenceRoulantDAO extends DAO<PresenceRoulant> implements IMergeable {
    private static final String[] PROJECTION = {
            DBTables.PresenceRoulant.COLONNE_ID_ROULANT,
            DBTables.PresenceRoulant.COLONNE_ID_EVENEMENT,
            DBTables.PresenceRoulant.COLONNE_NUMERO_ETUDIANT,
            DBTables.PresenceRoulant.COLONNE_TEMPS,
            DBTables.PresenceRoulant.COLONNE_DATE_ENTREE,
            DBTables.PresenceRoulant.COLONNE_DATE_SORTIE,
            DBTables.PresenceRoulant.COLONNE_ID_PERSONNEL,
            DBTables.PresenceRoulant.COLONNE_ID_AUTRE
    };

    public PresenceRoulantDAO(DatabaseHelper helper) {
        super(helper);
    }

    @Override
    public ContentValues getContentValues(PresenceRoulant item) {
        ContentValues values = new ContentValues();
//        values.put(DBTables.PresenceRoulant.COLONNE_ID_ROULANT, item.getIdRoulant());
        values.put(DBTables.PresenceRoulant.COLONNE_ID_EVENEMENT, item.getIdEvenement());
        values.put(DBTables.PresenceRoulant.COLONNE_NUMERO_ETUDIANT, item.getNumeroEtudiant());
        values.put(DBTables.PresenceRoulant.COLONNE_TEMPS, Utils.convertDateToString(item.getTemps()));
        values.put(DBTables.PresenceRoulant.COLONNE_DATE_ENTREE, Utils.convertDateToString(item.getDateEntree()));
        values.put(DBTables.PresenceRoulant.COLONNE_DATE_SORTIE, Utils.convertDateToString(item.getDateSortie()));
        values.put(DBTables.PresenceRoulant.COLONNE_ID_PERSONNEL, item.getIdPersonnel());
        values.put(DBTables.PresenceRoulant.COLONNE_ID_AUTRE, item.getIdAutre());
        return values;
    }

    @Override
    public long insertItem(PresenceRoulant item) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        return db.insert(DBTables.PresenceRoulant.TABLE_NAME, null, this.getContentValues(item));
    }

    @Override
    public int updateItem(Identifiant id, PresenceRoulant item) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        return db.update(DBTables.PresenceRoulant.TABLE_NAME, this.getContentValues(item),
                DBTables.PresenceRoulant.COLONNE_ID_ROULANT + " = ?",
                new String[]{String.valueOf(id.getId(DBTables.PresenceRoulant.COLONNE_ID_ROULANT))});
    }

    @Override
    public int removeItem(Identifiant id, PresenceRoulant item) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        return db.delete(DBTables.PresenceRoulant.TABLE_NAME,
                DBTables.PresenceRoulant.COLONNE_ID_ROULANT + " = ?",
                new String[]{String.valueOf(id.getId(DBTables.PresenceRoulant.COLONNE_ID_ROULANT))});
    }

    @Override
    public PresenceRoulant getItemById(Identifiant id) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        Cursor cursor = db.query(
                DBTables.PresenceRoulant.TABLE_NAME,
                PROJECTION,
                DBTables.PresenceRoulant.COLONNE_ID_ROULANT + " = ?",
                new String[]{String.valueOf(id.getId(DBTables.PresenceRoulant.COLONNE_ID_ROULANT))},
                null,
                null,
                DBTables.PresenceRoulant.COLONNE_DATE_ENTREE
        );
        return this.cursorToType(cursor);
    }

    @Override
    public PresenceRoulant cursorToType(Cursor cursor) {
        int idRoulant = cursor.getColumnIndex(DBTables.PresenceRoulant.COLONNE_ID_ROULANT);
        int idEvenement = cursor.getColumnIndex(DBTables.PresenceRoulant.COLONNE_ID_EVENEMENT);
        int numeroEtudiant = cursor.getColumnIndex(DBTables.PresenceRoulant.COLONNE_NUMERO_ETUDIANT);
        int temps = cursor.getColumnIndex(DBTables.PresenceRoulant.COLONNE_TEMPS);
        int dateEntree = cursor.getColumnIndex(DBTables.PresenceRoulant.COLONNE_DATE_ENTREE);
        int dateSortie = cursor.getColumnIndex(DBTables.PresenceRoulant.COLONNE_DATE_SORTIE);
        int idPersonnel = cursor.getColumnIndex(DBTables.PresenceRoulant.COLONNE_ID_PERSONNEL);
        int idAutre = cursor.getColumnIndex(DBTables.PresenceRoulant.COLONNE_ID_AUTRE);

        return new PresenceRoulant(
                cursor.getInt(idRoulant),
                cursor.getInt(idEvenement),
                cursor.getInt(numeroEtudiant),
                Utils.convertStringToDate(cursor.getString(temps)),
                Utils.convertStringToDate(cursor.getString(dateEntree)),
                Utils.convertStringToDate(cursor.getString(dateSortie)),
                cursor.getInt(idPersonnel),
                cursor.getInt(idAutre)
        );
    }

    /**
     * retourne si la personne est déjà entré dans cette séance. On en déduit qu'il sort.
     * @return
     */
    public boolean estDejaEntre() {
        // TODO : faire le contenu
        return false;
    }

    /**
     * ajoute l'entrée d'une personne dans le cours
     * @return
     */
    private boolean addEntree() {
        // TODO : faire le contenu
        return false;
    }

    /**
     * ajoute la sortir d'une personne du cours
     * @return
     */
    private boolean addSortie() {
        // TODO : faire le contenu
        return false;
    }

    /*public boolean setPresenceRoulantMifare(String mifare, int idEvenement) {

        SQLiteDatabase db = super.helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " +
                        " p." + DBTables.Personnel.COLONNE_ID_PERSONNEL + ", " +
                        " e." + DBTables.Etudiant.COLONNE_NUMERO_ETUDIANT +
                        " AS countPersonne FROM " + DBTables.Inscription.TABLE_NAME + " i " +
                        " INNER JOIN " + DBTables.Personnel.TABLE_NAME + " p " +
                        " ON p." + DBTables.Personnel.COLONNE_ID_PERSONNEL + " = i." + DBTables.Inscription.COLONNE_ID_PERSONNEL +
                        " INNER JOIN " + DBTables.Etudiant.TABLE_NAME + " e " +
                        " ON e." + DBTables.Etudiant.COLONNE_NUMERO_ETUDIANT + " = i." + DBTables.Inscription.COLONNE_NUMERO_ETUDIANT +
                        " WHERE " + DBTables.Inscription.COLONNE_ID_EVENEMENT + " = ? " +
                        " AND ( " + DBTables.Etudiant.COLONNE_NO_MIFARE + " = ?" +
                        " OR " + DBTables.Personnel.COLONNE_NO_MIFARE + " = ? )",
                new String[]{String.valueOf(idEvenement), mifare, mifare});
        cursor.moveToNext();
        if (cursor.getColumnCount() == 0)
            return false;

        int indexIdPersonnel = cursor.getColumnIndex(DBTables.Personnel.COLONNE_ID_PERSONNEL);
        int indexNumEtudiant = cursor.getColumnIndex(DBTables.Etudiant.COLONNE_NUMERO_ETUDIANT);

        PresenceDAO p = new PresenceDAO(super.helper);

        int idPersonnel = (!cursor.isNull(indexIdPersonnel)) ? cursor.getInt(indexIdPersonnel) : -1;
        int numEtudiant = (!cursor.isNull(indexNumEtudiant)) ? cursor.getInt(indexNumEtudiant) : -1;


        PresenceRoulant item = new PresenceRoulant(
                idEvenement,
                numEtudiant,
                sp,
                false,
                idPersonnel,
                -1
        );
        db.insert(DBTables.Presence.TABLE_NAME, null, this.getContentValues(item));
        return true;
    }*/

    @Override
    public void merge(Entity[] entities) {
        for (Entity e : entities) {
            PresenceRoulant presenceRoulant = (PresenceRoulant) e;
            deleteItem(presenceRoulant.getIdRoulant());
            long res = insertItem(presenceRoulant);
            if (res == -1) {
                throw new SQLException("Unable to merge PresenceRoulant Table");
            }
        }
    }

    private int deleteItem(int idRoulant) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        return db.delete(DBTables.PresenceRoulant.TABLE_NAME, DBTables.PresenceRoulant.COLONNE_ID_ROULANT + " = ?", new String[]{String.valueOf(idRoulant)});
    }
}