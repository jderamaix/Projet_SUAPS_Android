package com.chavau.univ_angers.univemarge.sync;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.chavau.univ_angers.univemarge.R;
import com.chavau.univ_angers.univemarge.database.DatabaseHelper;
import com.chavau.univ_angers.univemarge.database.dao.AutreDAO;
import com.chavau.univ_angers.univemarge.database.dao.EtudiantDAO;
import com.chavau.univ_angers.univemarge.database.dao.EvenementDAO;
import com.chavau.univ_angers.univemarge.database.dao.InscriptionDAO;
import com.chavau.univ_angers.univemarge.database.dao.PersonnelDAO;
import com.chavau.univ_angers.univemarge.database.dao.PresenceDAO;
import com.chavau.univ_angers.univemarge.database.dao.PresenceRoulantDAO;
import com.chavau.univ_angers.univemarge.database.dao.ResponsableDAO;
import com.chavau.univ_angers.univemarge.database.dao.RoulantParametreDAO;
import com.chavau.univ_angers.univemarge.database.entities.Autre;
import com.chavau.univ_angers.univemarge.database.entities.Etudiant;
import com.chavau.univ_angers.univemarge.database.entities.Evenement;
import com.chavau.univ_angers.univemarge.database.entities.Inscription;
import com.chavau.univ_angers.univemarge.database.entities.Personnel;
import com.chavau.univ_angers.univemarge.database.entities.Presence;
import com.chavau.univ_angers.univemarge.database.entities.PresenceRoulant;
import com.chavau.univ_angers.univemarge.database.entities.Responsable;
import com.chavau.univ_angers.univemarge.database.entities.RoulantParametre;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;


public class APICall extends Fragment {

    boolean mReady = false;
    boolean mQuiting = false;
    private static final String API_URL = "https://api-emaua.univ-angers.fr/api-emaua/";
    //private static final String API_URL = "http://172.19.3.100:8090/api-emaua/";
    private static final String LOGIN_INPUT_NAME = "login=";
    private static final String DATE_INPUT_NAME = "datemaj=";
    private static final int WAIT_TIME_MILISEC = 15000000;
    private Thread mThread = null;
    private Context context;
    private Date dateMaj;
    private String login, stringDateMaj=null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //creation du thread pour synchroniser les données
        this.mThread = new Thread() {
            @Override
            public void run() {
                // This thread runs almost forever.
                    // update only if connected to internet
                    if(testInternetConnection()) {
                        sendRequest(new SyncElement("/autres", Autre[].class, new AutreDAO(new DatabaseHelper(context))));
                        sendRequest(new SyncElement("/etudiants", Etudiant[].class, new EtudiantDAO(new DatabaseHelper(context))));
                        sendRequest(new SyncElement("/evenements", Evenement[].class, new EvenementDAO(new DatabaseHelper(context))));
                        sendRequest(new SyncElement("/inscriptions", Inscription[].class, new InscriptionDAO(new DatabaseHelper(context))));
                        sendRequest(new SyncElement("/personnels", Personnel[].class, new PersonnelDAO(new DatabaseHelper(context))));
                        // TODO : dans api renvoyer un tableau pour un seul user
                        sendRequest(new SyncElement("/info_user", Personnel[].class, new PersonnelDAO(new DatabaseHelper(context))));
                        sendRequest(new SyncElement("/presences", Presence[].class, new PresenceDAO(new DatabaseHelper(context))));
                        sendRequest(new SyncElement("/presence_roulants", PresenceRoulant[].class, new PresenceRoulantDAO(new DatabaseHelper(context))));
                        sendRequest(new SyncElement("/responsables", Responsable[].class, new ResponsableDAO(new DatabaseHelper(context))));
                        sendRequest(new SyncElement("/roulant_parametre", RoulantParametre[].class, new RoulantParametreDAO(new DatabaseHelper(context))));

                        // TODO : re-assign the new DateMaj
                        setDateMaj(new Date());
                    }

                }

        };

        // pour garder le fragment actif lorsque l'activité est détruite (ex: rotation)
        setRetainInstance(true);

       onDestroy();
    }

    /**
     * fonction qui lance le thread de synchronisation au click sur le bouton synchroniser
     */
    public void synchronize(){
        // Start up the worker thread.
        mThread.start();
    }


    /**
     * This is called when the fragment is going away.  It is NOT called
     * when the fragment is being propagated between activity instances.
     */
    @Override
    public void onDestroy() {
        // Make the thread go away.
        synchronized (mThread) {
            mReady = false;
            mQuiting = true;
            mThread.notify();
        }

        super.onDestroy();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

        // récupération des preferences login et dateMaj
        SharedPreferences pref = this.getActivity().getSharedPreferences(getResources().getString(R.string.PREFERENCE),Context.MODE_PRIVATE);
        login = pref.getString(getResources().getString(R.string.PREF_LOGIN),"");
        stringDateMaj = pref.getString(getResources().getString(R.string.PREF_DATE_MAJ),"");

        // Si on a cliquer
        if(mThread != null){
            synchronized (mThread) {
                mReady = true;
                mThread.notify();
            }
        }
    }

    /**
     * This is called right before the fragment is detached from its
     * current activity instance.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        // This fragment is being detached from its activity.  We need
        // to make sure its thread is not going to touch any activity
        // state after returning from this function.
        synchronized (mThread) {
            mReady = false;
            mThread.notify();
        }
    }

    /**
     * API for our UI to restart the progress thread.
     */
    public void restart() {
        synchronized (mThread) {
            mThread.notify();
        }
    }

    private void sendRequest(SyncElement element) {
        System.out.println("synchronising " + element.getLink());

        // TODO : remplacer par l'appel qui permet de récupérer ces 2 valeurs

        final String login = "h.fior";
        //final String dateMaj = "201910010000";
        final String dateMaj = "";

        // set up parameters & url
        String parameters = LOGIN_INPUT_NAME + login;
        if(stringDateMaj != null && !"".equals(dateMaj)) {
            parameters += "&" + DATE_INPUT_NAME + stringDateMaj;
        }
        final String url = API_URL + element.getLink() + "?" + parameters;

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~url : " + url);
        // set up the http request
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();


        // make the call to the api
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("ERROR : cannot merge " + element.getLink());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    // send the response to merge to out data
                    if (response.body() != null) {
                        final String reponse = Objects.requireNonNull(response.body()).string();
                        System.out.println("Response received for " + element.getLink());

                        if("[]".equals(reponse)) {
                            System.out.println("WARNING : empty data for " + element.getLink());
                        } else {
                            element.merge(reponse);
                            System.out.println(element.getLink() + " has successfully been merged");
                        }
                    }
                }
            }
        });
    }

    public boolean testInternetConnection() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException | InterruptedException e)          { e.printStackTrace(); }

        return false;
    }



    public Date getDateMaj() {
        return  this.dateMaj;
    }

    public void setDateMaj(Date dateMaj) {
        this.dateMaj = dateMaj;
    }
}
