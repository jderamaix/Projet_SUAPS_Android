package c.acdi.master.jderamaix.suaps;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe utilisé pour recevoir les information d'une personne par un get
 */
public class Classe {

    private String nom;
    private String prenom;

    private int no_etudiant;
    private SimpleDateFormat temps;


    public void setNom(String nom){
        this.nom = nom;
    }

    public void setPrenom(String prenom){
        this.prenom = prenom;
    }


    public void setNo_etudiant(int no_individu){
        this.no_etudiant = no_individu;
    }

    public void setTemps(SimpleDateFormat temps){ this.temps = temps;}


    public String getNom(){
        return this.nom;
    }

    public String getPrenom(){
        return this.prenom;
    }


    public int getNo_etudiant(){
        return this.no_etudiant;
    }

    public SimpleDateFormat getTemps(){ return this.temps;}
}
