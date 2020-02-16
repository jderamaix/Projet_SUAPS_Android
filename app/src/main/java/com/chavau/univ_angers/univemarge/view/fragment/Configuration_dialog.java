package com.chavau.univ_angers.univemarge.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.chavau.univ_angers.univemarge.MainActivity;
import com.chavau.univ_angers.univemarge.R;
import com.chavau.univ_angers.univemarge.view.activities.Musculation;

public class Configuration_dialog extends DialogFragment{


    /**
     * Constructeur par défaut de la classe
     */
    public Configuration_dialog() {
        super();
    }

    /**
     * Méthode servant à créer la fenêtre affichant les informations nécessaire à la configuration
     * d'une séance de sport ( temps et capacité )
     *

     * @param savedInstanceState Bundle qui peut contenir des informations sauvegardées
     * @return Retourne l'affichage de configuration d'une séance
     * @see DialogFragment#onCreateDialog(Bundle)
     */
    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /*
         * On utilise explicitement la classe MainActivity à la place de la simple Activity car ce
         * dialogue d'ajout est déjà spécifique à l'application de toute manière :
         * inutile d'abstraire ceux avec qui on communique
         */
        final Musculation activity = (Musculation) getActivity();
        final View view = LayoutInflater.from(activity).inflate(R.layout.vue_configuration,null);

        /*
         * Initialiser la sélection de la capacité à la valeur actuelle
         */
        final NumberPicker capacity = view.findViewById(R.id.configCapacite);
        capacity.setMinValue(1);
        capacity.setMaxValue(255);
        capacity.setValue(activity.capacity());

        /*
         * Initialiser la sélection du temps minimum au temps minimum actuel
         */
        final TimePicker duration = view.findViewById(R.id.configDuree);
        duration.setIs24HourView(true);
        duration.setCurrentHour(Integer.parseInt(activity.duration().substring(0,2)));
        duration.setCurrentMinute(Integer.parseInt(activity.duration().substring(3)));

        return new AlertDialog.Builder(activity)
                .setView(view)
                .setPositiveButton(R.string.etiquetteConfigValider, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //activity.ModificationCapaciteHeure(capacity.getValue(), duration.getCurrentHour(), duration.getCurrentMinute());
                    }
                })
                .setNegativeButton(R.string.etiquetteConfigAnnuler, null)
                .create();

    }
}
