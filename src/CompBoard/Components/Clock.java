package CompBoard.Components;

import EventWorker.EventWorker;
import UtilPackage.BinaryInt;


public class Clock extends Component implements GeneratingComponent {
    private Link outLink;
    private boolean enabled = false;
    private long onPeriod;
    private long offPeriod;
    private BinaryInt outputState;

    public Clock(){
        this(500, 500);
    }
    public Clock(long onPreriod,long offPeriod){
        this(null,onPreriod,offPeriod);
    }
    public Clock(Link l,long onPeriod,long offPeriod){
        EventWorker.addTriggerEvent((ps)->this.toggleActive(),500);
        outLink = l;
        this.onPeriod = onPeriod;
        this.offPeriod = offPeriod;
        outputState = new BinaryInt(0);

    }

    public void toggleActive(){
        enabled = !enabled;
        if (enabled)
            EventWorker.addTriggerEvent((ps) -> {
                if (ps != null)
                    ps.println(generate());
                else
                    generate();
            });
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name == null || name.length() == 0 ? "*Clock*" : name;
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
    public String generate() {
        if (!enabled)
            return getName() + " quarried but disabled";

        if (outLink == null){
            EventWorker.addTriggerEvent((ps)->{
                if (ps != null)
                    ps.println(generate());
                else
                    generate();
            }, outputState.getAsBool() ? onPeriod : offPeriod);
            return getName() + " quarried but disconnected";
        }

        outputState = outputState.inverse();
        outLink.setState(outputState);
        EventWorker.addTriggerEvent((ps)->{
            if (ps != null)
                ps.println(generate());
            else
                generate();
        }, outputState.getAsBool() ? onPeriod : offPeriod);
        setChanged();
        notifyObservers(outputState);
        return getName() + " flipped to " + outputState + ", event to switch state queued";
    }
}
