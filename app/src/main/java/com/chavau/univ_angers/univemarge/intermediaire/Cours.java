package com.chavau.univ_angers.univemarge.intermediaire;

import java.util.ArrayList;
import java.util.Random;

public class Cours {
    private String _intitule;
    private String _date;
    private String _jourDeLaSemaine;
    private String _heureDebut;
    private String _heureFin;
    private ArrayList<Etudiant> _listeEtudiantInscrit;



    public Cours(String i, String d, String jds, String hd, String hf, ArrayList<Etudiant> et) {
        this._intitule = i;
        this._date = d;
        this._jourDeLaSemaine = jds;
        this._heureDebut = hd;
        this._heureFin = hf;
        this._listeEtudiantInscrit = et;
    }

    public String get_date() {
        return _date;
    }

    public String get_heureDebut() {
        return _heureDebut;
    }

    public String get_heureFin() {
        return _heureFin;
    }

    public String get_intitule() {
        return _intitule;
    }

    public String get_jourDeLaSemaine() {
        return _jourDeLaSemaine;
    }

    public ArrayList<Etudiant> get_listeEtudiantInscrit() {
        return _listeEtudiantInscrit;
    }

    public static ArrayList<Cours> creeCours() {
        ArrayList<com.chavau.univ_angers.univemarge.intermediaire.Cours> cours = new ArrayList<>();

        String nomCours[] = {"Musculation","Logique et techniques de preuve", "Programmation avancée en C++",
                "Analyse et conception de systèmes d'information", "Introduction à la programmation",
                "Pratique du génie logiciel", "Réseaux pour ingénieurs", "Mathématiques pour informaticien",
                "Informatique théorique", "Développement d'applications Web", "Introduction à la robotique mobile",
                "Architecture logicielle", "Bases de données avancées", "Assurance qualité du logiciel",
                "Réseaux mobiles", "Projet expérimental", "Traitement automatique de la langue naturelle",
                "Proposition de projet de thèse", "Examen de connaissances fondamentales", "Projet expérimental",
                "Sécurité et méthodes formelles", "Interface personne-machine"};
        String joursSemaine[] = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"};
        String heuresDebut[] = {"8h00", "8h30", "9h00", "9h30", "10h00", "11h00", "13h30", "14h00", "15h20", "16h00", "17h30", "18h00", "19h10", "20h30"};
        String heureFin[] = {"11h00", "10h30", "12h00", "11h00", "11h00", "12h20", "17h30", "15h40", "18h20", "20h00", "21h00", "19h00", "22h10", "23h00"};
        String dates[] = {"9/10/2019", "12/10/2019", "25/10/2019", "2/11/2019", "16/11/2019", "11/11/2019", "24/11/2019", "6/12/2019", "7/12/2019", "11/12/2019",
                "9/01/2020", "12/01/2020", "25/01/2020", "2/02/2020", "16/02/2020", "11/02/2020", "24/02/2020", "6/03/2020", "7/03/2020", "11/03/2020"};

        for (int i = 0; i < 20; ++i) {
            Random rand = new Random();
            Random rand2 = new Random();
            int j = rand2.nextInt(14);
            com.chavau.univ_angers.univemarge.intermediaire.Cours c = new com.chavau.univ_angers.univemarge.intermediaire.Cours(nomCours[i], dates[i], joursSemaine[rand.nextInt(6)], heuresDebut[j], heureFin[j], Etudiant.creerEtudiants());
            cours.add(c);
        }

        return cours;
    }

}
