package CompBoard.Components;

import UtilPackage.BinaryInt;

public class Splitter extends CalculatingComponent {

    public void setName(String name) {
        this.name = name;
    }

    public Splitter(Link in, Link[] out) {
        super(new Link[]{in}, out);
    }
    public Splitter(){
        this(null,new Link[2]);
    }

    @Override
    BinaryInt[] calculateForInput(BinaryInt[] inputs) {
        return new BinaryInt[]{inputs[0],inputs[0],inputs[0]};
    }

    @Override
    public String getName() {
        return name == null || name.length() == 0? "*Splitter*":name;
    }


    @Override
    public int getInputSize() {
        return 1;
    }
    @Override
    public int getOutputSize() {
        return 2;
    }

    @Override
    public boolean setOutputs(Link[] out) {
        outputs = out;
        return true;
    }

    @Override
    public boolean addOutput(Link out) {
        if (outputs[0] != null && outputs[1] != null)
            return false;

        if (outputs[0] == null)
            outputs[0] = out;
        else
            outputs[1] = out;
        return true;
    }

    @Override
    public boolean setInputs(Link[] in) {
        inputs = in;
        if (in!= null)
            for (Link l: in)
                if (l!= null)
                    l.addChainedComp(this);
        return false;
    }

    @Override
    public boolean addInput(Link in) {
        if (inputs[0] != null)
            return false;
        inputs[0] = in;
        return true;
    }
}
