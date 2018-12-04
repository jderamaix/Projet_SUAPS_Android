package c.acdi.master.jderamaix.suaps;

/**
 * Classe utilisé pour transité les informations nécessaires pour
 *      les requêtes de  java dans l'application au Json pour la base de données,
 *      concernant les paramètres des séances.
 * Concerne les requêtes liées  :
 *      - au nombre limite de personnes dans une séance,
 *      - au temps autorisé par séance pour les utilisateurs de la séance,
 *      - au numéro de la séance.
 * Utilisé en cas de :
 *      Envoi de ces données à la base de données après modifications sur l'applications,
 *      Initialisation de l'application en prenant les données par défauts dans la base dde données.
 */
public class AuaListeSeance {
    /*
     * La limite de personnes autorisés en un même temps dans une séance.
     */
    private String limitePersonnes;
    /*
     * Le temps limite d'une séance pour un utilisateur, il peut dépasser,
     * mais il sera choisi en priorité si la limite est atteinte et qu'un autre utilisateur veut participer.
     */
    private String tempsSeance;
    /*
     * L'id de la séance, pour l'instant elle n'est pas utilisée.
     */
    private int idSeance;

    /*
     * Le constructeur de la classe avec chaque données d'initialisé.
     */
    public AuaListeSeance(String limitePersonnes, String tempsSeance, int idSeance) {
        this.idSeance = idSeance;
        this.limitePersonnes = limitePersonnes;
        this.tempsSeance = tempsSeance;
    }

    /**
     * Méthode changeant le temps limite de la séance.
     * @param tempsSeance : Le nouveau temps limite de la séance.
     */
    public void setTempsSeance(String tempsSeance){
        this.tempsSeance = tempsSeance;
    }

    /**
     * Méthode changeant le nombre limite d'utilisateurs de la séance.
     * @param limitePersonnes : La nouvelle limite d'utilisateurs de la séance.
     */
    public void setLimitePersonnes(String limitePersonnes){
        this.limitePersonnes = limitePersonnes;
    }

    /**
     * Méthode changeant l'id de la séance.
     * @param idSeance : La nouvelle id de la séance.
     */
    public void setIdSeance(int idSeance){
        this.idSeance = idSeance;
    }

    /**
     * Méthode renvoyant le temps limite de la personne.
     * @return : le temps limite pour une personne dans la séance.
     */
    public String getTempsSeance(){
        return this.tempsSeance;
    }

    /**
     * Méthode renvoyant la limite de'utilisateurs pour la séance.
     * @return : la limite d'utilisateurs pour la séance.
     */
    public String getLimitePersonnes(){
        return this.limitePersonnes;
    }

    /**
     * Méthode renvoyant l'id de la séance.
     * @return : L'id de la séance.
     */
    public int getIdSeance(){
        return this.idSeance;
    }
}
