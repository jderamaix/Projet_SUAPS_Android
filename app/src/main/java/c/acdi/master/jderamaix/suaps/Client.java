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
     *Requête pour envoyer une personne badgeant avec son numéro de carte
     *Prend en Body et en Path son numéro de carte
    */
     @POST("/controlleur/{temp}")
    Call<NomIDCarteEtudiant> EnvoieNumCarte(@Path("temp") String Variable, @Body NomIDCarteEtudiant nomEtudiant);




    /**
     *Requête pour envoyer une personne ajouter manuellement avec son nom
     *Prend en Body et en Path son nom
    */
     @POST("/controlleur/addPersonne/{temp}")
     Call<Void> EnvoieNom(@Path("temp") String Variable, @Body NomIDCarteEtudiant nomEtudiant);


    /**
     *Requête envoyant les changements d'heures de séances et capacité d'accueil
     * Prend en Path le string commposé de la capacité / heure  / 1
     */
    @POST("controlleur/setSeance/{temp}")
    Call<Void> EnvoieTempsCapacite(@Path("temp") String Variable, @Body AuaListeSeance auaListeSeance);
}
