package com.chavau.univ_angers.univemarge.view.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Authentification_Fragment extends Fragment {

    public static interface CallBacks{
        public void onItemDone(boolean result, String login, String key);
    }

    private String login;
    private String key;

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    private String ticket;
    private CallBacks mainListener = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainListener = (CallBacks) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        login = "";
        key = "" ;
        // pour garder le fragment actif lorsque l'activité est détruite (ex: rotation)
        setRetainInstance(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainListener = null;
    }

    protected void onPostExecute(boolean result){
        if(mainListener != null)
            mainListener.onItemDone(result,login, key);
    }

    protected void setKey(String key){
        this.key = key;
    }

    public void requete_connexion(String login, String mdp){

        this.login = login;

        ArrayList<String> tab = new ArrayList<>();
        tab.add(login);
        tab.add(mdp);

        AsyncAuthentification requete = new AsyncAuthentification(this);
        requete.execute(tab);
    }


    /**
     * TACHE DE FOND
     */

    class AsyncAuthentification extends AsyncTask<ArrayList<String>, Void, Boolean>{
        protected Authentification_Fragment fragment = null;

        private static final String URL_CAS = "https://casv6.univ-angers.fr/cas/v1/tickets";


        AsyncAuthentification(Authentification_Fragment authentification_fragment){
            fragment = authentification_fragment;
        }

        /**
         * Tache de fond
         * @param arrayLists
         * @return true si la personne est reconnu par le CAS, false sinon
         */
        @Override
        protected Boolean doInBackground(ArrayList<String>... arrayLists) {
            Boolean retour = false;
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("username", arrayLists[0].get(0)) // recupération du login
                    .add("password", arrayLists[0].get(1)) // recuperation du mdp
                    .build();

            Request requestok = new Request.Builder()
                    .url(URL_CAS)
                    .post(formBody)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Cache-Control", "no-cache")
                    .build();

            try {

                Response responseok = client.newCall(requestok).execute();
                //String responseBody = responseok.body().string();

                //String url_token = responseBody.substring(137, 137+140); // 140 est la longueur de l'url, 137 est la longueur de la chaine avant l'url
                retour =  responseok.isSuccessful();

                //System.out.println(" AUTHENTIFICATION : url token : " + url_token);

                // TODO : appeler EMA pour verifier l'identité et obtenir la clef

                //fragment.setKey(key);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return retour;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            fragment.onPostExecute(result);
        }
    }
}
