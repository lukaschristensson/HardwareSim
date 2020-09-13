package CompBoard.Components;

import EventWorker.EventWorker;
import UtilPackage.BinaryInt;

public class Pass extends Component implements GeneratingComponent, ReactiveComponent {
    Link out;
    Link in;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name == null || name.length() == 0? "*Pass*":name;
    }

    @Override
    public char getCompChar() {
        return 'P';
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
        this.out = out[0];
        return true;
    }

    @Override
    public boolean addOutput(Link out) {
        if (this.out != null)
            return false;
        this.out = out;
        return true;
    }

    @Override
    public boolean removeOutput(Link l) {
        if (out == l){
            out = null;
            return true;
        }
        return false;
    }

    @Override
    public int getInputSize() {
        return 1;
    }

    @Override
    public boolean setInputs(Link[] in) {
        if (in.length != 1)
            return false;
        this.in = in[0];
        this.in.addChainedComp(this);
        return true;
    }

    @Override
    public boolean addInput(Link in) {
        if (this.in != null)
            return false;
        this.in = in;
        this.in.addChainedComp(this);
        return true;
    }

    @Override
    public boolean removeInput(Link l) {
        if (in == l && l.removeChainedComp(this)){
            in = null;
            return true;
        }
        return false;
    }

    @Override
    public String react() {
        if (out != null)
            EventWorker.addTriggerEvent((ps -> {
                if (ps != null)
                    ps.println(generate());
                else
                    generate();
            }));

        return getName() + " has queued generate";
    }

    @Override
    public String generate() {
        if (active)
            return generate(false);
        return "";
    }

    @Override
    public String generate(boolean forced) {
        if (out != null)
            out.setState(in == null? new BinaryInt(0) : in.getState(), forced);
        return getName() + " passed signal through";
    }
}
