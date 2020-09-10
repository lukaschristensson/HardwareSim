package CompBoard.Components;

import EventWorker.EventWorker;

public class Lamp extends Component implements ReactiveComponent, GeneratingComponent {
    private Link in;
    private Link out;
    private boolean lit;

    public Lamp(Link l){
        setInputs(new Link[]{l});
    }
    public Lamp(){
        this(null);
    }

    public void setName(String s){
        name = s;
    }

    @Override
    public String getName() {
        return name == null || name.length() == 0? "*lamp*":name;
    }

    @Override
    public char getCompChar() {
        return 'L';
    }

    @Override
    public int getOutputSize() {
        return out == null ? 0: 1;
    }
    @Override
    public int getInputSize() {
        return 1;
    }

    @Override
    public boolean setInputs(Link[] in) {
        this.in = in[0];
        if (this.in != null)
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
    public boolean setOutputs(Link[] out) {
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
    public String react() {
        lit = in.getState().getAsBool();
        if(out != null) {
            EventWorker.addTriggerEvent((ps -> {
                if (ps != null)
                    ps.println(generate());
                else
                    generate();
            }));
        }
        setChanged();
        notifyObservers(lit);
        return getName() + " is now " + (lit? "lit" : " unlit") + (out != null? " and generate has been queued":"");
    }
    @Override
    public String generate(boolean forced) {
        if (out != null && in != null)
            out.setState(in.getState(), forced);
        return getName() + " passed signal through it";
    }
    @Override
    public String generate() {
        return generate(false);
    }
}
