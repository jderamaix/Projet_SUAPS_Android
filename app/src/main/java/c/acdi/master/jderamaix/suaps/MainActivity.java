package c.acdi.master.jderamaix.suaps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOError;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static c.acdi.master.jderamaix.suaps.RFIDActivity.PUBLIC_STATIC_STRING_IDENTIFIER;

public class MainActivity extends AppCompatActivity {

    // Élément principal de l'interface
    // Adaptateur de l'affichage des étudiants présents
    private Adapter _adapter;
    public static final int BadgeRequest = 1;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _adapter = new Adapter(this);

        // Initialisation du RecyclerView
        RecyclerView view = (RecyclerView) findViewById(R.id.affichageEtudiants);
        view.setHasFixedSize(true);
        view.setAdapter(_adapter);
        view.setLayoutManager(new LinearLayoutManager(this));

        // Implémentation de la suppression par swipe
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                _adapter.removeStudent((int) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(view);

        // Test
        _adapter.addStudent("Marcel");
        _adapter.addStudent("Jeanne");
        _adapter.addStudent("Martin");
        _adapter.addStudent("Godot");
        _adapter.addStudent("Philippe");
        _adapter.addStudent(new String(Character.toChars(0x1F60B)));
        _adapter.addStudent(new String(Character.toChars(0x1F44C)));
        _adapter.addStudent(new String(Character.toChars(0x1F438)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.config_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.configurerCours:
                configClass(null);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addStudent(View view) {
        new AddDialog(this, _adapter).show(getSupportFragmentManager(), "ajoutEtudiant");
    }

    public void configClass(View view) {
        new ConfigDialog(this).show(getSupportFragmentManager(),"configClasse");
    }

    public void Badger(View view) {
        //Toast.makeText(this, "Pas encore implantée", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, RFIDActivity.class);
        startActivityForResult(intent, BadgeRequest);
    }



    /**
     *Méthode executé lors du retour d'un résultat (réussi ou non) de startActivityForResult	(Badgeage)
     *  Regarde si L'activité a été annulé puis si il y a eu un problème
     *  et finalement execute les méthodes nécessaires
     *
     * @param requestCode   : code utilisé pour différencier de quelle activité vient le résultat.
     *        resultCode    : code utilisé pour savoir comment c'est passé
     *                          l'activité donnant le résultat.
     *        data          : Intent contenant les données renvoyé par l'activité
     *
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == Activity.RESULT_CANCELED){
            //Toast.makeText(this,"L'activité Badger/RFID a été annulé",Toast.LENGTH_LONG).show();
            if (requestCode == BadgeRequest) {
                Reinitialise_Liste();
            }
        } else if(resultCode == Activity.RESULT_OK) {
            if (requestCode == BadgeRequest) {
		        Reinitialise_Liste();
            }
        }
    }



	/**
	*Méthode Vidant de la structure utilisée pour l'affichage les membres puis rajoutant 
	*dedans ceux obtenues avec un GET , permet de mettre à l'heure leur temps passé dans la salle.
	*/
    public void  Reinitialise_Liste(){

        Client client = ServiceGenerator.createService(Client.class);

        Call<List<Classe>> call2 = client.RecoitPersonnes();

        for(int i = 0; i < _adapter.getItemCount();){
            _adapter.removeStudent(i);
        }

        call2.enqueue(new Callback<List<Classe>>() {
            @Override
            public void onResponse(Call<List<Classe>> call, Response<List<Classe>> response) {
                List<Classe> classeList = response.body();
                if(!(classeList == null)){
                    if(!(classeList.isEmpty())){
                            Iterator<Classe> i = classeList.iterator();
                            if(!i.hasNext()){
                                Classe classe = i.next();
                                String nomEtud = classe.getNom();
                                _adapter.addStudent(nomEtud);
                            } else {
                                while (i.hasNext()) {
                                    Classe classe = i.next();
                                    String nomEtud = classe.getNom();
                                    _adapter.addStudent(nomEtud);
                               }
                            }
                            Toast.makeText(MainActivity.this, "Ajout des personnes", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("TAG","La liste est vide");
                    }
                } else {
                    Log.e("TAG","classe_list est null");
                }
            }

            @Override
            public void onFailure(Call<List<Classe>> call, Throwable t) {
                Log.e("échec de call"," echec de call");
                Toast.makeText(MainActivity.this, "Le call a échoué. ", Toast.LENGTH_LONG).show();
            }
        });
	}


    public void ModificationCapaciteHeure() {
        Toast.makeText(MainActivity.this,"Une modification de la capacité/heure a été reperé.", Toast.LENGTH_SHORT).show();

        String capacity = "" + R.string.affichageCapacite;
        String temps = "" + R.string.affichageTempsMinimum;

        AuaListeSeance auaListeSeance = new AuaListeSeance();

        auaListeSeance.setLimitePersonnes(capacity);
        auaListeSeance.setTempsSeance(temps);
        auaListeSeance.setIdSeance(1);

        Client client = ServiceGenerator.createService(Client.class);

        Call<Void> call_Post = client.EnvoieTempsCapacite(capacity + "/" + temps + "/1", auaListeSeance);


        Log.e(TAG, "On est juste avant le enqueue");

        call_Post.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.e("Reponse recu", "reponse recu");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(MainActivity.this, "This is an actual network failure :(, do what is needed to inform those it concern", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Conversion issue :( BIG BIG problem", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.e(TAG, "On est juste après le enqueue");

    }
}
