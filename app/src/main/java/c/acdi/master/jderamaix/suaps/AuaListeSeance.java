package c.acdi.master.jderamaix.suaps;

/**
 * Classe qui fait l'interface entre l'application et la base de données
 * Occasionnée par l'utilisation de retrofit
 */
public class AuaListeSeance {
    private String limitePersonnes;
    private String tempsSeance;
    private int idSeance;

    public AuaListeSeance(String limitePersonnes, String tempsSeance, int idSeance) {
        this.idSeance = idSeance;
        this.limitePersonnes = limitePersonnes;
        this.tempsSeance = tempsSeance;
    }

    public void setTempsSeance(String tempsSeance){
        this.tempsSeance = tempsSeance;
    }

    public void setLimitePersonnes(String limitePersonnes){
        this.limitePersonnes = limitePersonnes;
    }

    public void setIdSeance(int idSeance){
        this.idSeance = idSeance;
    }

    public String getTempsSeance(){
        return this.tempsSeance;
    }

    public String getLimitePersonnes(){
        return this.limitePersonnes;
    }

    public int getIdSeance(){
        return this.idSeance;
    }
}
