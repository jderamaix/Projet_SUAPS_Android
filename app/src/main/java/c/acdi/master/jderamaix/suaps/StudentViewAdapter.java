package c.acdi.master.jderamaix.suaps;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StudentViewAdapter extends RecyclerView.Adapter<StudentViewAdapter.ViewHolder> {

    private ArrayList<StudentEntry> _dataset;
    private MainActivity _activity;
    private LayoutInflater _inflater;

    public StudentViewAdapter(MainActivity activity, ArrayList<StudentEntry> data) {
        _activity = activity;
        _inflater = LayoutInflater.from(_activity);
        _dataset = data;
    }

    public StudentViewAdapter(MainActivity activity) {
        this(activity, new ArrayList<StudentEntry>());
    }

    /**
     * Accès direct à un élément du jeu de données.
     * @param i L'indice de l'élément dans le jeu de données auquel accéder.
     * @return  L'élément d'indice i dans le jeu de données.
     */
    public StudentEntry get(int i){
        return _dataset.get(i);
    }

    @Override
    public int getItemCount() {
        return _dataset.size();
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
        String shownTime = _dataset.get(i).elapsedTime();
        {
            // Horloges temporaires pour le calcul du temps restant
            Calendar studentTime = Calendar.getInstance(),
                     targetTime = Calendar.getInstance(),
                     sessionTime = Calendar.getInstance();
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.FRANCE);
                Date time = formatter.parse(shownTime);
                studentTime.setTime(time);
                targetTime.setTime(time);
                targetTime.add(Calendar.MINUTE, 15);
                sessionTime.setTime(formatter.parse(_activity.duration()));
            } catch (ParseException e) {
                throw new RuntimeException(e.getMessage());
            }
            Resources resources = _activity.getResources();
            if (!studentTime.before(sessionTime)) {
                holder.itemView.setBackgroundColor(resources.getColor(R.color.couleurEntreeTempsDepasse));
            } else if (!targetTime.before(sessionTime)) {
                holder.itemView.setBackgroundColor(resources.getColor(R.color.couleurEntreeTempsLimite));
            } else {
                holder.itemView.setBackgroundColor(resources.getColor(R.color.couleurEntreeTempsBon));
            }
        }
        holder.elapsedTime.setText(shownTime);
        holder.itemView.setTag(i);
    }

    public ArrayList<StudentEntry> dataset() { return _dataset; }
    public void dataset(ArrayList<StudentEntry> dataset) {
        _dataset = dataset;
        notifyDataSetChanged();
    }

}
