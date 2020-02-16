package com.chavau.univ_angers.univemarge.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.chavau.univ_angers.univemarge.R;
import com.chavau.univ_angers.univemarge.view.activities.SettingsActivity;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    ComponentsActivityListener callback;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preference);
        init_preferences();
    }

    /**
     * Initialisation des preferences
     */
    public void init_preferences() {
        Preference pref_sync = findPreference(getString(R.string.Synchronisation_cle));
        Preference pref_notif = findPreference(getString(R.string.Notification_cle));
        Preference pref_pin_active = findPreference(getString(R.string.Code_pin_activation_cle));
        Preference pref_pin_text = findPreference(getString(R.string.Code_Pin_EditText_cle));
        //Enregistrement des preferences dans le listener du change
        pref_sync.setOnPreferenceChangeListener(this);
        pref_notif.setOnPreferenceChangeListener(this);
        pref_pin_active.setOnPreferenceChangeListener(this);
        pref_pin_text.setOnPreferenceChangeListener(this);
        //desactiver par defaut le champ qui va garder le code pin
        pref_pin_text.setVisible(false);
    }

    // MAJ du summary à la modification
    @Override
    public boolean onPreferenceChange(Preference preference, Object object) {
        if (preference.getKey().equals(getString(R.string.Synchronisation_cle))) {
            if (((Boolean) object)) {
                Toast.makeText(getActivity(), "Activé !", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Desactivé !", Toast.LENGTH_SHORT).show();
                SwitchPreference sp_notif = (SwitchPreference) findPreference(getString(R.string.Notification_cle));
                sp_notif.setChecked(true);
            }
        } else if (preference.getKey().equals(getString(R.string.Notification_cle))) {
            return true;
        } else if (preference.getKey().equals(getString(R.string.Code_pin_activation_cle))) {
            if (((Boolean) object)) {
                callback.setVisibilityComponents(true);
            } else {
                callback.setVisibilityComponents(false);
            }
        } else if (preference.getKey().equals(getString(R.string.Code_Pin_EditText_cle))) {
            //FIXME return false or true
        } else {
            return false;
        }
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SettingsActivity) {
            this.callback = (SettingsFragment.ComponentsActivityListener) context;
        }
    }

    /**
     * Méthode permettant de changer le champ de preference du code pin
     *
     * @param newPassword
     */
    public void changePassword(String newPassword) {
        EditTextPreference et_pin = (EditTextPreference) findPreference(getString(R.string.Code_Pin_EditText_cle));
        System.out.println(("L'ancien code pin: " + et_pin.getText() + " Le nouveau code pin: " + newPassword));
        et_pin.setText(newPassword);
    }

    /**
     * L'interface qui va servir de faire la communication du fragment a son activité associé
     */
    public interface ComponentsActivityListener {
        void setVisibilityComponents(boolean isVisible);
    }


}
