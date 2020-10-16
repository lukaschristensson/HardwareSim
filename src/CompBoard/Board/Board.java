package CompBoard.Board;

import CompBoard.Components.Component;

import java.util.ArrayList;

public class Board {
    private ArrayList<Component> components;
    public Board(){
        components = new ArrayList<>();
    }
    public boolean addComponent(Component c){
        if (components.contains(c))
            return false;
        components.add(c);
        return true;
    }
    public boolean removeComponent(Component c){
        return components.remove(c);
    }
    public ArrayList<Component> getComponents() {
        return components;
    }
}