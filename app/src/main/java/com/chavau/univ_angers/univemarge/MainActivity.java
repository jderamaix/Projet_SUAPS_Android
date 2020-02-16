package com.chavau.univ_angers.univemarge;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.chavau.univ_angers.univemarge.database.DatabaseHelper;
import com.chavau.univ_angers.univemarge.database.dao.PersonnelDAO;
import com.chavau.univ_angers.univemarge.sync.APICall;
import com.chavau.univ_angers.univemarge.view.activities.Authentification;
import com.chavau.univ_angers.univemarge.view.activities.CodePinDialogue;
import com.chavau.univ_angers.univemarge.view.activities.ListeEvenementsCours;
import com.chavau.univ_angers.univemarge.view.fragment.Configuration_dialog;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);

        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.PREFERENCE), 0);

        if (preferences.getString(getResources().getString(R.string.PREF_LOGIN), "").equals("")) {
            Intent intent = new Intent(MainActivity.this, Authentification.class);
            startActivity(intent);
        } else {
            if (preferences.getInt(getResources().getString(R.string.PREF_IDENTIFIANT), 0) == 0) {
                PersonnelDAO dao = new PersonnelDAO(new DatabaseHelper(this));

                SharedPreferences.Editor editor = preferences.edit();
                int identifiant_responsable = dao.getIdFromLogin(preferences.getString(getResources().getString(R.string.PREF_LOGIN), ""));
                editor.putInt(getResources().getString(R.string.PREF_IDENTIFIANT), identifiant_responsable);
                editor.commit();
            }
            Intent intent = new Intent(MainActivity.this, ListeEvenementsCours.class);
            startActivity(intent);


        }
    }
}
