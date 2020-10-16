package CompBoard.Components;

import EventWorker.EventWorker;
import UtilPackage.BinaryInt;

import java.util.Observable;
import java.util.Observer;

public class Lever extends Component implements GeneratingComponent {
    private Link outputLink;
    private boolean outputState;

    public void setName(String name) {
        this.name = name;
    }

    public Lever(Link out){
        this.outputLink = out;
        outputState = false;
    }

    public Lever(){
        this(null);
    }

    @Override
    public String getName() {
        return name == null || name.length() == 0? "*Lever*": name;
    }

    @Override
    public char getCompChar() {
        return 'S';
    }

    @Override
    public void deactivate() {
        active = false;
    }

    @Override
    public int getOutputSize() {
        return 1;
    }

    @Override
    public boolean setOutputs(Link[] out) {
        if (out.length != 1)
            return false;
        this.outputLink = out[0];
        return true;
    }

    @Override
    public boolean addOutput(Link out) {
        if (outputLink != null)
            return false;
        outputLink = out;
        return true;
    }

    @Override
    public boolean removeOutput(Link l) {
        if (outputLink == l){
            outputLink = null;
            return true;
        }
        return false;
    }

    public void toggleOutput(){
        if (active) {
            outputState = !outputState;
            EventWorker.addTriggerEvent(this::generate);
        }
    }

    @Override
    public void generate() {
        if (active) {
            if (outputLink != null)
                outputLink.setState(outputState);
            setChanged();
            notifyObservers(outputState);
        }
    }
}
