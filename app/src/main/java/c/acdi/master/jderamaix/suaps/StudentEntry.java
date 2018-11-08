package c.acdi.master.jderamaix.suaps;

public class StudentEntry {

    // Méthode de convenance pour calculer des heures
    public static long calculateTimeOffset(int hours, int minutes) {
        return 60000*((hours - 1)*60 + minutes);
    }

    private String _name;
    private String _elapsedTime;
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

    public int id(){ return _id;}
    public void id(int id){ this._id = id;}
}
