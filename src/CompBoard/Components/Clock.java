package CompBoard.Components;

import EventWorker.EventWorker;
import UtilPackage.BinaryInt;


public class Clock extends Component implements GeneratingComponent {
    private Link outLink;
    private boolean enabled = true;
    private long onPeriod;
    private long offPeriod;
    private BinaryInt outputState;

    public Clock(){
        this(200, 800);
    }
    public Clock(long onPreriod,long offPeriod){
        this(null,onPreriod,offPeriod);
    }
    public Clock(Link l,long onPeriod,long offPeriod){
        outLink = l;
        this.onPeriod = onPeriod;
        this.offPeriod = offPeriod;
        outputState = new BinaryInt(0);
        generate();
    }

    void forceGenerate(){
        if (outLink != null)
            outLink.setState(outputState);
    }

    public void toggleActive(){
        enabled = !enabled;
        generate();
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name == null || name.length() == 0 ? "*Clock*" : name;
    }

    @Override
    public char getCompChar() {
        return 'C';
    }

    @Override
    public void deactivate() {
        this.enabled = false;
    }

    @Override
    public int getOutputSize() {
        return 1;
    }

    @Override
    public boolean setOutputs(Link[] out) {
        this.outLink = out[0];
        return true;
    }

    @Override
    public boolean addOutput(Link out) {
        if (outLink != null)
            return false;
        outLink = out;
        return true;
    }

    @Override
    public boolean removeOutput(Link l) {
        if (outLink == l){
            outLink = null;
            return true;
        }
        return false;
    }

    @Override
    public void generate() {
        if (enabled) {
            outputState = outputState.inverse();
            if (outLink!= null)
                outLink.setState(outputState);
            EventWorker.addTriggerEvent(this::generate, outputState.getAsBool() ? onPeriod : offPeriod);
            setChanged();
            notifyObservers(outputState);
        }
    }
}
