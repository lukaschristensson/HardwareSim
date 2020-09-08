package CompBoard.Components;

import java.util.Observable;

public abstract class Component extends Observable {
    public String name;
    public abstract String getName();
}
