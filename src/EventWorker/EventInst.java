package EventWorker;

import java.io.PrintStream;
import java.util.ArrayList;

public class EventInst {
    EventInst next;
    long time;
    private ArrayList<TriggerEvent> ets;

    public int size(){
        if (next== null)
            return 1;
        return next.size()+1;
    }

    public EventInst(long time){
        ets = new ArrayList<>();
        this.time = time;
    }

    public boolean addTrigger(TriggerEvent te, long time){
        if (time < this.time) {
            return false;
        }
        if (this.time == time) {
            ets.add(te);
            return true;
        }
        if (next == null){
            next = new EventInst(time);
            next.ets.add(te);
            return true;
        }

        if (time < next.time){
            EventInst newInsertion = new EventInst(time);
            newInsertion.next = next;
            next = newInsertion;
        }
        return next.addTrigger(te,time);
    }

    public void run(PrintStream ps){
        ets.forEach(e -> {
            if (e != null)
                e.trigger(ps);
        });

    }
}
