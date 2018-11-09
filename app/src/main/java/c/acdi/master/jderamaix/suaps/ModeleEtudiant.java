package c.acdi.master.jderamaix.suaps;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.sql.Blob;

/**
  * Classe utilisé commemodèle pour représenter un étudiant et avoir les même types de données
  *     que la base de données
  */
public class ModeleEtudiant {

    //Les données obtenues de la base de données lors d'une requêtesur les étudiants
    private String nom;
    private String prenom;
    private int no_etudiant;
    private String duree;


    public ModeleEtudiant(String nom, String prenom){
        this.nom = nom;
        this.prenom = prenom;
    }

    public void setNom(String nom){
        this.nom = nom;
    }

    public void setPrenom(String prenom){
        this.prenom = prenom;
    }


    public void setNo_etudiant(int no_individu){
        this.no_etudiant = no_individu;
    }

    public void setDuree(String temps){ this.duree = temps;}

    public String getNom(){
        return this.nom;
    }

    public String getPrenom(){
        return this.prenom;
    }


    public int getNo_etudiant(){
        return this.no_etudiant;
    }

    public String getDuree(){ return this.duree;}
}
