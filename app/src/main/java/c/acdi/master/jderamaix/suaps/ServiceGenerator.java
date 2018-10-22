package c.acdi.master.jderamaix.suaps;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private  static final String BASE_URL =  "http://192.168.43.238:8000/";

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass){
        Log.e("Pendantcréationcall","Pendant creation calll");
        return retrofit.create(serviceClass);
    }

}
