package c.acdi.master.jderamaix.suaps;


/**
 * Classe utilisé pour avoir le modèle utilisé lors des requêtes à la base de données,
 * ayant besoin du nom, de la carte ou de l'id de l'étudiant
 * @see Client#EnleverPersonne(String, NomIDCarteEtudiant)
 */
public class NomIDCarteEtudiant {
    private String string;

    public NomIDCarteEtudiant(String string){
        this.string = string;
    }

    public String getString() {
        return string;
    }
}
