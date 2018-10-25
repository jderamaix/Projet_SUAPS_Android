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
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOError;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static c.acdi.master.jderamaix.suaps.RFIDActivity.PUBLIC_STATIC_STRING_IDENTIFIER;

public class MainActivity extends AppCompatActivity {

    // Affichage des données sur le cours
    private int _capacity = 0;
    private String _duration = "00:00";

    public int capacity() { return _capacity; }
    public String duration() { return _duration; }

    // Élément principal de l'interface
    // Adaptateur de l'affichage des étudiants présents
    private Adapter _adapter;
    public static final int BadgeRequest = 1;
    private static final String TAG = "MainActivity";

    // 3 minutes
    private final static int INTERVAL = 60 * 3;

    private final ScheduledExecutorService organisateur = Executors.newScheduledThreadPool(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _adapter = new Adapter(this);
        configureClass(5, 1, 20);

        // Initialisation du RecyclerView
        RecyclerView view = findViewById(R.id.affichageEtudiants);
        view.setHasFixedSize(true);
        view.setAdapter(_adapter);
        view.setLayoutManager(new LinearLayoutManager(this));

        // Implémentation de la suppression par swipe
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView view, RecyclerView.ViewHolder holder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder holder, int direction) {

                int numero_id = _adapter.get((int) holder.itemView.getTag()).id();

                String numero_id_chaine = "" + numero_id;

                _adapter.removeStudent((int) holder.itemView.getTag());

                Client client = ServiceGenerator.createService(Client.class);
                Task task = new Task(numero_id_chaine);
                Call<Void> call_Post = client.EnleverPersonne("" + numero_id_chaine,task);
                call_Post.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.e("TAG", "Laréponse est véritable");
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        String smiley = new String(Character.toChars(0x1F438));
                        if(t instanceof IOException){
                            Toast.makeText(MainActivity.this, "Erreur de connexion " + smiley + ", êtes vous connecté ?", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Problème de convertion " + smiley, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                _updateAttendance();
            }
        }).attachToRecyclerView(view);

