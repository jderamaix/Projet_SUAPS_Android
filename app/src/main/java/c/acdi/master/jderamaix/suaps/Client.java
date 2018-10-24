package c.acdi.master.jderamaix.suaps;



import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Client {

    /**
     * Get pour prendre les informations d'une personne
     *
     */
    @GET("/controlleur/listePersonne/1")
    Call<List<Classe>> RecoitPersonnes();


    /**
     * Post pour enlever quelqu'un selon son numero id
     */
    @POST("/controlleur/vuePresenceUpdate/{temp}")
    Call<Void> EnleverPersonne(@Path("temp") String Variable, @Body Task task);


    /**
     *Post pour envoyer une personne badgeant avec son numéro de carte
     *
    */
     @POST("/controlleur/{temp}")
    Call<Void> EnvoieNumCarte(@Path("temp") String Variable, @Body Task task);




    /**
     *Post pour envoyer une personne badgeant avec son nom
     *
    */
     @POST("/controlleur/addPersonne/{temp}")
     Call<Void> EnvoieNom(@Path("temp") String Variable, @Body Task task);


    /**
     *Envoie les changements d'heures de séances et capacité d'accueil
     */
    @POST("controlleur/setSeance/{temp}")
    Call<Void> EnvoieTempsCapacite(@Path("temp") String Variable, @Body AuaListeSeance auaListeSeance);
}
