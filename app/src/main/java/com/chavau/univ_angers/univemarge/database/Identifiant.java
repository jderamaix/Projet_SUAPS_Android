package com.chavau.univ_angers.univemarge.database;

import java.util.HashMap;
import java.util.Map;

/**
 * Identifiant ce comporte comme une ArrayList.
 * Il permet de stocker les identifians tels que les clés primaires des tables en clé
 * ex: DBTables.Etudiant.COLONNE_NUMERO_ETUDIANT avec comme valeur "42"
 */
public class Identifiant {

    private Map<String, Integer> ids;

    public Identifiant() {
        this.ids = new HashMap<>();
    }

    public void ajoutId(String cle, int valeur) {
        this.ids.put(cle, valeur);
    }

    public int getId(String cle) {
        int id = -1;

        if (this.ids.get(cle) != null)
            id = this.ids.get(cle);
        return id;
    }
}
