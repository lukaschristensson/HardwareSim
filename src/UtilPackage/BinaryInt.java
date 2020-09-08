package UtilPackage;

import CompBoard.Components.Component;

import java.util.ArrayList;

public class BinaryInt{
    private Integer state;

    public BinaryInt(int i){
        if (i != 0 && i != 1)
            System.out.println("BINARY INT NOT SET TO 0 OR 1");
        else
            state = i;
    }
    public BinaryInt(boolean b){
        this(b?1:0);
    }
    public BinaryInt(){
        this(0);
    }
    public void setState(int state) {
        this.state = state;
    }
    public void setState(Boolean b){
        state = b?1:0;
    }
    public void setState(BinaryInt bi){
        state = bi.getState();
    }
    public int getState() {
        return state;
    }
    public boolean getAsBool(){
        return state == 1;
    }
    public BinaryInt inverse(){
        return new BinaryInt(!getAsBool());
    }
    @Override
    public String toString() {
        return state.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BinaryInt)
            return ((BinaryInt)obj).state.equals(state);
        return false;
    }
}
