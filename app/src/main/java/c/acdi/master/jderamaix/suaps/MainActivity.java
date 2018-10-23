package c.acdi.master.jderamaix.suaps;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
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

    public static final int BadgeRequest = 1;
    private static final String TAG = "MainActivity";
    private RecyclerView _view;
    private Adapter _adapter;

    private ArrayList<Classe> liste_Ultime;


    public ArrayList<Classe> getListe_ultime() {
        return liste_Ultime;
    }

    public void setListe_Ultime(ArrayList<Classe> liste_Ultime){
        this.liste_Ultime = liste_Ultime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setListe_Ultime(new ArrayList<Classe>());

        _adapter = new Adapter(this);

        _view = (RecyclerView) findViewById(R.id.affichageEtudiants);
        _view.setHasFixedSize(true);
        _view.setAdapter(_adapter);
        _view.setLayoutManager(new LinearLayoutManager(this));

        // Implémentation de la suppression par swipe
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int index = (int) viewHolder.itemView.getTag();

                _adapter.removeStudent(index);
                Log.e("TAG5","" + index +"    " + MainActivity.this.getListe_ultime().size());
                Log.e("TAG",((MainActivity.this.getListe_ultime()).get(index)).getNom());
                MainActivity.this.getListe_ultime().remove(index);
                //Log.e("TAGULT", ((MainActivity.this.getListe_ultime().get(index)).getNom()));
            }
        }).attachToRecyclerView(_view);


        // Test
        _adapter.addStudent(new String(Character.toChars(0x1F60B)));
        _adapter.addStudent(new String(Character.toChars(0x1F44C)));
        _adapter.addStudent(new String(Character.toChars(0x1F438)));
        _adapter.addStudent( new String(Character.toChars(0x1F438)));
        _adapter.addStudent("Philippe");
    }

    public void addStudent(View view) {
        int sizeAvant = _adapter.getItemCount();
        AddDialog adddialog = new AddDialog(this, _adapter);
                adddialog.show(getSupportFragmentManager(), "ajoutEtudiant");
        Log.e("TAGAPRES"," APRES");

    }


    public void configClass(View view) {
        new ConfigDialog(this).show(getSupportFragmentManager(),"configClasse");
    }

    public void Badger(View view) {
        //Intent intent = new Intent(this, BadgeActivity.class);
        Intent intent = new Intent(this, RFIDActivity.class);
        startActivityForResult(intent,BadgeRequest);
    }



    /**
     *Méthode executé lors du retour d'un résultat (réussi ou non) de startActivityForResult
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
            Toast.makeText(this,"L'activité Badger/RFID a été annulé",Toast.LENGTH_LONG).show();

            Lancement_Num_Carte("1");


        } else if(resultCode == Activity.RESULT_OK) {
            if (requestCode == BadgeRequest) {
                ArrayList<String> donnees = data.getStringArrayListExtra(PUBLIC_STATIC_STRING_IDENTIFIER);



                Log.e("Identifiant carte étud", "On a fini l'activité secondaire");
                Lancement_Num_Carte("1");
            }
        }
    }

    /**
     * Méthode Envoyant le numéro de carte à la base de donnée et demandant la nouvelle liste
     *  de personnes badgés pour l'actualisée.
     *
     * @param num_Carte : Le numéro de la carte a envoyé à la base de donnée
     */

    public void  Lancement_Num_Carte(String num_Carte){

        //Task task = new Task(num_Carte);

        Client client = ServiceGenerator.createService(Client.class);

/*
        Call<Void> call_Post = client.EnvoieNumCarte("badgeage/" + num_Carte,task);


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
                    Toast.makeText(MainActivity.this, "This is an actual network failure :(, do what is needed to inform those it concern", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Conversion issue :( BIG BIG problem", Toast.LENGTH_SHORT).show();
                }

            }
        });


        Log.e("Post passé"," Post passé");

        //_adapter.addStudent(num_Carte);

*/
        Call<List<Classe>> call2 = client.Methode2();

        for(int i = 0; i < _adapter.getItemCount();){
            _adapter.removeStudent(i);
        }

        for(int i = 0; i < this.getListe_ultime().size();){
            this.getListe_ultime().remove(i);
        }

        call2.enqueue(new Callback<List<Classe>>() {
            @Override
            public void onResponse(Call<List<Classe>> call, Response<List<Classe>> response) {
                List<Classe> classeList = response.body();
                if(!(classeList == null)){
                    if(!(classeList.isEmpty())){
                        if(classeList.size() > 0){
                            Iterator<Classe> i = classeList.iterator();
                            if(!i.hasNext()){
                                Classe classe = i.next();
                                String nomEtud = classe.getNom();
                                _adapter.addStudent(nomEtud);
                                MainActivity.this.getListe_ultime().add(classe);
                            } else {
                                while (i.hasNext()) {
                                    Classe classe = i.next();
                                    String nomEtud = classe.getNom();
                                    _adapter.addStudent(nomEtud);
                                    MainActivity.this.getListe_ultime().add(classe);
                                    //Log.e("TAG",classe.getTemps());
/*
                                    if (classe.getNo_etudiant() != null) {
                                        _adapter.addStudent(classe.getNo_etudiant());
                                    }
*/                                }
                            }
                            Toast.makeText(MainActivity.this, "Ajout des personnes", Toast.LENGTH_SHORT).show();
                        }
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

    public void ModificationCapacite(View view) {
        //Toast.makeText(MainActivity.this,"Une modification de la capacité a été reperé.", Toast.LENGTH_SHORT).show();


        EditText et_capacity = this.findViewById(R.id.affichageCapacite);
        EditText et_temps = this.findViewById(R.id.affichageTempsMinimum);

        String capacity = et_capacity.getText().toString();
        String temps = et_temps.getText().toString();

        AuaListeSeance auaListeSeance = new AuaListeSeance();

        auaListeSeance.setLimitePersonnes(capacity);
        auaListeSeance.setTempsSeance(temps);
        auaListeSeance.setIdSeance(1);

        Client client = ServiceGenerator.createService(Client.class);

        Call<Void> call_Post = client.EnvoieTempsCapacite(capacity + "/" + temps + "/1",auaListeSeance);


        Log.e(TAG,"On est juste avant le enqueue");

        call_Post.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.e("Reponse recu","reponse recu");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if(t instanceof IOException){
                    Toast.makeText(MainActivity.this, "This is an actual network failure :(, do what is needed to inform those it concern", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Conversion issue :( BIG BIG problem", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.e(TAG,"On est juste après le enqueue");

    }

    public void ModificationHeure(View view) {
        Toast.makeText(MainActivity.this,"Une modification de l'heure a été reperé.", Toast.LENGTH_SHORT).show();



        EditText et_capacity = this.findViewById(R.id.affichageCapacite);
        EditText et_temps = this.findViewById(R.id.affichageTempsMinimum);

        String capacity = et_capacity.getText().toString();
        String temps = et_temps.getText().toString();

        AuaListeSeance auaListeSeance = new AuaListeSeance();

        auaListeSeance.setLimitePersonnes(capacity);
        auaListeSeance.setTempsSeance(temps);
        auaListeSeance.setIdSeance(1);

        Client client = ServiceGenerator.createService(Client.class);

        Call<Void> call_Post = client.EnvoieTempsCapacite(capacity + "/" + temps + "/1",auaListeSeance);

        Log.e(TAG,"On est juste avant le enqueue");



        call_Post.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.e("Reponse recu","reponse recu");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if(t instanceof IOException){
                    Toast.makeText(MainActivity.this, "This is an actual network failure :(, do what is needed to inform those it concern", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Conversion issue :( BIG BIG problem", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.e(TAG,"On est juste après le enqueue");


    }
}
