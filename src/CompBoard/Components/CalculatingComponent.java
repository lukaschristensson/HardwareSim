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
    public final String generate() {
        BinaryInt[] ins = new BinaryInt[inputs.length];
        for (int i = 0; i < inputs.length; i++) {
            if(inputs[i] != null)
                ins[i] = inputs[i].getState();
        }
        BinaryInt[] results = calculateForInput(ins);
        for (int i = 0; i < outputs.length; i++) {
            if(outputs[i] != null && results[i] != null)
                outputs[i].setState(results[i]);
        }
        return getName() + ": calculated inputs and out it in the outputs";
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