        final ScheduledFuture<?> organisateurGerant = organisateur.scheduleAtFixedRate(AppelRun,0,INTERVAL,TimeUnit.SECONDS);
    }


    final Runnable AppelRun = new Runnable(){
        public void run() {
            Reinitialise_Liste();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        Reinitialise_Liste();
    }


    @Override
    protected void onPause(){
        super.onPause();
        Toast.makeText(this, "On est sur onPause", Toast.LENGTH_SHORT).show();
        organisateur.shutdownNow();
    }


    /**
     * Méthode factorisant le code pour mettre à jour de l'affichage des présences.
     */
    private void _updateAttendance() {
        ((TextView) findViewById(R.id.affichageOccupation)).setText(
                getString(R.string.affichageOccupation, _adapter.getItemCount(), _capacity)
        );
    }

    /**
     * Méthode pour lancer le dialogue d'ajout manuel.
     * Il est un callback invoqué par le bouton R.id.ajouterEtudiant.
     */
    public void ajouterEtudiant(View view) {
        new AddDialog().show(getSupportFragmentManager(), "ajoutEtudiant");
    }

    /**
     * Méthode pour ajouter un étudiant manuellement à la séance.
     * @param name Le nom de l'étudiant à ajouter.
     */
    public void addStudent(String name) {
        Client client = ServiceGenerator.createService(Client.class);

        Task task = new Task(name);

        Call<Void> call_Post = client.EnvoieNom(task.getString(),task);


        call_Post.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, String.format("Le corps de task est : %s   ", String.valueOf(response.code())), Toast.LENGTH_SHORT).show();
                    Reinitialise_Liste();
                } else {
                    Toast.makeText(MainActivity.this, String.format("Response is %s ", String.valueOf(response.code())), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                String smiley = new String(Character.toChars(0x1F438));
                if(t instanceof IOException){
                    Toast.makeText(MainActivity.this, "Erreur de connexion " + smiley + ", êtes vous connecté ?", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Problème de convertion " + smiley, Toast.LENGTH_SHORT).show();
                }

            }
        });
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
                configurerCours(null);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Méthode pour lancer le dialogue de configuration de classe.
     */
    public void configurerCours(View view) {
        new ConfigDialog().show(getSupportFragmentManager(),"configClasse");
    }

    /**
     * Méthode permettant de changer la capacité et le temps minimum de la séance.
     * Elle est destinée à être utilisée à la place des setters déclarées en haut.
     *
     * @param capacity       La nouvelle capacité de la séance
     * @param minimumHours   Le nombre d'heures dans le nouveau temps minimum de la séance
     * @param minimumMinutes Le nombre de minutes dans le nouveau temps minimum de la séance
     */
    public void configureClass(int capacity, int minimumHours, int minimumMinutes) {
        _capacity = capacity;
        _duration = getString(R.string.affichageTemps, minimumHours, minimumMinutes);
        ((TextView) findViewById(R.id.affichageCapacite)).setText(
                getString(R.string.affichageCapacite, _capacity));
        _updateAttendance();
        ((TextView) findViewById(R.id.affichageTempsMinimum)).setText(_duration);

        // Avertir la base du changement
        ModificationCapaciteHeure();
    }

    public void Badger(View view) {
        //Toast.makeText(this, "Pas encore implantée", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, RFIDActivity.class);
        startActivityForResult(intent, BadgeRequest);
    }



    /**
     * Méthode executé lors du retour d'un résultat (réussi ou non) de startActivityForResult	(Badgeage)
     * Regarde si L'activité a été annulé puis si il y a eu un problème
     * et finalement execute les méthodes nécessaires
     *
     * @param requestCode   : code utilisé pour différencier de quelle activité vient le résultat.
     * @param resultCode    : code utilisé pour savoir comment c'est passé
     *                          l'activité donnant le résultat.
     * @param data          : Intent contenant les données renvoyé par l'activité
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == Activity.RESULT_CANCELED){
            //Toast.makeText(this,"L'activité Badger/RFID a été annulé",Toast.LENGTH_LONG).show();
            if (requestCode == BadgeRequest) {
                //Reinitialise_Liste();
            }
        } else if(resultCode == Activity.RESULT_OK) {
            if (requestCode == BadgeRequest) {
		        //Reinitialise_Liste();
            }
        }
    }



	/**
	* Méthode Vidant de la structure utilisée pour l'affichage les membres puis rajoutant
	* dedans ceux obtenues avec un GET , permet de mettre à l'heure leur temps passé dans la salle.
	*/
    public void  Reinitialise_Liste(){

        Client client = ServiceGenerator.createService(Client.class);

        Call<List<Classe>> call2 = client.RecoitPersonnes();

        call2.enqueue(new Callback<List<Classe>>() {
            @Override
            public void onResponse(Call<List<Classe>> call, Response<List<Classe>> response) {
                List<Classe> classeList = response.body();
                if(!(classeList == null)){
                    if(!(classeList.isEmpty())){
                        for(int i = 0; i < _adapter.getItemCount();){
                            _adapter.removeStudent(i);
                        }
                        Iterator<Classe> i = classeList.iterator();
                        do {
                            Classe classe = i.next();
                            _adapter.addStudent(classe.getNom(),classe.getDuree(),classe.getNo_etudiant());
                        } while (i.hasNext());
                        _updateAttendance();
                    } else {
                        Log.e("TAG","La liste est vide");
                    }
                } else {
                    Log.e("TAG","classe_list est null");
                }
            }

            @Override
            public void onFailure(Call<List<Classe>> call, Throwable t) {
                String smiley = new String(Character.toChars(0x1F438));
                if(t instanceof IOException){
                    Toast.makeText(MainActivity.this, "Erreur de connexion " + smiley + ", êtes vous connecté ?", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Problème de convertion " + smiley, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /**
     * Méthode pour avertir la base de données du changement des paramètres de la séance
     */
    public void ModificationCapaciteHeure() {
        String capacity = getString(R.string.affichageCapacite, _capacity);
        String temps = _duration;

        AuaListeSeance auaListeSeance = new AuaListeSeance();

        auaListeSeance.setLimitePersonnes(capacity);
        auaListeSeance.setTempsSeance(temps);
        auaListeSeance.setIdSeance(1);

        Client client = ServiceGenerator.createService(Client.class);

        Call<Void> call_Post = client.EnvoieTempsCapacite(capacity + "/" + temps + "/1", auaListeSeance);

        call_Post.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.e("Reponse recu", "reponse recu");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                String smiley = new String(Character.toChars(0x1F438));
                if(t instanceof IOException){
                    Toast.makeText(MainActivity.this, "Erreur de connexion " + smiley + ", êtes vous connecté ?", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Problème de convertion " + smiley, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
