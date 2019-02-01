package c.acdi.master.jderamaix.suaps;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

/**
 * Adaptateur pour l'affichage des étudiants présents.
 */
public class StudentViewAdapter extends RecyclerView.Adapter<StudentViewAdapter.ViewHolder> {

    /**
     * Le jeu de données des étudiants présent.
     */
    private ArrayList<StudentEntry> _dataset;
    /**
     * L'Activity, qui doit avoir la même signature que l'activité principale au cas où de la
     * communication serait nécessaire.
     * @see AddStudentDialog#onCreateDialog(Bundle)
     * @see ConfigDialog#onCreateDialog(Bundle)
     */
    private MainActivity _activity;
    /**
     * Le gonfleur, vers laquelle on garde une référence afin d'éviter d'en recréer à chaque
     * création de ViewHolder.
     */
    private LayoutInflater _inflater;

    /**
     *  Constructeur à signature complète.
     *
     * @param activity Activité principale
     * @param data Liste des étudiants présent à la séance de sport.
     */
    public StudentViewAdapter(MainActivity activity, ArrayList<StudentEntry> data) {
        _activity = activity;
        _inflater = LayoutInflater.from(_activity);
        _dataset = data;
    }

    /**
     * Constructeur à signature légère (pour l'initialisation par exemple).
     *
     * @param activity Activité servant à initialiser l'adapter
     */
    public StudentViewAdapter(MainActivity activity) {
        this(activity, new ArrayList<StudentEntry>());
    }

    /**
     * Accède directement à une entrée d'étudiant du jeu de données.
     * @param i L'indice de l'entrée d'étudiant dans le jeu de données auquel accéder.
     * @return  L'entrée d'étudiant d'indice i dans le jeu de données.
     */
    public StudentEntry get(int i){
        return _dataset.get(i);
    }

    /**
     * Obtenir la taille du jeu de données.
     * @return La taille du jeu de données.
     */
    @Override
    public int getItemCount() {
        return _dataset.size();
    }



    /**
     * La classe des instances contenant les vues vers les entrées du jeu de données.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView name, elapsedTime;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.nomEtudiant);
            elapsedTime = view.findViewById(R.id.tempsEcoule);
        }
    }

    /**
     * @see RecyclerView.Adapter#onCreateViewHolder(ViewGroup, int)
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(_inflater.inflate(R.layout.entry_student, parent, false));
    }

    /**
     * @see RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.name.setText(_dataset.get(i).name());
        String shownTime = _dataset.get(i).elapsedTime();
        {
            /*
             * Horloges temporaires pour le calcul du temps restant
             */
            Calendar studentTime = Calendar.getInstance(),
                     targetTime = Calendar.getInstance(),
                     sessionTime = Calendar.getInstance();
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.FRANCE);
                Date time = formatter.parse(shownTime);
                studentTime.setTime(time);
                Log.e("time",time.toString());
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

    /**
     * Modifie atomiquement le jeu de données.
     * @see MainActivity#ReinitialiseAffichage()
     * @param dataset ArrayList contenant l'intégralité des élèves présent à la séance
     */
    public void dataset(ArrayList<StudentEntry> dataset) {
        _dataset = dataset;
        notifyDataSetChanged();
    }

}
