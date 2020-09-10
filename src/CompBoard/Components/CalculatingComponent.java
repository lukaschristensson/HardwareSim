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
            ins[i] = inputs[i] != null ? inputs[i].getState(): new BinaryInt(0);

        BinaryInt[] results = calculateForInput(ins);
        for (int i = 0; i < outputs.length; i++) {
            if(outputs[i] != null && results[i] != null) // && !containsAhead(outputs, i))
                outputs[i].setState(results[i], forced);
        }
        return getName() + " calculated inputs and out it in the outputs";
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

    abstract BinaryInt[] calculateForInput(BinaryInt[] inputs);
}