package CompBoard.Components;

import EventWorker.EventWorker;

public class Button extends Component implements GeneratingComponent {
    private Link outputLink;
    private long pulseLength;
    private boolean currentlyDown;

    public Button(Link out, long pulseLength){
        this.pulseLength = pulseLength;
        outputLink = out;
        currentlyDown = false;
    }
    public Button(int pulseLength){
        this(null,pulseLength);
    }
    public Button(){
        this(100);
    }

    @Override
    public String getName() {
        return name == null || name.length() == 0?"*button*":name;
    }


    @Override
    public int getOutputSize() {
        return 1;
    }

    @Override
    public boolean setOutputs(Link[] out) {
        if (out.length == 1){
            outputLink = out[0];
            return true;
        }
        return false;
    }

    @Override
    public boolean addOutput(Link out) {
        if (outputLink != null)
            return false;
        outputLink = out;
        return true;
    }

    public boolean press(){
        if (currentlyDown)
            return false;
        EventWorker.addTriggerEvent((ps)->{
            if (ps != null)
                ps.println(generate());
            else
                generate();
        });
        return true;
    }

    @Override
    public String generate() {
        if (!currentlyDown) {
            outputLink.setState(1);
            currentlyDown = true;
            EventWorker.addTriggerEvent((ps)->{
                if (ps != null)
                    ps.println(generate());
                else
                    generate();
            }, pulseLength);
            setChanged();
            notifyObservers(currentlyDown);
            return getName() + " is now pressed down";
        } else {
            outputLink.setState(0);
            currentlyDown = false;
            setChanged();
            notifyObservers(currentlyDown);
            return getName() + " is now released";
        }
    }
}
