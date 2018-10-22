package c.acdi.master.jderamaix.suaps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class ConfigDialog extends DialogFragment {

    private Context _context;

    public ConfigDialog() {
        super();
        _context = getActivity();
    }

    public ConfigDialog(Context context) {
        super();
        _context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View view = LayoutInflater.from(_context).inflate(R.layout.dialog_config,null);
        final View main = LayoutInflater.from(_context).inflate(R.layout.activity_main,null);

        final TimePicker duration = view.findViewById(R.id.configDuree);
        duration.setIs24HourView(true);
        duration.setCurrentHour(0);
        duration.setCurrentMinute(0);

        return new AlertDialog.Builder(_context)
                .setView(view)
                .setPositiveButton(R.string.etiquetteConfigValider, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Capacité
                        ((TextView) main.findViewById(R.id.affichageCapacite)).setText(
                                String.valueOf(((NumberPicker) view.findViewById(R.id.configCapacite)).getValue())
                        );
                        // Duree
                        Date d = new Date(1000*(duration.getCurrentHour()*3600 + duration.getCurrentMinute()*60));
                        ((TextView) main.findViewById(R.id.affichageTempsMinimum)).setText(
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
