package c.acdi.master.jderamaix.suaps;

import android.app.Activity;
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
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class ConfigDialog extends DialogFragment {

    // C'est un `Activity` et non pas un `Context`
    // pour une raison qui est détaillée plus bas
    private Activity _activity;

    public ConfigDialog() {
        super();
        _activity = getActivity();
    }

    public ConfigDialog(Activity activity) {
        super();
        _activity = activity;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View view = LayoutInflater.from(_activity).inflate(R.layout.dialog_config,null);

        final NumberPicker capacity = view.findViewById(R.id.configCapacite);
        capacity.setMaxValue(20);

        final TimePicker duration = view.findViewById(R.id.configDuree);
        duration.setIs24HourView(true);
        duration.setCurrentHour(0);
        duration.setCurrentMinute(0);

        return new AlertDialog.Builder(_activity)
                .setView(view)
                .setPositiveButton(R.string.etiquetteConfigValider, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Ici, on doit utiliser un `Activity` pour que
                        // `findViewById` retourne la vue associée avec
                        // l'élément en lui-même, et non pas une copie
                        // que créerait `LayoutInflater.from(_context).inflate(...)`

                        // Capacité
                        ((TextView) _activity.findViewById(R.id.affichageCapacite)).setText(
                                String.valueOf(capacity.getValue())
                        );
                        // Durée
                        Date d = new Date(1000*((duration.getCurrentHour() - 1)*3600 + duration.getCurrentMinute()*60));
                        ((TextView) _activity.findViewById(R.id.affichageTempsMinimum)).setText(
                                DateFormat.getTimeInstance(DateFormat.SHORT, Locale.FRANCE).format(d)
                        );
                    }
                })
                .setNegativeButton(R.string.etiquetteConfigAnnuler, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                })
                .create();

    }
}
