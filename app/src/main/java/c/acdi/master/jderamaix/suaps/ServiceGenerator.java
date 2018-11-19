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
public class ServiceGenerator {

    //Le début des url des routes, peut être remplacés par une autre url dans les méthodes utilisés.
    private  static final String BASE_URL =  "http://192.168.43.104:8000/";

    //Le builder de retrofit où on ajoute :
    //      le début de l'url à ajouté,
    //      le convertisseur à utilisé pour transformer les informations des requêtes, contenu dans des objets java,
    //          à la base de données s'attendant à un langage spécifique, ici du Json.
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    //Contient la construction du builder.
    private static Retrofit retrofit = builder.build();

    //Le builder du OkhttpClient utilisé par retrofit.
    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder();

    //Un intercepteur utilisé pour intercepté les messages de retour des requêtes,
    // permettant de généraliser du code.
    private static Interceptor interceptor = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            okhttp3.Response response = chain.proceed(request);
            return response;
        }
    };

    //Méthode utilisé pour afficher un toast montrant qu'elle erreur s'est passé.
    public static void Message(Context c, String TAG, Throwable t) {
        Toast.makeText(
                c,
                (t instanceof IOException)? "Erreur de connexion": "Problème de conversion",
                Toast.LENGTH_SHORT
        ).show();
        Log.e(TAG,t.getMessage());
        Log.e(TAG,t.toString());
    }

    //Méthode créant le client avec toutes les options voulues.
    //C'est à partir de ce client que les méthodes des requêtes sont lancés.
    public static <S> S createService(Class<S> serviceClass){

        if(!httpClient.interceptors().contains(interceptor)){
            httpClient.addInterceptor(interceptor);
            builder.client(httpClient.build());
            retrofit = builder.build();
        }

        return retrofit.create(serviceClass);
    }

}
