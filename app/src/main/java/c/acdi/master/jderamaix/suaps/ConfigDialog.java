package c.acdi.master.jderamaix.suaps;

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

import java.util.Date;

public class ConfigDialog extends DialogFragment {

    public ConfigDialog() {
        super();
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final MainActivity activity = (MainActivity) getActivity();
        final View view = LayoutInflater.from(activity).inflate(R.layout.dialog_config,null);

        final NumberPicker capacity = view.findViewById(R.id.configCapacite);
        capacity.setMaxValue(20);

        final TimePicker duration = view.findViewById(R.id.configDuree);
        duration.setIs24HourView(true);
        duration.setCurrentHour(0);
        duration.setCurrentMinute(0);

        return new AlertDialog.Builder(activity)
                .setView(view)
                .setPositiveButton(R.string.etiquetteConfigValider, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.configureClass(
                                capacity.getValue(),
                                new Date(1000*((duration.getCurrentHour() - 1)*3600 + duration.getCurrentMinute()*60))
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
