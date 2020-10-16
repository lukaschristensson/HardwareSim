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
    public void deactivate() {
        active = false;
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
    public boolean removeInput(Link l) {
        if (in == l && l.removeChainedComp(this)){
            in = null;
            return true;
        }
        return false;
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
    public boolean removeOutput(Link l) {
        if (out == l){
            out = null;
            return true;
        }
        return false;
    }


    @Override
    public void react() {
        lit = in.getState().getAsBool();
        if(out != null)
            EventWorker.addTriggerEvent(this::generate);
        setChanged();
        notifyObservers(lit);
    }

    @Override
    public void generate() {
        if (active && out != null && in != null)
            out.setState(in.getState());
    }
}
