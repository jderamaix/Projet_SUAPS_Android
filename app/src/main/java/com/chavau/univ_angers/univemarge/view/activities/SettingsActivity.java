package com.chavau.univ_angers.univemarge.view.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chavau.univ_angers.univemarge.R;
import com.chavau.univ_angers.univemarge.view.fragments.SettingsFragment;

public class SettingsActivity extends AppCompatActivity implements SettingsFragment.ComponentsActivityListener {

    EditText et_new_pin;
    Button btn_change_pin;
    SettingsFragment fragmentPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initChangePasswordsComponents();
        fragmentPref = (SettingsFragment) getSupportFragmentManager().findFragmentById(R.id.frag_setting);

        /**
         *Code generant la fleche qui permet de revenir à l'activité principale
         */
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    /**
     * Methode permet suite a un click de boutton 'changer' de changer le code pin
     *
     * @param v
     */
    public void onClickChanger(View v) {
        String newPassSaisi = et_new_pin.getText().toString();
        if (newPassSaisi.isEmpty()) {
            dialog_msg("Veuillez remplir le nouveau code pin !");
        } else {
            if (newPassSaisi.length() != 4) {
                dialog_msg("Votre nouveau code pin doit avoir 4 numéros !");
            } else {
                fragmentPref.changePassword(et_new_pin.getText().toString());
                dialog_msg("Code pin modifié avec succès !");
            }
        }
    }

    /**
     * Methode qui permet d'afficher un dialogue avec le messgae passé en parametre
     *
     * @param msg
     */
    public void dialog_msg(String msg) {
        new AlertDialog.Builder(this)
                .setTitle(msg)
                .setPositiveButton("DONE", null)
                .create().show();
    }

    /**
     * Methode permet suivant un boolean d'afficher ou non les composants liés au traitement de changement de mot de passe.
     *
     * @param isVisible
     */
    @Override
    public void setVisibilityComponents(boolean isVisible) {
        if (isVisible) {
            et_new_pin.setVisibility(View.VISIBLE);
            btn_change_pin.setVisibility(View.VISIBLE);
        } else {
            et_new_pin.setVisibility(View.GONE);
            btn_change_pin.setVisibility(View.GONE);
        }
    }

    /**
     * Methode permet d'initialiser les composantes de traitements du code pin
     */
    public void initChangePasswordsComponents() {
        et_new_pin = findViewById(R.id.edtnewpin);
        btn_change_pin = findViewById(R.id.btnchange);
    }


}

