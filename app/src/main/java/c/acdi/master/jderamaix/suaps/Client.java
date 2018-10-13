package c.acdi.master.jderamaix.suaps;


import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Client {

    @GET("articles")
    Call<List<Classe>> Methode();
}
