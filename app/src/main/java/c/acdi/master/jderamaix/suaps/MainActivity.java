package c.acdi.master.jderamaix.suaps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

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
    }
}
