package CompBoard.Components;

import EventWorker.EventWorker;
import UtilPackage.BinaryInt;

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

    public boolean toggleOutput(){
        outputState = !outputState;
        EventWorker.addTriggerEvent((ps)->{
           if (ps != null)
               ps.println(generate());
           else
               generate();
        });
        return outputState;
    }

    @Override
    public String generate() {
        if (outputLink == null) {
            return getName() + " switched, but is disconnected";
        }

        outputLink.setState(outputState);
        return getName() + " switched to " + outputState;
    }
}
