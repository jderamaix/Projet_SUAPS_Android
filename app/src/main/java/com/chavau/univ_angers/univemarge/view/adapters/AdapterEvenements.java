package com.chavau.univ_angers.univemarge.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chavau.univ_angers.univemarge.R;
import com.chavau.univ_angers.univemarge.database.entities.Evenement;
import com.chavau.univ_angers.univemarge.intermediaire.Cours;
import com.chavau.univ_angers.univemarge.utils.Utils;
import com.chavau.univ_angers.univemarge.view.activities.BadgeageEtudiant;
import com.chavau.univ_angers.univemarge.view.activities.Musculation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdapterEvenements extends RecyclerView.Adapter<AdapterEvenements.ViewHolderCours> {

    private List<Evenement> _cours;
    private Context _context; //pouvoir utiliser des layouts plus tard

    private static String nomAct = "NOM_ACTIVITE";
    private static String idEvent = "idEvent";
    private final static String listeEtud = "LISTE_ETUDIANT";

    public AdapterEvenements(Context context, ArrayList<Evenement> cours) {
        _context = context;
        _cours = cours;
    }

    static class ViewHolderCours extends RecyclerView.ViewHolder {

        private CardView _cardview;

        public ViewHolderCours(CardView cv) {
            super(cv);
            _cardview = cv;
        }

    }

    @Override
    public ViewHolderCours onCreateViewHolder(ViewGroup parent, int i) {
        CardView cv = (CardView) LayoutInflater.from(_context).inflate(R.layout.vue_evenements,parent,false);
        return new ViewHolderCours(cv);
    }

    @Override
    public void onBindViewHolder(final ViewHolderCours viewHolderCours, final int i) {
        final Evenement cours = _cours.get(i);
        final CardView cardview = viewHolderCours._cardview;

        nomAct = cours.getLibelleEvenement();

        //Recuperation du TEXTVIEW pour l'intitulé de cours
        TextView tv = (TextView) cardview.findViewById(R.id.tv_intituleCours);
        //Recuperation de donnée correspondante
        tv.setText(cours.getLibelleEvenement());

        // Affectation des différentes textview avec les bonnes données
        tv = (TextView) cardview.findViewById(R.id.tv_jourSemaine);

        Calendar c = Calendar.getInstance();
        c.setTime(cours.getDateDebut());
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        tv.setText(Utils.IntToDayOfWeek(dayOfWeek));

        tv = (TextView) cardview.findViewById(R.id.tv_date);
        tv.setText(cours.getDateDebutToString().substring(0,5));

        tv = (TextView) cardview.findViewById(R.id.tx_details);
        //tv.setText("De "+Utils.convertDateToStringHour(cours.getDateDebut())+" à "+Utils.convertDateToStringHour(cours.getDateFin()));
        tv.setText("De "+cours.getDateDebut()+" à "+cours.getDateFin()); // TODO : recup l'heure avec le fuseau horaire

        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!cours.getLibelleEvenement().equals("Musculation")) { // TODO : ne pas mettre musuculation mais plutot utiliser indice roulant -> marche pas encore tous a 0
                    // Préparation des données à envoyer au deuxième activité
                    Intent intent = new Intent(_context, BadgeageEtudiant.class);
                    intent.putExtra(AdapterEvenements.getNomAct(),((TextView) cardview.findViewById(R.id.tv_intituleCours)).getText().toString());
                    intent.putExtra(getIdEvent(),cours.getIdEvenement());

                    // Envoie la liste des étudiant(e)s inscrit(e)s dans l'activité
                    //intent.putParcelableArrayListExtra(AdapterEvenements.getListeEtud(),cours.get_listeEtudiantInscrit()); // TODO : récupérer la liste via la bdd

                    //Commencer la deuxième activité
                    _context.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(_context, Musculation.class);
                    intent.putExtra(nomAct,cours.getIdEvenement());
                    _context.startActivity(intent);
                }

            }
        });

    }

    public void setListeCours(ArrayList<Evenement> lc) {
        _cours = lc;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return _cours.size();
    }

    public static String getNomAct() {
        return nomAct;
    }
    public static String getIdEvent() {
        return idEvent;
    }

    public static String getListeEtud() {
        return listeEtud;
    }
}