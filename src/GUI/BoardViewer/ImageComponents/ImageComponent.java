package GUI.BoardViewer.ImageComponents;

import CompBoard.Components.*;
import CompBoard.Components.Component;
import GUI.BoardViewer.CableManipulator.CableLink;
import GUI.CompMenu.ComponentMenuItem;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;


public abstract class ImageComponent{
    protected static final Color INPUT_NODE_COLOR = Color.CYAN.darker();
    protected static final Color OUTPUT_NODE_COLOR = Color.ORANGE.darker();
    public static final int EXTERNAL_NODE_SIZE = 10;
    public Dimension dim;
    public Point pos;
    String[] images;
    public CNode[] inputNodes;
    public CNode[] outputNodes;

    public ImageComponent(){
        this(null);
    }

    public ImageComponent(Dimension dim){
        this(null, dim);
    }
    public ImageComponent(Point pos, Dimension dim) {
        this.dim = dim;
        setPos(pos);
        if (images == null)
            setImageStrings();
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }

    public CNode containsNodeAt(Point p){
        for (CNode n: inputNodes)
        if (n != null && n.contains(p))
            return n;
        for (CNode n: outputNodes)
            if (n != null && n.contains(p))
                return n;
        return null;
    }
    public boolean contains(Point p){
        return getHitBox().contains(new Point2D(p.x,p.y));
    }

    public String[] getImages() {
        return images;
    }

    private Bounds getHitBox(){
         return new Rectangle(pos.x,pos.y,dim.width,dim.height).getBoundsInLocal();
    }
    protected void drawNodes(GraphicsContext gc){
        if (inputNodes != null &&  outputNodes != null){
            for (CNode n: inputNodes)
                n.drawSelf(gc, INPUT_NODE_COLOR);

            for (CNode n: outputNodes)
                n.drawSelf(gc, OUTPUT_NODE_COLOR);
        }
    }

    public void recalculateNodes(){
        if (dim != null){
            Integer[] nodeDim = getExternalPointDimensions();
            inputNodes = new CNode[nodeDim[0]];
            outputNodes = new CNode[nodeDim[1]];

            Integer nodeID = 0;

            for (int i = 0; i < nodeDim[0]; i++) {
                inputNodes[i] = new CNode(pos == null? null: new Point(pos.x, pos.y + (dim.height / (nodeDim[0] + 1)) * (i + 1)), nodeID, this, NodeType.INPUT);
                nodeID++;
            }
            for (int i = 0; i < nodeDim[1]; i++) {
                outputNodes[i] = new CNode(pos == null? null: new Point(pos.x + dim.width, pos.y + (dim.height / (nodeDim[1] + 1)) * (i + 1)), nodeID, this, NodeType.OUTPUT);
                nodeID++;
            }
        }
    }

    public void drawSelf(GraphicsContext gc, Point p){
        Point truePos = pos;
        pos = p;
        drawSelf(gc);
        pos = truePos;
    }
    public ComponentMenuItem getMenuItem(){
        ImageComponent localComponent = this;
        return new ComponentMenuItem() {
            @Override
            public ImageComponent getImageComp() {
                return localComponent;
            }
        };
    }
    public abstract void setImageStrings();
    public abstract Integer[] getExternalPointDimensions();
    public abstract ImageComponent getEmptyCopy();
    public abstract void drawSelf(GraphicsContext gc);
    public abstract Component getComp();
    public void forceGenerate(){
        if (getComp() instanceof GeneratingComponent)
            ((GeneratingComponent) getComp()).generate();
    }

    public static class CNode{
        public ImageComponent parent;
        Point pos;
        Integer nodeID;
        CableLink cable;
        NodeType type;

        CNode(Point pos, Integer nodeID, ImageComponent parent, NodeType type){
            this.nodeID = nodeID;
            this.pos = pos;
            this.parent = parent;
            this.type = type;
        }

        public void setPos(Point pos) {
            this.pos = pos;
        }

        public NodeType getType() {
            return type;
        }

        public boolean contains(Point p){
            return pos.distance(p) < EXTERNAL_NODE_SIZE / 2f;
        }

        public Point getPosition(){
            return pos;
        }

        public void drawSelf(GraphicsContext gc, Color c){
            gc.setFill(c);
            if (pos != null)
                gc.fillOval(pos.x - EXTERNAL_NODE_SIZE/2f,pos.y - EXTERNAL_NODE_SIZE/2f,EXTERNAL_NODE_SIZE,EXTERNAL_NODE_SIZE);
        }

        public boolean setLink(CableLink cb){
            if (this.cable == null) {
                this.cable = cb;
                switch (type) {
                    case OUTPUT:
                        if (((GeneratingComponent)parent.getComp()).addOutput(cable.l)){
                                parent.forceGenerate();
                            return true;
                        }
                        return false;
                    case INPUT:
                        return ((ReactiveComponent) parent.getComp()).addInput(cable.l);
                    default:
                        return false;
                }
            }
            return false;
        }
        public void clearCable(){
            cable = null;
        }
    }
    public enum NodeType{
        OUTPUT, INPUT;

        @Override
        public String toString() {
            switch (this) {
                case OUTPUT:
                    return "O";
                case INPUT:
                    return "I";
                default:
                    return null;
            }
        }
    }
}