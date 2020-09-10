package CompBoard.Components;

import EventWorker.EventWorker;
import UtilPackage.BinaryInt;

import java.util.ArrayList;
import java.util.Observable;

public class Link extends Observable {
    private BinaryInt state;
    private ArrayList<ReactiveComponent> chainComponents;

    public Link(){
        state = new BinaryInt();
        chainComponents = new ArrayList<>();
    }

    public BinaryInt getState() {
        return state;
    }

    public void addChainedComp(ReactiveComponent rc){
        if (!chainComponents.contains(rc))
            chainComponents.add(rc);
    }

    public boolean contains(Object obj){
        if (obj instanceof ReactiveComponent)
            return chainComponents.contains(obj);
        if (obj instanceof Link)
            for (ReactiveComponent rc: ((Link)obj).chainComponents)
                if (contains(rc))
                    return true;
        return false;
    }

    public void setState(BinaryInt state){
        setState(state, false);
    }

    public void setState(BinaryInt state, boolean forced) {
        if (!this.state.equals(state) || forced) {
            this.state = state;
            setChanged();
            notifyObservers(state.getAsBool());
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
    public void setState(int i, boolean forced){
        setState(new BinaryInt(i), forced);
    }
    public void setState(boolean b){
        setState(new BinaryInt(b));
    }
    public void setState(boolean b, boolean forced){
        setState(new BinaryInt(b), forced);
    }
}
