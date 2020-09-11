package CompBoard.Components;

import EventWorker.EventWorker;
import UtilPackage.BinaryInt;

public abstract class CalculatingComponent extends Component implements ReactiveComponent,GeneratingComponent {
    protected Link[] inputs, outputs;

    public CalculatingComponent(Link[]  in, Link[] out){
        inputs = in;
        outputs = out;
        for (Link l: in)
            if (l != null)
                l.addChainedComp(this);

    }

    @Override
    public boolean removeInput(Link l) {
        for (int i = 0; i < inputs.length; i++){
            if (inputs[i] != null && inputs[i] == l && l.removeChainedComp(this)){
                inputs[i] = null;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeOutput(Link l) {
        for (int i = 0; i < outputs.length; i++){
            if (outputs[i] != null && outputs[i] == l){
                outputs[i] = null;
                return true;
            }
        }
        return false;
    }

    @Override
    public void deactivate() {
        active = false;
    }

    @Override
    public String generate() {
        if (active)
            return generate(false);
        return "";
    }

    @Override
    public final String generate(boolean forced) {
        BinaryInt[] ins = new BinaryInt[inputs.length];
        for (int i = 0; i < inputs.length; i++)
            if (inputs[i] != null)
                ins[i] = inputs[i].getState();
            else
                return getName() + " was quarried but isn't plugged in";

        BinaryInt[] results = calculateForInput(ins);
        for (int i = 0; i < outputs.length; i++) {
            if(outputs[i] != null && results[i] != null) // && !containsAhead(outputs, i))
                outputs[i].setState(results[i], forced);
            else
                return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getName() + " calculated inputs [.");
        for (BinaryInt bi: ins)
            sb.append(bi + ".");
        sb.append("] -> [.");
        for (BinaryInt bi: results)
            sb.append(bi + ".");
        sb.append("] and out it in the outputs  " +
                ((Component)outputs[0].getChainComponents().get(0)).getName() +
                (outputs.length > 1? "::" +
                        ((Component)outputs[1].getChainComponents().get(0)).getName():
                        ""));
        return sb.toString();
    }

    private static boolean containsAhead(Link[] list, int index){
        if (index >= list.length)
            return false;
        for (int i = index + 1; i < list.length; i++)
            if (list[i].contains(list[index]))
                return true;

        return false;
    }

    @Override
    public final String react() {
        EventWorker.addTriggerEvent((ps)->{
            if (ps != null)
                ps.println(generate());
            else
                generate();
        });
        return getName() + " recieved react request and generation has been queued";
    }


    @Override
    public boolean setOutputs(Link[] out) {
        if (out.length != getOutputSize())
            return false;
        this.outputs = out;
        return true;
    }

    @Override
    public boolean addOutput(Link out) {
        for (int i = 0; i < getOutputSize(); i++)
            if (this.outputs[i] == null) {
                this.outputs[i] = out;
                return true;
            }
        return false;
    }

    @Override
    public boolean setInputs(Link[] in) {
        if (in.length != getInputSize())
            return false;
        this.inputs = in;
        for (Link l: in)
            if (l!= null)
                l.addChainedComp(this);
        return true;
    }

    @Override
    public boolean addInput(Link in) {
        for (int i = 0; i < getInputSize(); i++)
            if (this.inputs[i] == null){
                this.inputs[i] = in;
                in.addChainedComp(this);
                return true;
            }
        return false;
    }

    abstract BinaryInt[] calculateForInput(BinaryInt[] inputs);
}