package CompBoard.Components;

import EventWorker.EventWorker;

public class Button extends Component implements GeneratingComponent {
    private Link outputLink;
    private long pulseLength;
    private boolean currentlyDown;
    private boolean pressQueued = false;

    public Button(Link out, long pulseLength){
        this.pulseLength = pulseLength;
        outputLink = out;
        currentlyDown = false;
    }
    public Button(int pulseLength){
        this(null,pulseLength);
    }
    public Button(){
        this(600);
    }

    @Override
    public String getName() {
        return name == null || name.length() == 0?"*button*":name;
    }

    @Override
    public char getCompChar() {
        return 'B';
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
        if (out.length == 1){
            outputLink = out[0];
            return true;
        }
        return false;
    }

    public void ghostGenerate(){
        if (outputLink != null){
            outputLink.setState(currentlyDown);
        }
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
        if (outputLink == l) {
            outputLink = null;
            return true;
        }
        return false;
    }

    public void press(){
        if (!currentlyDown && !pressQueued) {
            pressQueued = true;
            EventWorker.addTriggerEvent(this::generate);
        }
    }

    @Override
    public void generate() {
        pressQueued = false;
        currentlyDown = !currentlyDown;
        if (active)
            if (currentlyDown) {
                if (outputLink != null)
                    outputLink.setState(1);
                EventWorker.addTriggerEvent(this::generate, pulseLength);
            } else {
                if (outputLink != null)
                    outputLink.setState(0);
            }
    }
}
