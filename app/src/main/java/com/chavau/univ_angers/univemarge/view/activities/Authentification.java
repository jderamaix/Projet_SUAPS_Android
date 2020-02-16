package com.chavau.univ_angers.univemarge.view.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chavau.univ_angers.univemarge.MainActivity;
import com.chavau.univ_angers.univemarge.R;
import com.chavau.univ_angers.univemarge.database.DatabaseHelper;
import com.chavau.univ_angers.univemarge.database.dao.PersonnelDAO;
import com.chavau.univ_angers.univemarge.utils.Utils;
import com.chavau.univ_angers.univemarge.view.fragments.Authentification_Fragment;

/**
 * Activité servant à authentifier un personnel responsable
 */
public class Authentification extends AppCompatActivity implements Authentification_Fragment.CallBacks{

    private static final String TAG_AUTHENTIFICATION_FRAGMENT = "authentification_fragment";
    private Authentification_Fragment authentification_fragment;

    private EditText ed_login;
    private EditText ed_mdp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("activité" , "activité create authentification");

        setContentView(R.layout.activity_authentification);

        ed_login = findViewById(R.id.ed_connexion_login);
        ed_mdp = findViewById(R.id.ed_connexion_mdp);

        FragmentManager frag_manage = getSupportFragmentManager();

        authentification_fragment = (Authentification_Fragment)frag_manage.findFragmentByTag(TAG_AUTHENTIFICATION_FRAGMENT);

        // Fragment sans interface pour l'appel API dans une asyncTask
        if(authentification_fragment == null){
            authentification_fragment = new Authentification_Fragment();
            frag_manage.beginTransaction().add(authentification_fragment, TAG_AUTHENTIFICATION_FRAGMENT).commit();
        }

    }

    public void onClickValider(View v){
        //TODO : vérifier connexion internet
        if (Utils.isConnectedInternet(this)) {
            String login = ed_login.getText().toString();
            String mdp = ed_mdp.getText().toString();
            if (login.equals("") || mdp.equals("")) {
                Toast.makeText(this, R.string.connexion_erreur_champ, Toast.LENGTH_LONG).show();
                //TODO : mettre en rouge l'erreur sur l'écran ?
            } else {
                authentification_fragment.requete_connexion(login, mdp);
            }
        }
        else {
            Toast.makeText(this, R.string.connexion_missing, Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * Recupère le login de l'enseignant, met aussi son identifiant en paramètre TODO
     * @param result vrai ou faux, en fonction du succès de la requete
     * @param login login de la personne authentifiée
     * @param key clef de sécurisation pour les appels API
     */
    @Override
    public void onItemDone(boolean result, String login, String key) {
        if(result) {
            //TODO : mettre login en préférence (voir token) et lancer MainActivity (ou activité de liste de cours)
            SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.PREFERENCE),0);
            SharedPreferences.Editor editor = preferences.edit();

            login = "h.fior"; // TODO : enlever pour les tests

            PersonnelDAO dao = new PersonnelDAO(new DatabaseHelper(this));
            int identifiant_responsable = dao.getIdFromLogin(login.toLowerCase());

            editor.putString(getResources().getString(R.string.PREF_LOGIN),login.toLowerCase());
            editor.putInt(getResources().getString(R.string.PREF_IDENTIFIANT),identifiant_responsable);

            editor.commit();

            //System.out.println("login : " + login + " identifiant : " + identifiant_responsable);

            Intent intent = new Intent(Authentification.this, MainActivity.class);
            startActivity(intent);
        }
        else {
            ed_mdp.setText("");
            Toast.makeText(this, R.string.connexion_erreur_mdp, Toast.LENGTH_LONG).show();
        }

    }

    /**
     * suppression du retour pour après une déconnexion
     */
    @Override
    public void onBackPressed() {

    }
}
