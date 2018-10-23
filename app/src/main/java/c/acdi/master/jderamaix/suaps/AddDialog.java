package c.acdi.master.jderamaix.suaps;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                        String nom_etudiant = ((EditText) view.findViewById(R.id.saisieNomEtudiant)).getText().toString();
                        _adapter.addStudent(nom_etudiant);
                        Log.e("TAGPRES","DOITETREAPRES");

                        Task task = new Task(nom_etudiant);

                        Client client = ServiceGenerator.createService(Client.class);

                        Call<Void> call_Post = client.EnvoieNom(nom_etudiant,task);


                        call_Post.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Log.e("TAG", "Laréponse est véritable");
                                }
                            }
                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                                if(t instanceof IOException){
                                    Toast.makeText(_context, "This is an actual network failure :(, do what is needed to inform those it concern", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(_context, "Conversion issue :( BIG BIG problem", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

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
