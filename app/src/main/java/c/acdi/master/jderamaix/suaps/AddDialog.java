package c.acdi.master.jderamaix.suaps;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class AddDialog extends DialogFragment {

    // Code de la classe en soi-même

    private Context _context;
    private Adapter _adapter;

    public AddDialog(Context context, Adapter adapter) {
        _context = context;
        _adapter = adapter;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View view = LayoutInflater.from(_context).inflate(R.layout.dialog_add, null);

        return new AlertDialog.Builder(_context)
                .setView(view)
                .setPositiveButton(R.string.etiquetteAjoutAjouter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        _adapter.addStudent(((EditText) view.findViewById(R.id.saisieNomEtudiant)).getText().toString());
                    }
                })
                .setNegativeButton(R.string.etiquetteAjoutAnnuler, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        // à compléter
                    }
                })
                .create();
    }

}
