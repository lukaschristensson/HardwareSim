package GUI.BoardViewer.ChipManipulator;

import CompBoard.Components.Component;
import CompBoard.Components.Pass;
import GUI.BoardViewer.ImageComponents.ImageComponent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;

public class PassImageComponent extends ImageComponent{
    private Pass pass;
    public boolean drawInputs = true;
    public boolean drawOutputs = true;
    private Integer borderWidth;

    public PassImageComponent(Integer radius, Integer borderWidth, Point pos){
        this(radius, borderWidth, pos, new Pass());
    }

    public PassImageComponent(Integer radius, Integer borderWidth, Point pos, Pass pass) {
        super(pos, new Dimension(radius*2, radius*2));
        this.pass = pass;
        this.borderWidth = borderWidth;
        recalculateNodes();
    }

    @Override
    public CNode containsNodeAt(Point p) {
        for (CNode n: inputNodes)
            if (n != null && drawInputs && n.contains(p))
                return n;
        for (CNode n: outputNodes)
            if (n != null && drawOutputs && n.contains(p))
                return n;
        return null;
    }

    @Override
    public void drawSelf(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillOval(pos.x, pos.y, dim.height, dim.height);
        gc.setFill(Color.YELLOW.darker());
        gc.fillOval(pos.x + borderWidth, pos.y + borderWidth, dim.height - borderWidth*2f, dim.height - borderWidth*2f);

        if (pos != null && drawInputs)
            inputNodes[0].drawSelf(gc, INPUT_NODE_COLOR);
        if (pos != null && drawOutputs)
            outputNodes[0].drawSelf(gc, OUTPUT_NODE_COLOR);
    }

    @Override
    public Component getComp() {
        return pass;
    }

    @Override
    public void forceGenerate() {
        pass.generate();
    }

    @Override
    public void setImageStrings() {
        //not using images
    }

    @Override
    public Integer[] getExternalPointDimensions() {
        return new Integer[]{1, 1};
    }

    @Override
    public ImageComponent getEmptyCopy() {
        //not relevant
        return null;
    }
}
