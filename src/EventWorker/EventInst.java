package EventWorker;

import java.util.ArrayList;

class EventInst {
    EventInst next;
    long time;
    private boolean running = false;
    private ArrayList<TriggerEvent> ets;

    private int size(){
        if (next == null)
            return 1;
        return next.size() + 1;
    }

    EventInst(long time){
        ets = new ArrayList<>();
        this.time = time;
    }

    boolean addTrigger(TriggerEvent te, long time){
        if (te == null || time < this.time) {
            System.out.println("threw event");
            return false;
        }
        if (this.time == time) {
            if (running)
                te.trigger();
            else
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

    void run(){
        running = true;
        ets.forEach(TriggerEvent::trigger);
    }
}
