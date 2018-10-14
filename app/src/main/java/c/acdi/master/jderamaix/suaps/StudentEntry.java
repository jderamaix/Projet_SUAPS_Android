package c.acdi.master.jderamaix.suaps;

import org.threeten.bp.Duration;

public class StudentEntry {

    private String _name;
    private Duration _elapsedTime;

    public StudentEntry(String name) {
        _name = name;
        _elapsedTime = Duration.ofHours(0);
    }

    public StudentEntry(String name, Duration elapsedTime) {
        _name = name;
        _elapsedTime = elapsedTime;
    }

    public String name() {
        return _name;
    }
    public void name(String name) { _name = name; }

    public Duration elapsedTime() {
        return _elapsedTime;
    }
    public void elapsedTime(Duration elapsedTime) { _elapsedTime = elapsedTime; }
}
