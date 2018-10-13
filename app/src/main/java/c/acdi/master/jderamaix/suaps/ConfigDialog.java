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

public class ConfigDialog extends DialogFragment {

    private Context _context;

    public ConfigDialog(Context context) {
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
                        ((TextView) view.findViewById(R.id.affichageCapacite)).setText(
                                ((NumberPicker) view.findViewById(R.id.configCapacite)).getValue()
                        );
                        ((TextView) view.findViewById(R.id.affichageTempsMinimum)).setText(
                                ((NumberPicker) view.findViewById(R.id.configDureeHeures)).getValue()
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
