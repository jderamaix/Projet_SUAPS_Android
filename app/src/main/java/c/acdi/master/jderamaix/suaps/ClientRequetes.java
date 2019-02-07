package c.acdi.master.jderamaix.suaps;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


/**
 *  Classe utilisé pour contenir tous les corps de requête.
 *  Ce sont ces méthodes qui doivent être appellés pour l'instantion du Call pour les requêtes.
 *  Méthode type :
 *          {@literal @} précède le typ de requête voulu.
 *          Peut-être utilisé avec pour exemple : GET, POST, PUT, PATCH.
 *          Suit ensuite soit :<br>
 *          - Une partie de l'URL, le début étant donnée dans l'instanciation de retrofit.<br>
 *          - La partie entière de l'URL si une partie n'ai pas donnée lors de l'instanciation de retrofit.<br>
 *          - Rien si @Url est utilisé comme paramètre de la méthode.<br><br>
 *
 *          La réponse attendu de la requête, pouvant aller de Call{@literal <}void{@literal >} à Call{@literal <}List{@literal <}T{@literal >}{@literal >}
 *              en passant par Call{@literal <}T{@literal >}.<br>
 *
 *         Nom de la méthode :
 *
 *         Paramètre(s) : @Path, @Body, @Field ou autres.
 *
 */
public interface ClientRequetes {

    /**
     * Requête pour prendre les informations de toutes personnes présentes.
     * Le 1 permet d'indiquer que la requête vient de l'application et non de l'écran.
     *
     * @return réponse de la requête
     */
    @GET("/controlleur/listePersonne/1")
    Call<List<ModeleUtilisateur>> RecoitPersonnes();

    /**
     * Requête envoyant les changements d'heures de séances et capacité d'accueil
     * Prend en Path le string commposé de la capacité / heure  / 1
     * Attend un retour de type String pour avoir des informations sur l'intéraction avec la base de données
     *
     *
     * @param capacite Nouvelle capacité à envoyé au serveur
     * @param temps Nouvelle durée minimal pour une séance
     * @param id identifiant de la séance
     * @return Réponse du serveur
     */
    @FormUrlEncoded
    @POST("controlleur/setSeance")
    Call<ReponseRequete> EnvoieTempsCapacite(@Field("capacite") String capacite,
                                             @Field("temps") String temps,
                                             @Field("id") String id);

    /**
     *
     * Requête pour prendre les données de la base de données à propos des paramètres de la séance
     * pour pouvoir les mettre à jour sur l'affichage.
     *
     * @return réponse de la requête
     */
    @GET("/controlleur/sendSeance")
    Call<List<AuaListeSeance>> RecoitParametre();

    /**
     * Requête pour enlever quelqu'un selon son numero id
     * Prend en Body et en Path son numéro id
     *
     *
     * @param Variable Contient l'id de l'étudiant pour la séance actuelle (à confirmer)
     * @param IDEtudiant Identifiant de l'étudiant pour le repérer dans la base de données
     * @return réponse de la requête
     */
    @POST("/controlleur/vuePresenceUpdate/{temp}")
    Call<Void> EnleverPersonne(@Path("temp") String Variable, @Body NumeroIDCarteEtudiant IDEtudiant);

    /**
     * Requête pour envoyer une personne badgeant avec son numéro de carte
     * Prend en Field et en Path son numéro de carte
     * Attend un retour de type String pour avoir des informations sur l'intéraction avec la base de données
     *
     * @param numeroCarte numéro MIFARE inverse de la carte qui sert d'identifiant
     * @return réponse de la requête
     */
    @FormUrlEncoded
     @POST("/controlleur/badgeage")
    Call<ReponseRequete> EnvoieNumCarte(@Field("numeroCarte") String numeroCarte);

    /**
     * Requête pour envoyer une personne ajouter manuellement avec son nom
     * Prend en Field et en Path son nom
     *
     * @param nom Nom de l'étudiant
     * @param prenom Prénom de l'étudiant
     * @return réponse de la requête
     */
    @FormUrlEncoded
    @POST("/controlleur/addPersonne")
    Call<ReponseRequete> EnvoieNom(@Field("nom") String nom,
                                   @Field("prenom") String prenom
    );

    /**
     * Requête utilisé pour tester si l'adresse IP actuelle est celle du serveur.
     * Ne prend aucun paramètre, ne fais rien d'autre que d'assayer d'atteindre la route pour le test.
     *
     * @return réponse de la requête
     */
    @GET("/controlleur/connexion")
    Call<ReponseRequete> TestBonneAdresseIP();

}
