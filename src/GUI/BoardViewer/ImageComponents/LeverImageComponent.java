package GUI.BoardViewer.ImageComponents;

import CompBoard.Components.Lever;
import UtilPackage.ImageLibrary;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class LeverImageComponent extends ImageComponent implements ClickableComponent {
    private boolean on;

    public LeverImageComponent(){
        this(null, new Lever());
    }
    public LeverImageComponent(Point pos, Lever lever) {
        super(pos, new Dimension(30, 30));
        on = false;
        this.comp = lever;
    }

    @Override
    public void clicked() {
        on = !on;
        ((Lever)this.comp).toggleOutput();
    }

    @Override
    public void drawSelf(GraphicsContext gc) {
        gc.drawImage(ImageLibrary.getImage(on?images[0]:images[1]),pos.x,pos.y,dim.width,dim.height);
        drawNodes(gc);
    }

    @Override
    public void setImageStrings() {
        images = new String[]{"leverOnSprite", "leverOffSprite"};
    }

    @Override
    public Integer[] getExternalPointDimensions() {
        return new Integer[]{0, 1};
    }

    @Override
    public ImageComponent getEmptyCopy() {
        return new LeverImageComponent();
    }
}
