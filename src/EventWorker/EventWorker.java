package EventWorker;

import TimeHandle.TimeDependant;

public class EventWorker implements TimeDependant {
    private static long now = 0;
    private static EventInst LIST;
    private final static Object concFlag = new Object();
    private static final long BASE_ADD_TIME = 10;

    public static boolean addTriggerEvent(TriggerEvent te, long time) {
        synchronized (concFlag) {
            long insertTime = now + time * 1000000L + BASE_ADD_TIME;

            if (LIST == null || (insertTime < LIST.time)) {
                EventInst newFirst = new EventInst(insertTime);
                newFirst.next = LIST;
                LIST = newFirst;
            }

            return LIST.addTrigger(te, insertTime);
        }
    }

    public static boolean addTriggerEvent(TriggerEvent te) {
        synchronized (concFlag) {
            return addTriggerEvent(te, 0);
        }
    }


    private Long lastSec = 0L;
    private int calc = 0;

    @Override
    public void act(long now) {
        synchronized (concFlag) {
            if ((now - lastSec) > 1000000000L) {
                System.out.println("Calc last 1 sec: " + calc);
                lastSec = now;
                calc = 0;
            }
            EventWorker.now = now;
            while (LIST != null && LIST.time <= EventWorker.now) {
                calc++;
                LIST.run();
                LIST = LIST.next;
            }
        }
    }
}