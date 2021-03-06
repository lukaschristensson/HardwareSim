package GUI.BoardViewer.CableManipulator;

import CompBoard.Components.GeneratingComponent;
import CompBoard.Components.Link;
import CompBoard.Components.ReactiveComponent;
import GUI.BoardViewer.ImageComponents.ImageComponent;
import GUI.UndoEvent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class CableLink implements Observer {
    public Link l;
    private Color color;
    private Integer width = 3;
    private ImageComponent.CNode from;
    private ImageComponent.CNode to;
    ArrayList<Point> path;
    public boolean completed;

    public ImageComponent.CNode getFrom() {
        return from;
    }

    public ImageComponent.CNode getTo() {
        return to;
    }

    CableLink(){
        l = new Link();
        completed = false;
        path = new ArrayList<>();
    }

    void startDraw(ImageComponent.CNode node, Color color){
        if (node != null) {
            this.color = color;
            from = node;
            path.add(node.getPosition());
        }
    }

    public void addPoint(int x, int y){
        addPoint(new Point(x, y));
    }
    void addPoint(Point p){
        if (from != null)
            path.add(p);
    }

    UndoEvent completePath(ImageComponent.CNode node){
        this.to = node;
        addPoint(node.getPosition());
        completed = true;

        if(from != to && to.setLink(this) && from.setLink(this)) {
            l.addObserver(this);
            color = l.state.getAsBool() ? Color.RED:Color.BLACK;
            return ()->{
                if (from.getType() == ImageComponent.NodeType.INPUT)
                    ((ReactiveComponent)from.parent.getComp()).removeInput(l);
                else
                    ((GeneratingComponent)from.parent.getComp()).removeOutput(l);

                if (to.getType() == ImageComponent.NodeType.INPUT)
                    ((ReactiveComponent)to.parent.getComp()).removeInput(l);
                else
                    ((GeneratingComponent)to.parent.getComp()).removeOutput(l);
                from.clearCable();
                to.clearCable();
            };
        } else {
            from.clearCable();
            to.clearCable();
            return null;
        }
    }

    public void drawSelf(GraphicsContext gc){
        gc.setStroke(color);
        gc.setLineWidth(width);
        Point lastp = path.get(0);
        for (int i = 1; i < path.size(); i++){
            gc.strokeLine(lastp.x, lastp.y, path.get(i).x, path.get(i).y);
            lastp = path.get(i);
        }
    }

    public void drawSelf(GraphicsContext gc, Point bufferP) {
        gc.setStroke(color);
        gc.setLineWidth(width);
        Point lastp = path.get(0);
        for (int i = 1; i < path.size(); i++){
            gc.strokeLine(lastp.x, lastp.y, path.get(i).x, path.get(i).y);
            lastp = path.get(i);
        }
        if (bufferP != null){
            gc.strokeLine(lastp.x, lastp.y, bufferP.x, bufferP.y);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        color = (boolean)arg ? Color.RED:Color.BLACK;
    }
}
