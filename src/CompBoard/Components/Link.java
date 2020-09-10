package CompBoard.Components;

import EventWorker.EventWorker;
import UtilPackage.BinaryInt;

import java.util.ArrayList;
import java.util.Observable;

public class Link extends Observable {
    private BinaryInt state;
    private ArrayList<ReactiveComponent> chainComponents;
    private boolean sunk;
    private  boolean raised;

    public Link(){
        state = new BinaryInt();
        chainComponents = new ArrayList<>();
    }

    public BinaryInt getState() {
        return state;
    }

    public void addChainedComp(ReactiveComponent rc){
        chainComponents.add(rc);
    }

    public void setState(BinaryInt state) {
        if (!this.state.equals(state) && !sunk && !raised) {
            this.state = state;
            for (ReactiveComponent rc : chainComponents)
                EventWorker.addTriggerEvent((ps) -> {
                    if (ps != null)
                        ps.println(rc.react());
                    else
                        rc.react();
                });
        }
    }
    public void setState(int i){
        setState(new BinaryInt(i));
    }
    public void setState(boolean b){
        setState(new BinaryInt(b));
    }
}
