package c.acdi.master.jderamaix.suaps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Locale;

public class ConfigDialog extends DialogFragment {

    public ConfigDialog() {
        super();
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final MainActivity activity = (MainActivity) getActivity();
        final View view = LayoutInflater.from(activity).inflate(R.layout.dialog_config,null);

        // Initialiser la sélection de la capacité à la valeur actuelle
        final NumberPicker capacity = view.findViewById(R.id.configCapacite);
        capacity.setMinValue(1);
        capacity.setMaxValue(50);
        capacity.setValue(activity.capacity());

        // Initialiser la sélection du temps minimum
        final TimePicker duration = view.findViewById(R.id.configDuree);
        duration.setIs24HourView(true);
        Calendar source = Calendar.getInstance(Locale.FRANCE);
        source.setTime(activity.duration());
        duration.setCurrentHour(source.get(Calendar.HOUR_OF_DAY));
        duration.setCurrentMinute(source.get(Calendar.MINUTE));

        return new AlertDialog.Builder(activity)
                .setView(view)
                .setPositiveButton(R.string.etiquetteConfigValider, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.configureClass(capacity.getValue(), duration.getCurrentHour(), duration.getCurrentMinute());
                    }
                })
                .setNegativeButton(R.string.etiquetteConfigAnnuler, null)
                .create();

    }
}
