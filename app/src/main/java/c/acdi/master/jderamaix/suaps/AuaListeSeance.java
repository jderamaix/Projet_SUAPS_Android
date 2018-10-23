package c.acdi.master.jderamaix.suaps;

public class AuaListeSeance {
    private String tempsSeance;
    private String limitePersonnes;
    private int idSeance;

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
