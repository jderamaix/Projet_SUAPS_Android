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


    /**
     * Constructeur permettant d'initialiser un étudiant
     *
     * @param name Nom et Prénom de l'individu
     * @param elapsedTime temps passé à la séance
     * @param id identifiant de l'étudiant pour la séane
     */
    public StudentEntry(String name, String elapsedTime, int id) {
        _name = name;
        _elapsedTime = elapsedTime;
        _id = id;
    }

    /**
     * Méthode pour obtenir le nom et prénom de l'étudiant
     * @return Nom et prénom
     */
    public String name() { return _name; }

    /**
     * Attribue le nom de l'élève
     * @param name Nom à attribuer
     */
    public void name(String name) { _name = name; }

    /**
     * Méthode retournant le temps passé à la séance
     *
     * @return Temps passé durant la séance
     */
    public String elapsedTime() { return _elapsedTime; }

    /**
     * Attribue le temps passé
     * @param elapsedTime Temps passé durant la séance
     */
    public void elapsedTime(String elapsedTime) { _elapsedTime = elapsedTime; }

    /**
     * Méthode renvoyant l'id de l'étudiant
     * @return Id de l'étudiant
     */
    public int id() { return _id;}

    /**
     * Attribue un id à un étudiant
     * @param id Id à attribuer
     */
    public void id(int id) { this._id = id;}
}
