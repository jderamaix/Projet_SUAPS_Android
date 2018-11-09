package c.acdi.master.jderamaix.suaps;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;

import java.sql.Blob;

/**
 * Classe utilisé commemodèle pour représenter un étudiant et avoir les même types de données
 *     que la base de données
 */
public class PersonneAvecImage {

    //Les données obtenues de la base de données lors d'une requêtesur les étudiants
    private String nom;
    private String prenom;
    //private Image image;


    public PersonneAvecImage(String nom, String prenom/*,Image image*/){
        this.nom = nom;
        this.prenom = prenom;
        //this.image = image;
    }

//    public void setImage(Image image){ this.image = image;}

    public void setNom(String nom){
        this.nom = nom;
    }

    public void setPrenom(String prenom){
        this.prenom = prenom;
    }


  //  public Image getImage(){return this.image;}

    public String getNom(){
        return this.nom;
    }

    public String getPrenom(){
        return this.prenom;
    }
}
