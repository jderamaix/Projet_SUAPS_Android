package c.acdi.master.jderamaix.suaps;


/**
 * Classe utilisé pour le post d'une personne par l'envoie
 * de son numméro de carte
 */
public class Task {
    private String string;

    public Task(String string){
        this.string = string;
    }

    public String getString() {
        return string;
    }
}
