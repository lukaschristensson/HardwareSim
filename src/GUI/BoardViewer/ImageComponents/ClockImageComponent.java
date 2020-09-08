package GUI.BoardViewer.ImageComponents;

import CompBoard.Components.Clock;
import UtilPackage.ImageLibrary;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

public class ClockImageComponent extends ImageComponent {

    public ClockImageComponent(){
        this(null, new Clock());
    }

    public ClockImageComponent(Point pos, Clock clock) {
        super(pos, new Dimension(40,40));
        this.comp = clock;
    }

    public void toggleActive(){
        ((Clock)this.comp).toggleActive();
    }

    @Override
    public void drawSelf(GraphicsContext gc) {
        if (images != null) {
            gc.drawImage(ImageLibrary.getImage(images[0]),pos.x,pos.y,dim.width,dim.height);
            this.drawNodes(gc);
        } else
            System.out.println("clock images has not been loaded");
    }

    @Override
    public void setImageStrings() {
        images = new String[]{"clockSprite"};
    }
    @Override
    public Integer[] getExternalPointDimensions() {
        return new Integer[]{0,((Clock)this.comp).getOutputSize()};
    }

    @Override
    public ImageComponent getEmptyCopy() {
        return new ClockImageComponent();
    }
}
