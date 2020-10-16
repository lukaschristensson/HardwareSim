package CompBoard.Components;

import EventWorker.EventWorker;
import UtilPackage.BinaryInt;

import java.util.ArrayList;
import java.util.Observable;

public class Link extends Observable {
    public BinaryInt state;
    private boolean forceNext;
    private ArrayList<ReactiveComponent> chainComponents;

    public Link(){
        state = new BinaryInt();
        chainComponents = new ArrayList<>();
        forceNext();
    }

    public BinaryInt getState() {
        return state;
    }

    public void forceNext(){
        forceNext = true;
    }

    void addChainedComp(ReactiveComponent rc){
        if (!chainComponents.contains(rc))
            chainComponents.add(rc);
    }
    boolean removeChainedComp(ReactiveComponent rc){
        return chainComponents.remove(rc);
    }

    public ArrayList<ReactiveComponent> getChainComponents() {
        return chainComponents;
    }

    boolean contains(Object obj){
        if (obj instanceof ReactiveComponent)
            return chainComponents.contains(obj);
        if (obj instanceof Link)
            for (ReactiveComponent rc: ((Link)obj).chainComponents)
                if (contains(rc))
                    return true;
        return false;
    }

    void setState(BinaryInt state) {
        if (!this.state.equals(state) || forceNext) {
            forceNext = false;
            this.state = state;
            setChanged();
            notifyObservers(state.getAsBool());
            for (ReactiveComponent rc : chainComponents)
                EventWorker.addTriggerEvent(rc::react);
        }
    }
    void setState(int i){
        setState(new BinaryInt(i));
    }
    void setState(boolean b){
        setState(new BinaryInt(b));
    }
}
