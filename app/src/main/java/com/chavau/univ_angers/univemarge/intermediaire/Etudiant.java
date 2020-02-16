package com.chavau.univ_angers.univemarge.intermediaire;

import android.os.Parcel;
import android.os.Parcelable;

import com.chavau.univ_angers.univemarge.R;
import com.chavau.univ_angers.univemarge.database.entities.StatutPresence;

import java.util.ArrayList;

public class Etudiant implements Parcelable {
    private String _nom;
    private String _prenom;
    private String _numEtud;
    private String _typeActivite;
    private int _imageId;

    public static enum STATUE_ETUDIANT {PRESENT, ABSENT, RETARD, EXCUSE};
    private STATUE_ETUDIANT _etat;

    public static ArrayList<Etudiant> creerEtudiants () {

        ArrayList<Etudiant> etu = new ArrayList<>();

        etu.add(new Etudiant("André", "Bertrand", "0", "Loisirs", R.drawable.man));
        etu.add(new Etudiant("Le Quec", "Vincent", "1", "Loisirs", R.drawable.man));
        etu.add(new Etudiant("Bonnet", "Chevaliere", "2", "Loisirs", R.drawable.woman));
        etu.add(new Etudiant("Paul", "Durand", "3", "Loisirs", R.drawable.man));
        etu.add(new Etudiant("Faure", "Blanche", "4", "Loisirs", R.drawable.woman));
        etu.add(new Etudiant("Fontaine", "Dumont", "5", "Loisirs", R.drawable.man));
        etu.add(new Etudiant("Fournier", "Dupont", "6", "Loisirs", R.drawable.man));
        etu.add(new Etudiant("Francois", "Durand", "7", "Loisirs", R.drawable.man));
        etu.add(new Etudiant("François", "Blanc", "8", "Loisirs", R.drawable.man));
        etu.add(new Etudiant("Garnier", "Fontaine", "9", "Loisirs", R.drawable.woman));
        etu.add(new Etudiant("Girard", "Fournier", "10", "Loisirs", R.drawable.man));
        etu.add(new Etudiant("Henry", "Francois", "11", "Loisirs", R.drawable.man));
        etu.add(new Etudiant("Jean", "Garnier", "12", "Loisirs", R.drawable.man));
        etu.add(new Etudiant("Lambert", "Gauthier", "13", "Loisirs", R.drawable.man));
        etu.add(new Etudiant("Pierre", "Bernard", "14", "Loisirs", R.drawable.man));

        // trier les étudiant(e)s de la liste
       /* etu.sort(new Comparator<Etudiant>() {
            @Override
            public int compare(Etudiant e1, Etudiant e2) {
                return (e1.get_nom().compareTo(e2.get_nom()));
            }
        });*/

        return etu;
    }

    public Etudiant (String _nom, String _prenom, String _numEtud, String _typeActivite, int _imageId) {
        this._nom = _nom;
        this._prenom = _prenom;
        this._numEtud = _numEtud;
        this._typeActivite = _typeActivite;
        this._imageId = _imageId;
        _etat = STATUE_ETUDIANT.ABSENT;
    }

    public String get_nom() {
        return _nom;
    }

    public String get_prenom() {
        return _prenom;
    }

    public String get_numEtud() {
        return _numEtud;
    }

    public int get_imageId() {
        return _imageId;
    }

    public String get_typeActivite() {
        return _typeActivite;
    }

    public void set_etat(STATUE_ETUDIANT etat) {
        this._etat = etat;
    }

    public STATUE_ETUDIANT get_etat() {
        return _etat;
    }



    /**************************PARCELABLE**************************/


    public static final  Creator<Etudiant> CREATOR = new Creator<Etudiant>() {

        @Override
        public Etudiant createFromParcel(Parcel parcel) {
            return new Etudiant(parcel);
        }

        @Override
        public Etudiant[] newArray(int i) {
            return new Etudiant[i];
        }
    };

    public Etudiant(Parcel parcel) {
        _nom = parcel.readString();
        _prenom = parcel.readString();
        _numEtud = parcel.readString();
        _typeActivite = parcel.readString();
        _imageId = parcel.readInt();
        _etat = STATUE_ETUDIANT.values()[parcel.readInt()];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(get_nom());
        parcel.writeString(get_prenom());
        parcel.writeString(get_numEtud());
        parcel.writeString(get_typeActivite());
        parcel.writeInt(get_imageId());
        parcel.writeInt(_etat.ordinal());
    }
}
