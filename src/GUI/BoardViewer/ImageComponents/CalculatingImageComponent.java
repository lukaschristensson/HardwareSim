package GUI.BoardViewer.ImageComponents;

import CompBoard.Components.CalculatingComponent;

import java.awt.*;

public abstract class CalculatingImageComponent extends ImageComponent {

    public CalculatingImageComponent(Dimension dim){
        this(null, dim);
    }

    public CalculatingImageComponent(Point pos, Dimension dim) {
        super(pos, dim);
        setPos(pos);
    }

    @Override
    public Integer[] getExternalPointDimensions() {
        CalculatingComponent cc = (CalculatingComponent) getComp();
        return new Integer[]{cc.getInputSize(),cc.getOutputSize()};
    }
}
