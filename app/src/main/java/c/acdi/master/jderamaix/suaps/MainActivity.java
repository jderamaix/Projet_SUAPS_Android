package c.acdi.master.jderamaix.suaps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Élément principal de l'interface
    // Adaptateur de l'affichage des étudiants présents
    private Adapter _adapter;
    private int _capacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _adapter = new Adapter(this);
        configureClass(5, new Date(1000*(20*60)));

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
        addStudent("Marcel");
        addStudent("Jeanne");
        addStudent("Martin");
        addStudent("Godot");
        addStudent("Philippe");
    }

    public void ajouterEtudiant(View view) {
        new AddDialog().show(getSupportFragmentManager(), "ajoutEtudiant");
    }

    public void addStudent(String name) {
        _adapter.addStudent(name);
        ((TextView) findViewById(R.id.affichageOccupation)).setText(
                getString(R.string.affichageOccupation, _adapter.getItemCount(), _capacity)
        );
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

    public void configurerCours(View view) {
        new ConfigDialog().show(getSupportFragmentManager(),"configClasse");
    }

    public void configureClass(int capacity, Date minimumTime) {
        _capacity = capacity;
        ((TextView) findViewById(R.id.affichageCapacite)).setText(String.valueOf(capacity));
        ((TextView) findViewById(R.id.affichageOccupation)).setText(
                getString(R.string.affichageOccupation,_adapter.getItemCount(), capacity)
        );
        ((TextView) findViewById(R.id.affichageTempsMinimum)).setText(
                DateFormat.getTimeInstance(DateFormat.SHORT, Locale.FRANCE).format(minimumTime)
        );
    }

    public void Badger(View view) {
        Toast.makeText(this, "Pas encore implantée", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, BadgeActivity.class);
        startActivity(intent);
    }
}
