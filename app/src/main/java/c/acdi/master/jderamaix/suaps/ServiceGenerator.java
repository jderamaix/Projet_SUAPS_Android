package c.acdi.master.jderamaix.suaps;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Classe permettant la création d'un client pour les requêtes.
 * BASE_URL est la base des majorités des URLs utilisés dans les requêtes.
 * builder contient le builder de Retrofit.
 * retrofit contient le lancement de son builder.
 * httpClient contient son builder.
 * interceptor contient une classe permettant d'intercepter les messages d'erreurs des requêtes avant qu'elles
 *      n'arrivent au code les attendant et donc de généralisé du code pour des erreurs spécifiques.
 * createService est la méthode créant le client.
 */
public abstract class ServiceGenerator {

    /**
     * Base des URLs des requêtes.
     */
    private static final String BASE_URL =  "http://";

    /**
     * String contenant le port du serveur, à ajouter à l'url pour les requêtes.
     */
    private static final String FIN_URL = ":8000";

    /**
     * La valeur de l'IP du serveur ou si en cours de test la valeur de l'IP a testé.
     * Utilisé pour avoir l'ULR finale pour les requêtes.
     */
    private static String IP_URL = "";

    /**
     * L'URL constitué de BASE_URL et de IP_URL.
     * Est l'URL utilisé pour le requêtes.
     */
    private static String URL_Complete = "http://urlDeTest";

    /**
     * Méthode modifiant URL_complete par la concaténation deBASE_URL et IP_URL.
     */
    private static void Modification_URL_Complete(){
        URL_Complete = BASE_URL + IP_URL + FIN_URL;
        builder.baseUrl(URL_Complete);
    }

    /**
     * Vrai si l'adresse IP est la bonne.
     * Faux si ce n'est pas la bonne ou elle est en cours de test.
     */
    private static boolean etatDeLAdresseIPDuServeur = false;

    /**
     * Remplace le boolean représentant l'état de l'adresse IP par son nouveau état.
     * @param valeurEtatAdresseIP : Le boolean par lequel remplacé celui actuel.
     */
    public static void setEtatDeLAdresseIPDuServeur(boolean valeurEtatAdresseIP){
        etatDeLAdresseIPDuServeur = valeurEtatAdresseIP;
    }

    /**
     * Retourne le boolean en charge de l'état actuel de l'adresse IP.
     * @return le boolean en charge de l'état actuel de l'adresse IP.
     */
    public static boolean getEtatDeLAdresseIPDuServeur(){
        return etatDeLAdresseIPDuServeur;
    }

    /**
     * Change l'adresse IP actuelle avec la nouvelle adresse IP.
     * @param IP_URL : Nouvelle adresse IP.
     */
    public static void setIPUrl(String IP_URL){
        ServiceGenerator.IP_URL = IP_URL;
        Modification_URL_Complete();
    }

    /**
     * Retourne l'adresse IP actuelle.
     * @return Retourne l'adresse IP actuelle.
     */
    public static String getIpUrl(){
        return ServiceGenerator.IP_URL;
    }



    /**
     * Builder des requêtes.
     */
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(URL_Complete)
            .addConverterFactory(GsonConverterFactory.create());

    /**
     * Lancée du builder des requêtes.
     */
    private static Retrofit retrofit = builder.build();

    /**
     * Instance du builder du client HTTP.
     */
    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder();

    /**
     * Intercepteur interceptant les erreurs.
     */
    private static Interceptor interceptor = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            okhttp3.Response response = chain.proceed(request);
            return response;
        }
    };

    /**
     * Émet un message d'erreur dans un Toast.
     */
    public static void Message(Context c, String TAG, Throwable t) {
        Toast.makeText(
                c,
                (t instanceof IOException)? "Erreur de connexion" : "Problème de conversion",
                Toast.LENGTH_SHORT
        ).show();
        Log.e(TAG,t.getMessage());
        Log.e(TAG,t.toString());
    }

    //Méthode créant le client avec toutes les options voulues.
    //C'est à partir de ce client que les méthodes des requêtes sont lancés.
    public static <S> S createService(Class<S> serviceClass){
        //Test si l'interceptor a été ajouté au client http utilisé par retrofit,
        // si il n'y est pas alors le client http n'a pas été initialisé.
        // Si il y est alors il n'y a pas besoin d'initialiser de nouveau le client http.
        if(!httpClient.interceptors().contains(interceptor)){
            //Ajout l'intercepteur au client http.
            httpClient.addInterceptor(interceptor);
            //Ajout le client http au builder de retrofit.
            builder.client(httpClient.build());
            //Créer l'instance de retrofit utilisé pour les requêtes.
            retrofit = builder.build();
        } else {
            retrofit = builder.build();

        }

        return retrofit.create(serviceClass);
    }

}
