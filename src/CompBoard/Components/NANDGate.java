package CompBoard.Components;

import UtilPackage.BinaryInt;

public class NANDGate extends CalculatingComponent{

    public void setName(String name) {
        this.name = name;
    }

    public NANDGate() {
        super(new Link[2], new Link[1]);
    }

    @Override
    BinaryInt[] calculateForInput(BinaryInt[] inputs) {
        return new BinaryInt[]{new BinaryInt(
                !(inputs[0].getAsBool() && inputs[1].getAsBool())
        )};
    }

    @Override
    public String getName() {
        return name == null || name.length() == 0? "*NANDGate*": name;
    }

    @Override
    public char getCompChar() {
        return 'N';
    }

    @Override
    public int getOutputSize() {
        return 1;
    }

    @Override
    public int getInputSize() {
        return 2;
    }
}
