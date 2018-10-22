package c.acdi.master.jderamaix.suaps;

import java.util.Date;

public class StudentEntry {

    private String _name;
    private Date _elapsedTime;

    public StudentEntry(String name) {
        _name = name;
        _elapsedTime = new Date(0);
    }

    public StudentEntry(String name, Date elapsedTime) {
        _name = name;
        _elapsedTime = elapsedTime;
    }

    public String name() {
        return _name;
    }
    public void name(String name) { _name = name; }

    public Date elapsedTime() {
        return _elapsedTime;
    }
    public void elapsedTime(Date elapsedTime) { _elapsedTime = elapsedTime; }
}
