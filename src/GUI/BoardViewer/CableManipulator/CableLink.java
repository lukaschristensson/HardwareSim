package GUI.BoardViewer.CableManipulator;

import CompBoard.Components.Link;
import GUI.BoardViewer.ImageComponents.ImageComponent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class CableLink implements Observer {
    public Link l;
    Color color;
    Integer width = 3;
    ImageComponent.CNode from;
    ImageComponent.CNode to;
    ArrayList<Point> path;
    public boolean completed;

    public ImageComponent.CNode getFrom() {
        return from;
    }

    public ImageComponent.CNode getTo() {
        return to;
    }

    public CableLink(){
        l = new Link();
        completed = false;
        path = new ArrayList<>();
    }

    public boolean startDraw(ImageComponent.CNode node, Color color){
        if (node == null)
            return false;
        this.color = color;
        from = node;
        path.add(node.getPosition());
        return true;
    }

    public void addPoint(int x, int y){
        addPoint(new Point(x, y));
    }
    public void addPoint(Point p){
        if (from != null)
            path.add(p);
    }

    public boolean completePath(ImageComponent.CNode node){
        this.to = node;
        addPoint(node.getPosition());
        completed = true;

        if(from != to && to.setLink(this) && from.setLink(this)) {
            l.addObserver(this);
            return true;
        } else {
            from.clearCable();
            to.clearCable();
            return false;
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
        if ((boolean)arg)
            color = Color.RED;
        else
            color = Color.BLACK;
    }
}
