package c.acdi.master.jderamaix.suaps;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.threeten.bp.Duration;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private ArrayList<StudentEntry> _dataset;
    private LayoutInflater _inflater;

    public Adapter(Context context, ArrayList<StudentEntry> data) {
        _inflater = LayoutInflater.from(context);
        _dataset = data;
    }

    public Adapter(Context context) {
        _inflater = LayoutInflater.from(context);
        _dataset = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return _dataset.size();
    }

    public void addStudent(String name) {
        //Log.i("Adapter.addStudent", name);  // debugging
        _dataset.add(new StudentEntry(name));
        notifyItemInserted(_dataset.size() - 1);
    }

    public void removeStudent(int i) {
        //Log.i("Adapter.removeStudent",name);  // debugging
        _dataset.remove(i);
        // Pour des raisons encore inconnues,
        //`notifyItemRemoved(i);`
        // donne des problèmes considérables.
        // C'est pour cela que
        //`notifyDataSetChanged();`
        // est utilisé ici.
        notifyDataSetChanged();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView _name, _elapsedTime;

        public ViewHolder(View view) {
            super(view);
            _name = view.findViewById(R.id.nomEtudiant);
            _elapsedTime = view.findViewById(R.id.tempsEcoule);
        }

        public String name() { return (String) _name.getText(); }
        public void name(String name) { _name.setText(name); }

        public Duration elapsedTime() { return Duration.parse(_elapsedTime.getText()); }
        public void elapsedTime(Duration duration) { _elapsedTime.setText(String.valueOf(duration.getSeconds())); }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(_inflater.inflate(R.layout.entry_student, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.name(_dataset.get(i).name());
        holder.itemView.setTag(i);
    }

}
