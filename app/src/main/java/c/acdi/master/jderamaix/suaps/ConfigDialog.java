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

        return new AlertDialog.Builder(_context)
                .setView(view)
                .setPositiveButton(R.string.etiquetteConfigValider, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Capacité
                        ((TextView) view.findViewById(R.id.affichageCapacite)).setText(
                                ((NumberPicker) view.findViewById(R.id.configCapacite)).getValue()
                        );
                        // Duree
                        TimePicker picker = (TimePicker) view.findViewById(R.id.configDuree);
                        ((TextView) view.findViewById(R.id.affichageTempsMinimum)).setText(
                                DateFormat.getTimeInstance(DateFormat.SHORT, Locale.FRANCE).format(new Date(0))
                        );
                    }
                })
                .setNegativeButton(R.string.etiquetteConfigAnnuler, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();

    }
}
