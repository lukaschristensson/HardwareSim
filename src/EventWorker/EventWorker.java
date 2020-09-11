package EventWorker;

import TimeHandle.TimeDependant;

import java.io.PrintStream;

public class EventWorker implements TimeDependant {
    public static long now = 0;
    private static EventInst LIST;
    private static final long BASE_ADD_TIME = 1;
    private static PrintStream ps;

    public static boolean addTriggerEvent(TriggerEvent te, long time) {
        long insertTime = now + time + BASE_ADD_TIME;


        if (LIST == null || (insertTime < LIST.time)){
            EventInst newFirst = new EventInst(insertTime);
            newFirst.next = LIST;
            LIST = newFirst;
        }

        return LIST.addTrigger(te,insertTime);
    }
    public static boolean addTriggerEvent(TriggerEvent te){
        return addTriggerEvent(te,0);
    }

    public static void setPrintStream(PrintStream ps){
        EventWorker.ps = ps;
    }

    @Override
    public void act(long now) {
        EventWorker.now = now;
        while (LIST != null && LIST.time <= now) {
            LIST.run(ps);
            LIST = LIST.next;
        }
    }
}