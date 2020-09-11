package GUI.BoardViewer.CableManipulator;

import GUI.BoardViewer.BoardViewer;
import GUI.BoardViewer.DrawContributer;
import GUI.BoardViewer.ImageComponents.ImageComponent;
import GUI.MainWindow;
import GUI.UndoEvent;
import UtilPackage.Cursor;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.awt.*;


public class CableLinkEditor extends VBox implements DrawContributer {
    private BoardViewer boardViewer;
    private Button startDraw;
    private CableLink currentCable;
    private ColorPicker colorPicker;
    private EventHandler<MouseEvent> canvasMouseEvent;
    private Point latestMouseP = new Point(0, 0);
    private EventHandler<MouseEvent> updateLatestMouseP = (e)-> latestMouseP = new Point((int)e.getX(),(int)e.getY());
    private EventHandler<KeyEvent> cableShortCut = (e)-> {
        if (e.getCode() == KeyCode.C)
            startDraw.fire();
    };

    public CableLinkEditor(int width, int height, BoardViewer boardViewer){
        super(10);
        this.boardViewer = boardViewer;

        setPrefSize(width, height);
        setAlignment(Pos.CENTER);

        canvasMouseEvent = getOnCanvasClick();

        colorPicker = new ColorPicker(Color.BLACK);

        startDraw = new Button("New Cable");

        getChildren().addAll(startDraw, colorPicker);
    }

    public void switchBoardViewer(BoardViewer bv){
        this.boardViewer = bv;

        try {
            cancelDraw();
        } catch(Exception e){}
        try {
            boardViewer.removeEventHandler(MouseEvent.MOUSE_MOVED, updateLatestMouseP);
        }catch (Exception e){}
        try {
            boardViewer.removeEventHandler(KeyEvent.KEY_PRESSED, cableShortCut);
        }catch (Exception e){}

        boardViewer.addEventHandler(MouseEvent.MOUSE_MOVED, updateLatestMouseP);
        startDraw.setOnAction(getStartDrawEvent());
        boardViewer.addEventHandler(KeyEvent.KEY_PRESSED, cableShortCut);
    }

    private EventHandler<ActionEvent> getStartDrawEvent(){
        return e -> {
            startDraw.setDisable(true);
            currentCable = new CableLink();
            boardViewer.addDrawContributor(this);
            boardViewer.addEventHandler(MouseEvent.MOUSE_CLICKED, canvasMouseEvent);
            boardViewer.removeEventHandler(MouseEvent.MOUSE_CLICKED, boardViewer.placeOrSwitch);
            Cursor.setCursor(javafx.scene.Cursor.CROSSHAIR);
        };
    }

    public void cancelDraw(){
        boardViewer.removeEventHandler(MouseEvent.MOUSE_CLICKED, canvasMouseEvent);
        boardViewer.addEventHandler(MouseEvent.MOUSE_CLICKED, boardViewer.placeOrSwitch);
        startDraw.setDisable(false);
        currentCable = null;
        Cursor.clearHand();
        boardViewer.removeDrawContributor(this);
    }

    private EventHandler<MouseEvent> getOnCanvasClick(){
        return e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                Point mouseP = new Point((int) e.getX(), (int) e.getY());
                ImageComponent.CNode hitNode = boardViewer.getComponentAt(mouseP);
                addPoint(hitNode, mouseP);
                boardViewer.update();
            } else if (e.getButton() == MouseButton.SECONDARY)
                cancelDraw();
        };
    }

    private void addPoint(ImageComponent.CNode c, Point p){
        if (currentCable.path.size() == 0 && c != null)
            currentCable.startDraw(c, colorPicker.getValue());
        else if (currentCable.path.size() != 0 && c == null)
            currentCable.addPoint(p);
        else if (currentCable.path.size() >= 1) {
            UndoEvent ue = currentCable.completePath(c);
            final CableLink finalCable = currentCable;
            if(ue != null) {
                boardViewer.addCable(finalCable);
                MainWindow.addUndo(()->{
                    ue.undo();
                    boardViewer.removeCable(finalCable);
                });
            }
            cancelDraw();
            boardViewer.update();
        }
    }

    @Override
    public void drawSelf(GraphicsContext gc) {
        if (currentCable != null && currentCable.path.size() >= 1) {
            currentCable.drawSelf(gc, latestMouseP);
        }
    }

    @Override
    public boolean isEnabled() {
        return currentCable != null;
    }
}
