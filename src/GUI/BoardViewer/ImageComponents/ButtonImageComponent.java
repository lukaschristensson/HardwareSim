package GUI.BoardViewer.ImageComponents;

import CompBoard.Components.Button;
import GUI.BoardViewer.BoardViewer;
import UtilPackage.ImageLibrary;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

public class ButtonImageComponent extends ImageComponent implements ClickableComponent {

    public ButtonImageComponent(){
        this(null, new Button());
    }

    public ButtonImageComponent(Point pos, Button button) {
        super(pos, new Dimension(40,40));
        this.comp = button;
    }

    @Override
    public void drawSelf(GraphicsContext gc) {
        gc.drawImage(ImageLibrary.getImage(images[0]),pos.x,pos.y,dim.width,dim.height);
        drawNodes(gc);
    }

    @Override
    public void setImageStrings() {
        images = new String[]{"buttonSprite"};
    }

    @Override
    public Integer[] getExternalPointDimensions() {
        return new Integer[]{0, ((Button)getComp()).getOutputSize()};
    }

    @Override
    public ImageComponent getEmptyCopy() {
        return new ButtonImageComponent();
    }

    @Override
    public void clicked() {
        ((Button)getComp()).press();
    }
}
