package GUI.BoardViewer;

import CompBoard.Board.Board;
import CompBoard.Components.Component;
import GUI.BoardViewer.CableManipulator.CableLink;
import GUI.BoardViewer.ChipManipulator.ChipImageComponent;
import GUI.BoardViewer.ImageComponents.ClickableComponent;
import GUI.BoardViewer.ImageComponents.ImageComponent;
import UtilPackage.Cursor;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;

public class BoardViewer extends Canvas{
    protected Board board;
    protected ArrayList<CableLink> cls;
    protected ArrayList<ImageComponent> ics;
    private ArrayList<DrawContributer> dcs;
    protected Color backgroundColor = new Color(0.8, 0.8, 0.8, 1);
    public EventHandler<MouseEvent> placeOrSwitch;
    protected AnimationTimer at;

    public BoardViewer(float width, float height){
        super(width,height);
        addEventHandler(MouseEvent.MOUSE_PRESSED, (e)-> requestFocus());
        at = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        board = new Board();
        dcs = new ArrayList<>();
        ics = new ArrayList<>();
        cls = new ArrayList<>();

        placeOrSwitch = e->{
            if (e.getButton() == MouseButton.PRIMARY) {
                Object heldObject = Cursor.getHeldObject();
                if (heldObject instanceof ImageComponent) {
                    ImageComponent im = (ImageComponent) heldObject;
                    im.setPos(new Point((int)e.getX(), (int)e.getY()));
                    im.recalculateNodes();
                    addComponent(im);
                    Cursor.clearHand();
                } else
                    for (ImageComponent ic: ics){
                        if (ic instanceof ClickableComponent && ic.contains(new Point((int)e.getX(), (int)e.getY())))
                            ((ClickableComponent)ic).clicked();
                    }
            }
        };

        addEventHandler(MouseEvent.MOUSE_CLICKED, placeOrSwitch);
        clearCanvas();
    }

    public void resetContent(){
        for (Component c: board.getComponents())
            if (c != null)
                c.deactivate();
        board = new Board();
        for (ImageComponent ic: ics)
            if (ic instanceof ChipImageComponent)
                ((ChipImageComponent) ic).deactivate();
        ics = new ArrayList<>();
        cls = new ArrayList<>();
    }

    public void startUpdate(){
        at.start();
    }
    public void stopUpdate(){
        at.stop();
    }

    public ImageComponent.CNode getComponentAt(Point p){
        ImageComponent.CNode ans;

        for (ImageComponent i: ics){
            ans = i.containsNodeAt(p);
            if (ans != null)
                return ans;
        }

        return null;
    }
    public void addComponent(ImageComponent ic){
        ics.add(ic);
        board.addComponent(ic.getComp());
        update();
    }
    public boolean removeComponent(ImageComponent ic){
        if(ics.remove(ic)){
            board.removeComponent(ic.getComp());
            update();
            return true;
        }
        return false;
    }
    public void addDrawContributor(DrawContributer dc){
        dcs.add(dc);
    }
    public void removeDrawContributor(DrawContributer dc){
        dcs.remove(dc);
    }
    public void addCable(CableLink cl){
        cls.add(cl);
    }
    public void update(){
        GraphicsContext gc = this.getGraphicsContext2D();
        clearCanvas();
        ics.forEach(e-> e.drawSelf(gc));
        cls.forEach(e->{
            if (e.completed)
                e.drawSelf(gc);
        });
        dcs.forEach(e->{
            if(e.isEnabled())
                e.drawSelf(gc);
        });
    }
    public void clearCanvas(){
        getGraphicsContext2D().setFill(backgroundColor);
        getGraphicsContext2D().fillRect(0, 0, getWidth(), getHeight());
    }
}
