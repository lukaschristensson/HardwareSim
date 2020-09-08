package TimeHandle;

import java.util.ArrayList;

public class SuperClock {
    private static long startTime;
    private static long time;
    private static ArrayList<TimeDependant> tds;
    public static void addTimeDependant(TimeDependant td){
        if (tds == null)
            tds = new ArrayList<>();
        tds.add(td);
    }

    public static void tick(){
        if (startTime == 0)
            startTime = System.currentTimeMillis();
        time = System.currentTimeMillis() - startTime;
        if (tds != null)
            tds.forEach(e-> e.act(time));
    }
}
