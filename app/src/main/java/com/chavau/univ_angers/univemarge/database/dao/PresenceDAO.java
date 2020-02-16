package com.chavau.univ_angers.univemarge.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.chavau.univ_angers.univemarge.database.DBTables;
import com.chavau.univ_angers.univemarge.database.DatabaseHelper;
import com.chavau.univ_angers.univemarge.database.Identifiant;
import com.chavau.univ_angers.univemarge.database.entities.Entity;
import com.chavau.univ_angers.univemarge.database.entities.Presence;
import com.chavau.univ_angers.univemarge.database.entities.StatutPresence;
import com.chavau.univ_angers.univemarge.utils.Utils;

public class PresenceDAO extends DAO<Presence> implements IMergeable {
    private static final String[] PROJECTION = {
            DBTables.Presence.COLONNE_ID_PRESENCE,
            DBTables.Presence.COLONNE_ID_EVENEMENT,
            DBTables.Presence.COLONNE_STATUT_PRESENCE,
            DBTables.Presence.COLONNE_DATE_MAJ,
            DBTables.Presence.COLONNE_DELETED,
            DBTables.Presence.COLONNE_NUMERO_ETUDIANT,
            DBTables.Presence.COLONNE_ID_PERSONNEL,
            DBTables.Presence.COLONNE_ID_AUTRE
    };

    public PresenceDAO(DatabaseHelper helper) {
        super(helper);
    }

    @Override
    public ContentValues getContentValues(Presence item) {
        ContentValues values = new ContentValues();
//        values.put(DBTables.Presence.COLONNE_ID_PRESENCE, item.getIdPresence());
        values.put(DBTables.Presence.COLONNE_ID_EVENEMENT, item.getIdEvenement());
        values.put(DBTables.Presence.COLONNE_STATUT_PRESENCE, item.getStatutPresence().getValue());
        values.put(DBTables.Presence.COLONNE_DATE_MAJ, Utils.convertDateToString(item.getDateMaj()));
        values.put(DBTables.Presence.COLONNE_DELETED, item.isDeleted());
        values.put(DBTables.Presence.COLONNE_NUMERO_ETUDIANT, item.getNumeroEtudiant());
        values.put(DBTables.Presence.COLONNE_ID_PERSONNEL, item.getIdPersonnel());
        values.put(DBTables.Presence.COLONNE_ID_AUTRE, item.getIdAutre());
        return values;
    }

    @Override
    public long insertItem(Presence item) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        return db.insert(DBTables.Presence.TABLE_NAME, null, this.getContentValues(item));
    }

    @Override
    public int updateItem(Identifiant id, Presence item) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        return db.update(DBTables.Presence.TABLE_NAME, this.getContentValues(item),
                DBTables.Presence.COLONNE_ID_PRESENCE + " = ?",
                new String[]{String.valueOf(id.getId(DBTables.Presence.COLONNE_ID_PRESENCE))});
    }

    @Override
    public int removeItem(Identifiant id, Presence item) {
        item.setDeleted(true);
        return this.updateItem(id, item);
    }

    @Override
    public Presence getItemById(Identifiant id) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        Cursor cursor = db.query(
                DBTables.Presence.TABLE_NAME,
                PROJECTION,
                DBTables.Presence.COLONNE_ID_PRESENCE + " = ?",
                new String[]{String.valueOf(id.getId(DBTables.Presence.COLONNE_ID_PRESENCE))},
                null,
                null,
                DBTables.Presence.COLONNE_STATUT_PRESENCE
        );
        return this.cursorToType(cursor);
    }

    @Override
    public Presence cursorToType(Cursor cursor) {
        int idPresence = cursor.getColumnIndex(DBTables.Presence.COLONNE_ID_PRESENCE);
        int idEvenement = cursor.getColumnIndex(DBTables.Presence.COLONNE_ID_EVENEMENT);
        int numeroEtudiant = cursor.getColumnIndex(DBTables.Presence.COLONNE_NUMERO_ETUDIANT);
        int statutPresence = cursor.getColumnIndex(DBTables.Presence.COLONNE_STATUT_PRESENCE);
        int dateMaj = cursor.getColumnIndex(DBTables.Presence.COLONNE_DATE_MAJ);
        int deleted = cursor.getColumnIndex(DBTables.Presence.COLONNE_DELETED);
        int idPersonnel = cursor.getColumnIndex(DBTables.Presence.COLONNE_ID_PERSONNEL);
        int idAutre = cursor.getColumnIndex(DBTables.Presence.COLONNE_ID_AUTRE);

        return new Presence(
                cursor.getInt(idPresence),
                cursor.getInt(idEvenement),
                cursor.getInt(numeroEtudiant),
                StatutPresence.fromInt(cursor.getInt(statutPresence)),
                (cursor.getInt(deleted) == 1),
                cursor.getInt(idPersonnel),
                cursor.getInt(idAutre),
                Utils.convertStringToDate(cursor.getString(dateMaj))
        );
    }

    /**
     * insère la nouvelle présence dans la table si la personne n'a pas encore badgé.
     * sinon effectue un update.
     * @param mifare
     * @param idEvenement
     * @param sp
     * @return
     */
    public boolean insererPresence(String mifare, int idEvenement, StatutPresence sp) {
        // TODO : faire le contenu
        return false;
        /*
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

        int idPersonnel = (!cursor.isNull(indexIdPersonnel)) ? cursor.getInt(indexIdPersonnel) : -1;
        int numEtudiant = (!cursor.isNull(indexNumEtudiant)) ? cursor.getInt(indexNumEtudiant) : -1;

        Presence item = new Presence(
                idEvenement,
                numEtudiant,
                sp,
                false,
                idPersonnel,
                -1
        );
        db.insert(DBTables.Presence.TABLE_NAME, null, this.getContentValues(item));
        return true;
        */
    }


    @Override
    public void merge(Entity[] entities) {
        for (Entity e : entities) {
            Presence presence = (Presence) e;
            deleteItem(presence.getIdPresence());
            long res = insertItem(presence);
            if (res == -1) {
                throw new SQLException("Unable to merge Presence Table");
            }
        }
    }

    private int deleteItem(int idPresence) {
        SQLiteDatabase db = super.helper.getWritableDatabase();
        return db.delete(DBTables.Presence.TABLE_NAME, DBTables.Presence.COLONNE_ID_PRESENCE + " = ?", new String[]{String.valueOf(idPresence)});
    }
}