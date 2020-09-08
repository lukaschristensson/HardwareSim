package GUI.BoardViewer.CableManipulator;

import GUI.BoardViewer.BoardViewer;
import GUI.BoardViewer.DrawContributer;
import GUI.BoardViewer.ImageComponents.ImageComponent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
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

    public CableLinkEditor(int width, int height, BoardViewer boardViewer){
        super(10);
        this.boardViewer = boardViewer;

        setPrefSize(width, height);
        setAlignment(Pos.CENTER);

        canvasMouseEvent = getOnCanvasClick();

        colorPicker = new ColorPicker(Color.BLACK);

        startDraw = new Button("New Cable");
        startDraw.setOnAction(getStartDrawEvent());

        getChildren().addAll(startDraw, colorPicker);
    }

    private EventHandler<ActionEvent> getStartDrawEvent(){
        return e -> {
            startDraw.setDisable(true);
            currentCable = new CableLink();
            boardViewer.addDrawContributor(this);
            boardViewer.addEventHandler(MouseEvent.MOUSE_CLICKED, canvasMouseEvent);
            boardViewer.removeEventHandler(MouseEvent.MOUSE_CLICKED, boardViewer.placeOrSwitch);
        };
    }

    public void cancelDraw(){
        boardViewer.removeEventHandler(MouseEvent.MOUSE_CLICKED, canvasMouseEvent);
        boardViewer.addEventHandler(MouseEvent.MOUSE_CLICKED, boardViewer.placeOrSwitch);
        startDraw.setDisable(false);
        currentCable = null;
        boardViewer.removeDrawContributor(this);
    }

    private EventHandler<MouseEvent> getOnCanvasClick(){
        return e -> {
            Point mouseP = new Point((int)e.getX(), (int)e.getY());
            ImageComponent.CNode hitNode = boardViewer.getComponentAt(mouseP);
            addPoint(hitNode, mouseP);
            boardViewer.update();
        };
    }

    private void addPoint(ImageComponent.CNode c, Point p){
        if (currentCable.path.size() == 0 && c != null)
            currentCable.startDraw(c, colorPicker.getValue());
        else if (currentCable.path.size() != 0 && c == null)
            currentCable.addPoint(p);
        else if (currentCable.path.size() >= 1) {
            if(currentCable.completePath(c)) {
                boardViewer.addCable(currentCable);
            }
            cancelDraw();
            boardViewer.update();
        }
    }

    @Override
    public void drawSelf(GraphicsContext gc) {
        if (currentCable != null && currentCable.path.size() > 1) {
            currentCable.drawSelf(gc);
        }
    }

    @Override
    public boolean isEnabled() {
        return currentCable != null;
    }
}
