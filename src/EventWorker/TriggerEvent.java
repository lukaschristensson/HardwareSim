package EventWorker;

import java.io.PrintStream;

public interface TriggerEvent {
    void trigger(PrintStream ps);
}
