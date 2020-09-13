package GUI.BoardViewer.ChipManipulator;

import CompBoard.Components.Component;
import GUI.BoardViewer.ImageComponents.ClickableComponent;
import GUI.BoardViewer.ImageComponents.ImageComponent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;


public class ChipImageComponent extends ImageComponent implements ClickableComponent {
    public int outputSize;
    public int inputSize;
    public static final int heightPerNode = ImageComponent.EXTERNAL_NODE_SIZE + 3;
    private static final int chipWidth = 20;
    private static final Color basicChipColor = Color.BLACK.brighter();
    private String saveData;
    private ArrayList<Component> comps;
    public String[] description;
    public String chipName;

    public void setComps(ArrayList<Component> comps) {
        this.comps = comps;
    }

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
        return ChipCreator.loadChip(chipName, saveData);
    }

    public void deactivate(){
        for (Component c: comps)
            c.deactivate();
    }

    public void addPrefix(String s){
        for (CNode node: inputNodes)
            node.parent.getComp().name = node.parent.getComp().getName() + s;
        for (CNode node: outputNodes)
            node.parent.getComp().name = node.parent.getComp().getName() + s;
    }

    public void showDescriptionWindow(){
        Group g = new Group();
        Scene sc = new Scene(g);
        Stage s = new Stage();
        s.setScene(sc);
        HBox mainHBox = new HBox(10);
        mainHBox.setPadding(new Insets(5));

        VBox left = new VBox();
        VBox right = new VBox();
        HBox.setHgrow(left, Priority.ALWAYS);
        HBox.setHgrow(right, Priority.ALWAYS);
        left.setAlignment(Pos.TOP_CENTER);
        right.setAlignment(Pos.TOP_CENTER);
        for (int i = 0; i < description.length; i++){
                javafx.scene.control.Label inLabel = new javafx.scene.control.Label(description[i]);
                inLabel.setAlignment(Pos.CENTER);
                inLabel.setPrefSize(50, 50);
                inLabel.setPadding(new Insets(3));
                inLabel.setStyle("" +
                        "-fx-font-size: 17px;" +
                        "-fx-border-color: BLACK;" +
                        "-fx-border-width: 3;");
                if (i < inputSize) {
                    left.getChildren().addAll(inLabel);
                }else {
                    right.getChildren().addAll(inLabel);
                }
        }

        HBox blockDivider = new HBox();
        blockDivider.setPrefWidth(100);
        VBox.setVgrow(blockDivider, Priority.ALWAYS);
        blockDivider.setStyle("-fx-background-color: BLACK;");
        mainHBox.getChildren().addAll(left, blockDivider, right);

        Label chipNameL = new Label(chipName);
        chipNameL.setPrefHeight(40);
        chipNameL.setAlignment(Pos.CENTER);

        VBox mainVBox = new VBox();
        mainVBox.getChildren().addAll(chipNameL, mainHBox);
        mainVBox.setAlignment(Pos.CENTER);

        g.getChildren().addAll(mainVBox);

        s.show();
    }

    public ChipImageComponent setSaveData(String s){
        this.saveData = s;
        return this;
    }

    @Override
    public void clicked() {
        if (description != null)
            showDescriptionWindow();
        else
            new Alert(Alert.AlertType.ERROR, "This chip does not contain a description", ButtonType.OK).showAndWait();
    }
}
