package CompBoard.Components;

import java.util.Observable;

public abstract class Component extends Observable {
    public String name;
    boolean active = true;
    public abstract String getName();
    public abstract char getCompChar();
    public abstract void deactivate();

    public static Component getCompByChar(char c){
        switch (c){
            case 'B': return new Button();
            case 'C': return new Clock();
            case 'L': return new Lamp();
            case 'S': return new Lever();
            case 'N': return new NANDGate();
            case 'P': return new Pass();
            case 'E': return new Splitter();
            default: return null;
        }
    }
}
