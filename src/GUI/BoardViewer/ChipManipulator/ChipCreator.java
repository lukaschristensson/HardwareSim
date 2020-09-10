package GUI.BoardViewer.ChipManipulator;

import CompBoard.Components.*;
import CompBoard.Components.Component;
import GUI.BoardViewer.BoardViewer;
import GUI.BoardViewer.CableManipulator.CableLink;
import GUI.BoardViewer.DrawContributer;
import GUI.BoardViewer.ImageComponents.ImageComponent;
import GUI.MainWindow;
import UtilPackage.Cursor;
import UtilPackage.ImageLibrary;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ChipCreator extends BoardViewer {
    public final static Integer maxInputs = 12;
    public final static Integer maxOutputs = 12;

    private boolean running = false;
    private ArrayList<PassImageComponent> inNodes;
    private ArrayList<PassImageComponent> outNodes;
    private ChipImageComponent currentChip;
    private ArrayList<String> subChips;

    private static int compID = 0;
    private static int chipID = 0;

    public ChipCreator(float width, float height) {
        super(width, height);

        backgroundColor = Color.PINK.desaturate();

        this.at = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        addEventHandler(MouseEvent.MOUSE_CLICKED, placeOrSwitch);
        addConfirmAndCancel();
        clearCanvas();
    }

    @Override
    public void startUpdate() {
        super.startUpdate();
        running = true;
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void stopUpdate() {
        super.stopUpdate();
        running = false;
    }

    private void addConfirmAndCancel() {
        Integer buttonWidth = 50;
        Integer borderWidth = 5;

        // add confirm button
        addDrawContributor(new DrawContributer() {
            @Override
            public void drawSelf(GraphicsContext gc) {
                gc.setFill(Color.BLACK);
                gc.fillRect(
                        getWidth() - buttonWidth, getHeight() - buttonWidth,
                        buttonWidth, buttonWidth);
                gc.setFill(Color.GREEN);
                gc.fillRect(getWidth() - buttonWidth + borderWidth, getHeight() - buttonWidth + borderWidth,
                        buttonWidth - borderWidth*2, buttonWidth - borderWidth*2);
                gc.setStroke(Color.BLACK);
                gc.strokeText("C", getWidth() - buttonWidth/1.8, getHeight() - (buttonWidth - buttonWidth/1.8));
            }

            @Override
            public boolean isEnabled() {
                return running;
            }
        });

        // add cancel button
        addDrawContributor(new DrawContributer() {
            @Override
            public void drawSelf(GraphicsContext gc) {
                gc.setFill(Color.BLACK);
                gc.fillRect(
                        getWidth() - buttonWidth, 0,
                        buttonWidth, buttonWidth);
                gc.setFill(Color.RED);
                gc.fillRect(getWidth() - buttonWidth + borderWidth, borderWidth,
                        buttonWidth - borderWidth*2, buttonWidth - borderWidth*2);
                gc.setStroke(Color.BLACK);
                gc.strokeText("X", getWidth() - buttonWidth/1.8, buttonWidth/1.8);
            }

            @Override
            public boolean isEnabled() {
                return running;
            }
        });

        addEventHandler(MouseEvent.MOUSE_PRESSED, (e) ->{
            if (e.getButton() == MouseButton.PRIMARY &&
                    e.getX() < getWidth() && e.getX() > getWidth() - buttonWidth &&
                    e.getY() < getHeight() && e.getY() > getHeight() - buttonWidth){
                confirmChip();
            } else if (e.getButton() == MouseButton.PRIMARY &&
                    e.getX() < getWidth() && e.getX() > getWidth() - buttonWidth &&
                    e.getY() < buttonWidth && e.getY() > 0) {
                cancelAndExit();
            }
        });
    }

    @Override
    public void addComponent(ImageComponent ic) {
        if (ic instanceof ChipImageComponent) {
            ((ChipImageComponent) ic).addPrefix("ch" + chipID);
            String chipSaveData = ((ChipImageComponent) ic).getSaveData();
            String[] saveLines = chipSaveData.split("\n");
            StringBuilder sb = new StringBuilder();
            for (int i = 2; i < saveLines.length; i++) {
                if (!saveLines[i].contains(" -> ")) {
                    saveLines[i] += "ch" + chipID;
                } else{
                    String c1 = saveLines[i].split(" -> ")[0];
                    String c2 = saveLines[i].split(" -> ")[1];
                    saveLines[i] = c1 + "ch" + chipID + " -> " + c2 + "ch" + chipID;
                }
                sb.append(saveLines[i] +"\n");
            }
            subChips.add(sb.toString());
            ics.add(ic);
            chipID++;
            update();
        } else {
            ic.getComp().name = String.valueOf(ic.getComp().getCompChar()) + compID;
            compID++;
            super.addComponent(ic);
        }
    }

    private static int nodeOrbRad = 15;
    private static int nodeOrbBorderWidth = 2;
    private static int widthPadding = 30;
    private static int heightPadding = 20;

    private void populateNodes() {
        if (currentChip != null) {
            int distBetweenInNodes = (int) ((getHeight() - heightPadding * 2) / (currentChip.inputSize + 1));
            int distBetweenOutNodes = (int) ((getHeight() - heightPadding * 2) / (currentChip.outputSize + 1));
            inNodes = new ArrayList<>();
            outNodes = new ArrayList<>();
            for (int i = 1; i <= currentChip.inputSize; i++) {
                Point pos = new Point(widthPadding, i*distBetweenInNodes + heightPadding);
                pos.translate(-nodeOrbRad/2, -nodeOrbRad/2);
                PassImageComponent pic = new PassImageComponent(nodeOrbRad, nodeOrbBorderWidth, pos);
                pic.drawInputs = false;
                inNodes.add(pic);
                addComponent(pic);
            }
            for (int i = 1; i <= currentChip.outputSize; i++) {
                Point pos = new Point((int)(getWidth() - 50 - widthPadding), i*distBetweenOutNodes + heightPadding);
                pos.translate(-nodeOrbRad/2, -nodeOrbRad/2);
                PassImageComponent pic = new PassImageComponent(nodeOrbRad, nodeOrbBorderWidth, pos);
                pic.drawOutputs = false;
                outNodes.add(pic);
                addComponent(pic);
            }
        }
    }

    public void confirmChip(){
        ArrayList<ImageComponent.CNode> finalInNodes = new ArrayList<>();
        ArrayList<ImageComponent.CNode> finalOutNodes = new ArrayList<>();
        for (PassImageComponent pic: inNodes)
            finalInNodes.add(pic.inputNodes[0]);
        for (PassImageComponent pic: outNodes)
            finalOutNodes.add(pic.outputNodes[0]);

        currentChip.setInputNodes(finalInNodes.toArray(new ImageComponent.CNode[0]));
        currentChip.setOutputNodes(finalOutNodes.toArray(new ImageComponent.CNode[0]));
        currentChip.calcDim();
        saveChip();
        cancelAndExit();
    }

    public Menu getAsMenu(){
        Menu chipMenu = new Menu("Chip");

        MenuItem createNew = new MenuItem("Create new");
        chipMenu.getItems().addAll(createNew);

        javafx.scene.control.Dialog<Integer[]> getSizeDialog = new javafx.scene.control.Dialog<>();
        getSizeDialog.setTitle("Set chip size");
        DialogPane dp = getSizeDialog.getDialogPane();
        dp.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        javafx.scene.control.Label textIn = new javafx.scene.control.Label("input size");
        javafx.scene.control.Label textOut = new Label("output size");
        Spinner<Integer> inSize = new Spinner<>();
        Spinner<Integer> outSize = new Spinner<>();
        SpinnerValueFactory<Integer> spinnerFactoryIn = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, ChipCreator.maxInputs, 1);
        SpinnerValueFactory<Integer> spinnerFactoryOut = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, ChipCreator.maxOutputs, 1);
        inSize.setValueFactory(spinnerFactoryIn);
        outSize.setValueFactory(spinnerFactoryOut);
        HBox mainDialogHBox = new HBox(10);
        mainDialogHBox.setPadding(new Insets(10));
        mainDialogHBox.setAlignment(Pos.CENTER);
        VBox dialogTextBox = new VBox(10);
        VBox dialogSpinnerBox = new VBox(5);
        dialogTextBox.setAlignment(Pos.CENTER);
        dialogSpinnerBox.setAlignment(Pos.CENTER);
        dialogTextBox.getChildren().addAll(textIn, textOut);
        dialogSpinnerBox.getChildren().addAll(inSize, outSize);

        mainDialogHBox.getChildren().addAll(dialogTextBox, dialogSpinnerBox);
        dp.setContent(mainDialogHBox);

        getSizeDialog.setResultConverter((ButtonType b)->{
            if (b == ButtonType.OK)
                return new Integer[]{inSize.getValue(), outSize.getValue()};
            return null;
        });

        createNew.setOnAction(e -> {
            Optional<Integer[]> res = getSizeDialog.showAndWait();
            if (res.isPresent()) {
                MainWindow.setCurrentActiveCanvas(MainWindow.cc);
                MainWindow.cc.startNewChip(res.get()[0], res.get()[1]);
            }else
                MainWindow.setCurrentActiveCanvas(MainWindow.bv);
        });

        MenuItem loadChip = new MenuItem("Load a chip");
        loadChip.setOnAction(e ->{
            JFileChooser fileChooser = new JFileChooser(ImageLibrary.RES_URL);
            fileChooser.showDialog(null, null);
            if (fileChooser.getSelectedFile() != null)
                try (BufferedReader br = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    Cursor.hold(loadChip(sb.toString()));
                }catch (Exception exc){exc.printStackTrace();}
        });

        chipMenu.getItems().addAll(loadChip);

        return chipMenu;
    }

    public static ChipImageComponent loadChip(String saveString){

        ArrayList<Component> reconstructedComponents = new ArrayList<>();
        ArrayList<Pass> reconstructedInputs = new ArrayList<>();
        ArrayList<Pass> reconstructedOutputs = new ArrayList<>();
        List<String> rawLineInfo = Arrays.asList(saveString.split("\n"));
        String[] lineInfo = new String[rawLineInfo.size()];

        ArrayList<String> linkInf = new ArrayList<>();
        ArrayList<String> compInf = new ArrayList<>();

        lineInfo[0] = rawLineInfo.get(0);
        lineInfo[1] = rawLineInfo.get(1);

        for (String s: rawLineInfo.subList(2, rawLineInfo.size()))
            if (s.contains(" -> "))
                linkInf.add(s);
            else
                compInf.add(s);

        for (int i = 2; i < rawLineInfo.size(); i++)
            lineInfo[i] = (i-2) < compInf.size() ?  compInf.get(i - 2) : linkInf.get((i-2) - compInf.size());

        int inputSize = Integer.parseInt(lineInfo[0]);
        int outputSize = Integer.parseInt(lineInfo[1]);

        for (int i = 2; i < lineInfo.length; i++){
            String info = lineInfo[i];
            if (!info.contains(" -> ")) {
                int numberID = Integer.MAX_VALUE;

                try {
                    numberID = Integer.parseInt(lineInfo[i].substring(1));
                }catch (Exception e){}

                Component reconstructedComp = Component.getCompByChar(info.charAt(0));
                if (numberID < inputSize && !lineInfo[i].contains("ch")) {
                    reconstructedInputs.add((Pass) reconstructedComp);
                }
                else if (numberID < inputSize + outputSize && !lineInfo[i].contains("ch")) {
                    reconstructedOutputs.add((Pass) reconstructedComp);
                }
                if (reconstructedComp == null)
                    return null;
                reconstructedComp.name = info;
                reconstructedComponents.add(reconstructedComp);
            } else if(info.contains(" -> ")){
                String[] linkInfo = info.split(" -> ");
                Link reconstructedLink = new Link();
                Component comp1 = null;
                Component comp2 = null;
                for (Component c: reconstructedComponents)
                    if (c.name.equals(linkInfo[0].substring(1)))
                        comp1 = c;
                for (Component c: reconstructedComponents)
                    if (c.name.equals(linkInfo[1].substring(1)))
                        comp2 = c;
                if (comp1 == null || comp2 == null) {
                    System.out.println(info);
                    System.out.println("can't find connected comp");
                    return null;
                }

                if (linkInfo[0].startsWith("O"))
                    ((GeneratingComponent)comp1).addOutput(reconstructedLink);
                else
                    ((ReactiveComponent) comp1).addInput(reconstructedLink);

                if (linkInfo[1].startsWith("O"))
                    ((GeneratingComponent)comp2).addOutput(reconstructedLink);
                else
                    ((ReactiveComponent) comp2).addInput(reconstructedLink);

            }

        }

        ChipImageComponent cic = new ChipImageComponent(inputSize, outputSize);
        ArrayList<ImageComponent.CNode> inputNodes = new ArrayList<>();
        ArrayList<ImageComponent.CNode> outputNodes = new ArrayList<>();

        for (Pass p: reconstructedInputs)
            inputNodes.add(new PassImageComponent(nodeOrbRad,nodeOrbBorderWidth, null, p).inputNodes[0]);
        for (Pass p: reconstructedOutputs)
            outputNodes.add(new PassImageComponent(nodeOrbRad,nodeOrbBorderWidth, null, p).outputNodes[0]);

        cic.setInputNodes(inputNodes.toArray(new ImageComponent.CNode[]{}));
        cic.setOutputNodes(outputNodes.toArray(new ImageComponent.CNode[]{}));

        cic.calcDim();

        return cic.setSaveData(saveString);
    }

    private void saveChip(){
        StringBuilder saveString = new StringBuilder();

        String name =  JOptionPane.showInputDialog(null, "Chip name: ");

        saveString.append(currentChip.inputSize + "\n");
        saveString.append(currentChip.outputSize + "\n");

        for (Component c: board.getComponents())
            saveString.append(c.getName() + "\n");

        for (CableLink cl: cls)
            saveString.append(
                    cl.getFrom().getType()).append(cl.getFrom().parent.getComp().getName())
                    .append(" -> ")
                    .append(cl.getTo().getType()) .append(cl.getTo().parent.getComp().getName())
                    .append("\n");
        subChips.forEach(saveString::append);
        try (PrintWriter out = new PrintWriter(ImageLibrary.RES_URL + ImageLibrary.ESCAPE_CHAR + name +".ch")) {
            out.print(saveString);
        } catch (Exception e){e.printStackTrace();}
    }

    public void cancelChip(){
        inNodes = null;
        outNodes = null;
        currentChip = null;
        subChips = null;
        compID = 0;
        chipID = 0;
        resetContent();
    }
    public void cancelAndExit(){
        cancelChip();
        MainWindow.setCurrentActiveCanvas(MainWindow.bv);
    }

    public boolean startNewChip(Integer inNodes, Integer outNodes){
        if (inNodes > maxInputs || outNodes > maxOutputs || inNodes < 1 || outNodes < 1)
            return false;
        try {
            cancelChip();
        }catch (Exception e){}

        subChips = new ArrayList<>();
        currentChip = new ChipImageComponent(inNodes, outNodes);
        populateNodes();
        return true;
    }
}
