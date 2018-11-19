package c.acdi.master.jderamaix.suaps;

/**
 * Contient les données nécessaires pour l'affichage d'un étudiant présent à une séance.
 */
public class StudentEntry {

    /**
     * Nom et prénom (en une seule chaîne)
     */
    private String _name;
    /**
     * Temps passé à la séance
     */
    private String _elapsedTime;
    /**
     * Identifiant de l'étudiant pour la séance
     */
    private int _id;



    public StudentEntry(String name, String elapsedTime, int id) {
        _name = name;
        _elapsedTime = elapsedTime;
        _id = id;
    }

    public String name() { return _name; }
    public void name(String name) { _name = name; }

    public String elapsedTime() { return _elapsedTime; }
    public void elapsedTime(String elapsedTime) { _elapsedTime = elapsedTime; }

    public int id() { return _id;}
    public void id(int id) { this._id = id;}
}
