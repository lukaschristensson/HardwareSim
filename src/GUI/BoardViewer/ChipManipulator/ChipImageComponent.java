package GUI.BoardViewer.ChipManipulator;

import CompBoard.Components.Component;
import GUI.BoardViewer.ImageComponents.ImageComponent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;


public class ChipImageComponent extends ImageComponent{
    public int outputSize;
    public int inputSize;
    public static final int heightPerNode = ImageComponent.EXTERNAL_NODE_SIZE + 3;
    private static final int chipWidth = 20;
    private static final Color basicChipColor = Color.BLACK.brighter();
    private String saveData;

    public ChipImageComponent(int inputSize, int outputSize) {
        super(null);
        this.inputSize = inputSize;
        this.outputSize = outputSize;
    }

    public String getSaveData() {
        return saveData;
    }

    public void setInputNodes(CNode... inputNodes){
        this.inputNodes = inputNodes;
    }
    public void setOutputNodes(CNode... outputNodes){
        this.outputNodes = outputNodes;
    }

    public boolean calcDim(){
        if (inputNodes == null || outputNodes == null)
            return false;
        Dimension dim = new Dimension();
        dim.width = chipWidth;
        dim.height = Integer.max(inputNodes.length, outputNodes.length) * heightPerNode;
        this.dim = dim;
        return true;
    }

    @Override
    public void recalculateNodes() {
        if (pos != null){
            for (int i = 0; i < inputSize; i++)
                inputNodes[i].setPos(new Point(pos.x, pos.y + i*heightPerNode + heightPerNode/2));
            for (int i = 0; i < outputSize; i++)
                outputNodes[i].setPos(new Point(pos.x + dim.width, pos.y + i*heightPerNode + heightPerNode/2));
        }
    }

    @Override
    public void drawSelf(GraphicsContext gc) {
        gc.setFill(basicChipColor);
        gc.fillRect(pos.x, pos.y, dim.width, dim.height);
        drawNodes(gc);
    }

    @Override
    public Integer[] getExternalPointDimensions() {
        return new Integer[]{inputSize, outputSize};
    }

    @Override
    public void setImageStrings() {
        //blank since this is going to be drawn
    }

    @Override
    public ImageComponent getEmptyCopy() {
        return ChipCreator.loadChip(saveData);
    }

    public void addPrefix(String s){
        for (CNode node: inputNodes)
            node.parent.getComp().name = node.parent.getComp().getName() + s;
        for (CNode node: outputNodes)
            node.parent.getComp().name = node.parent.getComp().getName() + s;
    }

    public ChipImageComponent setSaveData(String s){
        this.saveData = s;
        return this;
    }
}
