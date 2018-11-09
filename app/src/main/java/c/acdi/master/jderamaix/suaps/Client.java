package c.acdi.master.jderamaix.suaps;



import android.graphics.drawable.Drawable;
import android.media.Image;
import android.provider.Telephony;

import java.sql.Blob;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Client {

    /**
     * Requête pour prendre les informations de toutes personnes présentes
     * Le 1 permet d'indiquer que la requête vient de l'application et non de l'écran
     */
    @GET("/controlleur/listePersonne/1")
    Call<List<ModeleEtudiant>> RecoitPersonnes();


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
    Call<Void> EnleverPersonne(@Path("temp") String Variable, @Body NomIDCarteEtudiant IDEtudiant);


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
