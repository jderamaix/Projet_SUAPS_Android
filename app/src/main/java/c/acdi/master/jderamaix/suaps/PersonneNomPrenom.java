package c.acdi.master.jderamaix.suaps;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;

import java.sql.Blob;

/**
 * Classe représentant un étudiant et alignée sur les types de la base de données
 */
public class PersonneNomPrenom {

    /*
     * Données fournies par la base de données.
     */
    private String nom;
    private String prenom;

    public PersonneNomPrenom(String nom, String prenom){
        this.nom = nom;
        this.prenom = prenom;
    }

    public void setNom(String nom){
        this.nom = nom;
    }

    public void setPrenom(String prenom){
        this.prenom = prenom;
    }

    public String getNom(){
        return this.nom;
    }

    public String getPrenom(){
        return this.prenom;
    }
}
