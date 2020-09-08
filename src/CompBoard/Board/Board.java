package CompBoard.Board;

import CompBoard.Components.Component;

import java.util.ArrayList;

public class Board {
    private ArrayList<Component> components;
    public Board(){
        components = new ArrayList<>();
    }
    public boolean addComponent(Component c){
        components.add(c);
        return true;
    }
    public boolean removeComponent(Component c){
        return components.remove(c);
    }
}