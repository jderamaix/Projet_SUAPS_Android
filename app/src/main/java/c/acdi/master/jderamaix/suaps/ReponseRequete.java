package c.acdi.master.jderamaix.suaps;

/**
 * Classe utilisé pour les réponses de la base, qui sont systématiquement de type chaîne.
 */
public class ReponseRequete {

    private String reponse;

    public ReponseRequete(String reponse){ this.reponse = reponse; }

    public String getReponse() { return this.reponse; }

    public void setReponse(String reponse) { this.reponse = reponse; }
}
