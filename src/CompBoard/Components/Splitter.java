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
        if(inputs[0] == null)
            return new BinaryInt[]{new BinaryInt(0), new BinaryInt(0)};
        return new BinaryInt[]{inputs[0],inputs[0]};
    }

    @Override
    public String getName() {
        return name == null || name.length() == 0? "*Splitter*":name;
    }

    @Override
    public char getCompChar() {
        return 'E';
    }


    @Override
    public int getInputSize() {
        return 1;
    }
    @Override
    public int getOutputSize() {
        return 2;
    }
}
