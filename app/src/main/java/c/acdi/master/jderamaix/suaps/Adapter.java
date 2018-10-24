package c.acdi.master.jderamaix.suaps;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private ArrayList<StudentEntry> _dataset;
    private MainActivity _activity;
    private LayoutInflater _inflater;

    public Adapter(MainActivity activity, ArrayList<StudentEntry> data) {
        _activity = activity;
        _inflater = LayoutInflater.from(_activity);
        _dataset = data;
    }

    public Adapter(MainActivity activity) {
        _activity = activity;
        _inflater = LayoutInflater.from(_activity);
        _dataset = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return _dataset.size();
    }

    public void addStudent(String name) {
        _dataset.add(new StudentEntry(name));
        notifyItemInserted(_dataset.size() - 1);
    }

    public void addStudent(String name, String duree, int id) {
        _dataset.add(new StudentEntry(name,duree,id));
        notifyItemInserted(_dataset.size() - 1);
    }


    public void removeStudent(int i) {
        _dataset.remove(i);
        // Pour des raisons encore inconnues,
        //`notifyItemRemoved(i);`
        // donne des problèmes considérables.
        // C'est pour cela que
        //`notifyDataSetChanged();`
        // est utilisé ici
        notifyDataSetChanged();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView name, elapsedTime;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.nomEtudiant);
            elapsedTime = view.findViewById(R.id.tempsEcoule);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(_inflater.inflate(R.layout.entry_student, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.name.setText(_dataset.get(i).name());
        holder.elapsedTime.setText(_activity.getString(R.string.affichageTempsEcoule, StudentEntry.calculateTimeOffset(0,0)));
        holder.itemView.setTag(i);
    }

}
