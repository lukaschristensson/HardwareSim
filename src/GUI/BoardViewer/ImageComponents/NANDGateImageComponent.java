package GUI.BoardViewer.ImageComponents;

import CompBoard.Components.Component;
import CompBoard.Components.NANDGate;
import UtilPackage.ImageLibrary;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;

public class NANDGateImageComponent extends CalculatingImageComponent {
    private NANDGate nandGate;

    public NANDGateImageComponent(){
        this(null, new NANDGate());
    }

    public NANDGateImageComponent(Point pos, NANDGate gate) {
        super(pos, new Dimension(50, 50));
        this.nandGate = gate;
    }

    @Override
    public void drawSelf(GraphicsContext gc) {
        gc.drawImage(ImageLibrary.getImage(images[0]), pos.x, pos.y, dim.width, dim.height);
        drawNodes(gc);
    }

    @Override
    public Component getComp() {
        return nandGate;
    }

    @Override
    public void setImageStrings() {
        images = new String[]{"nandSprite"};
    }

    @Override
    public ImageComponent getEmptyCopy() {
        return new NANDGateImageComponent();
    }
}
