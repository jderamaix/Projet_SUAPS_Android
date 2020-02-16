package com.chavau.univ_angers.univemarge.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chavau.univ_angers.univemarge.R;
import com.chavau.univ_angers.univemarge.intermediaire.MusculationData;

import java.util.ArrayList;

public class AdapterViewPager extends PagerAdapter {

    private Context context;
    private MusculationData muscuData;

    public AdapterViewPager(MusculationData muscuData, Context context) {
        this.muscuData = muscuData;
        this.context = context;
    }

    @Override
    // Creer une page pour une position donnée
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.vue_details_musculation, null);
        ImageView iv_muscu = view.findViewById(R.id.iv_occupation);
        TextView tv_details = view.findViewById(R.id.id_details);
        TextView tv_details_value = view.findViewById(R.id.id_details_value);

        if (position == 0) {
            iv_muscu.setImageResource(R.drawable.people_online);
            tv_details.setText("Occupation");
            tv_details_value.setText(muscuData.getOccupation());
            // Plus tard pour mettre à jour les presents
            tv_details_value.setTag("OcuppationValue");
        }
        else if (position == 1) {
            iv_muscu.setImageResource(R.drawable.capacite);
            tv_details.setText("Capacité");
            tv_details_value.setText(String.valueOf(muscuData.getCapacite()));
        }
        else {
            iv_muscu.setImageResource(R.drawable.temps_mini);
            tv_details.setText("Temps Minimum");
            tv_details_value.setText(muscuData.getTempsMinimum());
        }

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View)view);
    }

    @Override
    // Retourner le nombre de vues disponibles
    public int getCount() {
        return 3;
    }

    @Override
    // Determiner si la page View is associée à un objet clé spécifique retourné par "instantiacedItem(ViewGroup, int)"
    public boolean isViewFromObject(View view, Object o) {
        return view.equals(o);
    }
}
