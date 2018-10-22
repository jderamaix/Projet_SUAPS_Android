package c.acdi.master.jderamaix.suaps;


/**
 * Classe utilisé pour recevoir les information d'une personne par un get
 */
public class Classe {

    private String nom_usuel;
    private String prenom;
    private String no_individu;

    public void setNom_usuel(String nom){
        this.nom_usuel = nom;
    }

    public void setPrenom(String prenom){
        this.prenom = prenom;
    }

    public void setNo_individu(String no_individu){
        this.no_individu = no_individu;
    }

    public String getNom(){
        return this.nom_usuel;
    }

    public String getPrenom(){
        return this.prenom;
    }

    public String getNo_individu(){
        return this.no_individu;
    }
}
