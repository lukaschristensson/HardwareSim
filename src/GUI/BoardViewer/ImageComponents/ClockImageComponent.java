package GUI.BoardViewer.ImageComponents;

import CompBoard.Components.Clock;
import CompBoard.Components.Component;
import UtilPackage.ImageLibrary;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

public class ClockImageComponent extends ImageComponent {
    private Clock clock;

    public ClockImageComponent(){
        this(null, new Clock());
    }

    public ClockImageComponent(Point pos, Clock clock) {
        super(pos, new Dimension(40,40));
        this.clock = clock;
    }

    public void toggleActive(){
        clock.toggleActive();
    }

    @Override
    public void drawSelf(GraphicsContext gc) {
        gc.drawImage(ImageLibrary.getImage(images[0]), pos.x, pos.y, dim.width, dim.height);
        this.drawNodes(gc);
    }

    @Override
    public Component getComp() {
        return clock;
    }

    @Override
    public void forceGenerate() {
        // no generation
    }

    @Override
    public void setImageStrings() {
        images = new String[]{"clockSprite"};
    }
    @Override
    public Integer[] getExternalPointDimensions() {
        return new Integer[]{0,clock.getOutputSize()};
    }

    @Override
    public ImageComponent getEmptyCopy() {
        return new ClockImageComponent();
    }
}
