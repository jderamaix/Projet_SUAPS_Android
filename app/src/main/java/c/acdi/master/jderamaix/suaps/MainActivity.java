package c.acdi.master.jderamaix.suaps;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static c.acdi.master.jderamaix.suaps.RFIDActivity.PUBLIC_STATIC_STRING_IDENTIFIER;

public class MainActivity extends AppCompatActivity {

    public static final int BadgeRequest = 1;
    private RecyclerView _view;
    private Adapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                _adapter.removeStudent((int) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(_view);

        // Test
        _adapter.addStudent("Marcel");
        _adapter.addStudent("Jeanne");
        _adapter.addStudent("Martin");
        _adapter.addStudent("Godot");
        _adapter.addStudent("Philippe");
    }

    public void addStudent(View view) {
        new AddDialog(this, _adapter).show(getSupportFragmentManager(), "ajoutEtudiant");
    }

    public void configClass(View view) {
        new ConfigDialog(this).show(getSupportFragmentManager(),"configClasse");
    }

    public void Badger(View view) {
        Toast.makeText(this, "Pas encore implantée", Toast.LENGTH_SHORT).show();
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
        } else if(resultCode == Activity.RESULT_OK) {
            if (requestCode == BadgeRequest) {
                String data_num_carte_Etudiant = data.getStringExtra(PUBLIC_STATIC_STRING_IDENTIFIER);
                Log.e("Identifiant carte étud", data_num_carte_Etudiant);
                Lancement_Num_Carte(data_num_carte_Etudiant);
            }
        }
    }

    /**
     * Méthode Envoyant le numéro de carte à la base de donnée et demandant la nouvelle liste
     *  de personnes badgés pour l'actualisée.
     *
     * @param num_Carte : Le numéro de la carte a envoyé à la base de donnée
     */
    public void Lancement_Num_Carte(String num_Carte){
        Log.e("Ajout étudiant","On ajoute l'étudiant");

        Task task = new Task(num_Carte);

        Client client = ServiceGenerator.createService(Client.class);

        /*
        Call<Task> call_Post = client.EnvoieNumCarte("badgeage/",task);


        call_Post.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                Log.e("Reponse recu","reponse recu");
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Log.e("échec de call"," echec de call");
                Toast.makeText(MainActivity.this, "Le call a échoué. ", Toast.LENGTH_LONG).show();

            }
        });*/



        Log.e("Post passé"," Post passé");

        //_adapter.addStudent(num_Carte);

        Call<List<Classe>> call2 = client.Methode2("042d87ca253980");

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
                            } else {
                                while (i.hasNext()) {
                                    Classe classe = i.next();
                                    String nomEtud = classe.getNom();
                                    _adapter.addStudent(nomEtud);
                                    if (classe.getNo_individu() != null) {
                                        _adapter.addStudent(classe.getNo_individu());
                                    }
                                }
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
}
