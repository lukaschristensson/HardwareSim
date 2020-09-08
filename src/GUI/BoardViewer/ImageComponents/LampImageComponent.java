package GUI.BoardViewer.ImageComponents;

import CompBoard.Components.Component;
import CompBoard.Components.Lamp;
import UtilPackage.ImageLibrary;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class LampImageComponent extends ImageComponent implements Observer {
    private boolean lit;

    public LampImageComponent(){
        this(null, new Lamp());
    }

    public LampImageComponent(Point pos, Lamp lamp) {
        super(pos, new Dimension(40,40));
        this.comp = lamp;
        lamp.addObserver(this);
    }

    @Override
    public void drawSelf(GraphicsContext gc) {
        gc.drawImage(ImageLibrary.getImage(lit? images[1]:images[0]),pos.x,pos.y,dim.width,dim.height);
        drawNodes(gc);
    }

    @Override
    public void setImageStrings() {
        images = new String[]{"lampOffSprite", "lampOnSprite"};
    }

    @Override
    public Integer[] getExternalPointDimensions() {
        return new Integer[]{1,1};
    }

    @Override
    public ImageComponent getEmptyCopy() {
        return new LampImageComponent();
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("lamp updated with" + (boolean)arg);
        if (o instanceof Lamp && arg instanceof Boolean)
            this.lit = (boolean) arg;
    }
}
