package GUI.BoardViewer.ImageComponents;

import CompBoard.Components.Component;
import CompBoard.Components.Splitter;
import UtilPackage.ImageLibrary;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

public class SplitterImageComponent extends CalculatingImageComponent {
    private Splitter splitter;

    public SplitterImageComponent(){
        this(null, new Splitter());
    }

    public SplitterImageComponent(Point pos, Splitter splitter) {
        super(pos, new Dimension(30,60));
        this.splitter = splitter;
    }

    @Override
    public void drawSelf(GraphicsContext gc) {
        gc.drawImage(ImageLibrary.getImage(images[0]),pos.x,pos.y,dim.width,dim.height);
        drawNodes(gc);
    }

    @Override
    public Component getComp() {
        return splitter;
    }

    @Override
    public void setImageStrings() {
        images = new String[]{"switchSprite"};
    }

    @Override
    public ImageComponent getEmptyCopy() {
        return new SplitterImageComponent();
    }
}
