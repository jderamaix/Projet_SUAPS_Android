package c.acdi.master.jderamaix.suaps;



/**
 * Classe utilisé pour transité les données nécessaires au
 *      requête du java de l'application au Json de la base de données,
 *      concernant le numéro de la carte des utilisateurs.
 *  Concerne la requête utilisée pour enlever un utilisater de la séance
 *      en fonction de son id (si ajouté manuellement) ou son numéro de carte étudiant/personnel.
 */
public class NumeroIDCarteEtudiant {
    /*
     * Le numéro de la carte si l'utilisateur est un étudiant ou un personnel
     *      ou le numéro d'identification de l'utilisateur, si il est ajouté manuellement
     *      sur l'application, donné par la base de données.
     */
    private String string;

    /**
     * Constructeur initialisant le numéro de l'utilisateur.
     * @param string : Le numéro de l'utilisateur.
     */
    public NumeroIDCarteEtudiant(String string){
        this.string = string;
    }

    /**
     * Méthode renvoyant le numéro de l'utilisateur.
     * @return le numéro de l'utilisateur.
     */
    public String getString() {
        return string;
    }
}
