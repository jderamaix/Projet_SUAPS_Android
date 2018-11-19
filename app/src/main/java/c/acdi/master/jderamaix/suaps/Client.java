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
 *          @ précède le typ de requête voulu.
 *          Peut-être utilisé avec pour exemple : GET, POST, PUT, PATCH.
 *          Suit ensuite soit :
 *          - Une partie de l'URL, le début étant donnée dans l'instanciation de retrofit.
 *          - La partie entière de l'URL si une partie n'ai pas donnée lors de l'instanciation de retrofit.
 *          - Rien si @Url est utilisé comme paramètre de la méthode.
 *
 *          La réponse attendu de la requête, pouvant aller de Call<void> à Call<List<T>>
 *              en passant par Call<T>.
 *
 *         Nom de la méthode
 *
 *         Paramètre(s) : @Path, @Body, @Field ou autres.
 *
 */
public interface Client {

    /**
     * Requête pour prendre les informations de toutes personnes présentes
     * Le 1 permet d'indiquer que la requête vient de l'application et non de l'écran
     */
    @GET("/controlleur/listePersonne/1")
    Call<List<ModeleUtilisateur>> RecoitPersonnes();


    /**
     *
     */
    @GET("/controlleur/sendSeance")
    Call<List<AuaListeSeance>> RecoitParametre();

    /**
     * Requête pour enlever quelqu'un selon son numero id
     * Prend en Body et en Path son numéro id
     */
    @POST("/controlleur/vuePresenceUpdate/{temp}")
    Call<Void> EnleverPersonne(@Path("temp") String Variable, @Body NumeroIDCarteEtudiant IDEtudiant);


    /**
     * Requête pour envoyer une personne badgeant avec son numéro de carte
     * Prend en Body et en Path son numéro de carte
     * Attend un retour de type String pour avoir des informations sur l'intéraction avec la base de données
     */
    @FormUrlEncoded
     @POST("/controlleur/badgeage")
    Call<ReponseRequete> EnvoieNumCarte(@Field("numeroCarte") String numeroCarte);




    /**
     *Requête pour envoyer une personne ajouter manuellement avec son nom
     *Prend en Body et en Path son nom
    */
    @FormUrlEncoded
    @POST("/controlleur/addPersonne")
    Call<ReponseRequete> EnvoieNom(@Field("nom") String nom,
                                   @Field("prenom") String prenom
    );

    /**
     * Requête envoyant les changements d'heures de séances et capacité d'accueil
     * Prend en Path le string commposé de la capacité / heure  / 1
     * Attend un retour de type String pour avoir des informations sur l'intéraction avec la base de données
     */
    @FormUrlEncoded
    @POST("controlleur/setSeance")
    Call<ReponseRequete> EnvoieTempsCapacite(@Field("capacite") String capacite,
                                                @Field("temps") String temps,
                                                @Field("id") String id);

}
