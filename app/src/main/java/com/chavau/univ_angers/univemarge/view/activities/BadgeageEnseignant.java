package com.chavau.univ_angers.univemarge.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chavau.univ_angers.univemarge.R;
import com.chavau.univ_angers.univemarge.database.DatabaseHelper;
import com.chavau.univ_angers.univemarge.database.dao.EtudiantDAO;
import com.chavau.univ_angers.univemarge.database.entities.Etudiant;
import com.chavau.univ_angers.univemarge.view.adapters.AdapterEvenements;
import com.chavau.univ_angers.univemarge.view.adapters.AdapterPersonneInscrite;

import java.util.ArrayList;

public class BadgeageEnseignant extends AppCompatActivity {
    private RecyclerView _recyclerview;
    private Intent _intent;
    private String _titreActivite;
    private ArrayList<Etudiant> _etudiants;
    AdapterPersonneInscrite _api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_seance);

        _intent = getIntent();

        _titreActivite = _intent.getStringExtra(AdapterEvenements.getNomAct());
        int id_evenement = _intent.getIntExtra(AdapterEvenements.getIdEvent(),0);

        setTitle(_titreActivite);

        _recyclerview = findViewById(R.id.recyclerview_modification_seance);

        //_etudiants = _intent.getParcelableArrayListExtra(AdapterEvenements.getListeEtud());


        EtudiantDAO dao = new EtudiantDAO(new DatabaseHelper(this));
        _etudiants =  dao.listeEtudiantInscrit(id_evenement);

        // TODO : ca marche pas
        _api = new AdapterPersonneInscrite(this, _etudiants, AdapterPersonneInscrite.VueChoix.MS);

        _recyclerview.setLayoutManager(new LinearLayoutManager(this));
        _recyclerview.setAdapter(_api);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        //returnIntent.putParcelableArrayListExtra(AdapterEvenements.getListeEtud(),_api.get_etudIns());
        setResult(RESULT_OK,returnIntent);
        finish();
    }
}