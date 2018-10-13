package c.acdi.master.jderamaix.suaps;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://oc-jswebsrv.herokuapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass){
        return retrofit.create(serviceClass);
    }

}
