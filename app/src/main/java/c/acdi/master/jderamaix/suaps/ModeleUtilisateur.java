package c.acdi.master.jderamaix.suaps;


/**
  * Classe utilisé pour transité les données nécessaires au
 *      requête du java de l'application au Json de la base de données,
 *      concernant les utilisateurs.
 *  Concerne les requêtes liéés :<br>
 *      - au nom d'un participant,<br>
 *      - au prénom d'un participant,<br>
 *      - au numéro de carte de la carte du participant,<br>
 *      - au temps déjà effectué par le participant dans cette séance.<br>
 *  Utilisé en cas de :<br>
 *      - Actualisation de l'affichage des utilisateurs d'une séance.<br>
  */
public class ModeleUtilisateur {

    /*
     * Nom de l'utilisateur
     */
    private String nom;
    /*
     * Prénom de l'utilisateur
     */
    private String prenom;
    /*
     * Numéro de la carte de l'utilisateur
     */
    private int no_etudiant;
    /*
     * Durée de la présence de l'utilisateur pour cette séance
     */
    private String duree;

    /**
     * Constructeur de ModeleUtilisateur initialisant le nom et le prénom de l'utilisateur.
     * @param nom : Nom de l'utilisateur.
     * @param prenom : Prénom de l'utilisateur.
     */
    public ModeleUtilisateur(String nom, String prenom){
        this.nom = nom;
        this.prenom = prenom;
    }


    /**
     * Méthode retournant le nom de l'utilisateur.
     * @return le nom de l'utilisteur.
     */
    public String getNom(){
        return this.nom;
    }

    /**
     * Méthode retournant le prénom de l'utilisateur.
     * @return le prénom de l'utilisateur.
     */
    public String getPrenom(){
        return this.prenom;
    }

    /**
     * Méthode retournant le numéro de la carte de l'utilisateur.
     * @return le numéro de la carte de l'utilisateur.
     */
    public int getNo_etudiant(){
        return this.no_etudiant;
    }

    /**
     * Méthode retournant la durée que l'utilisateur a passé dans la session.
     * @return la durée que l'utilisateur a passé dans la session.
     */
    public String getDuree(){ return this.duree;}
}
