package UtilPackage;

import GUI.BoardViewer.DrawContributer;
import GUI.BoardViewer.ImageComponents.ImageComponent;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.awt.*;

public class Cursor {
    private static Object heldObject;
    private static ImageCursor image;
    private static Scene mScene;
    private static Point latestMouseP;

    public static void setSceneWithCanvas(Scene scene, Canvas c) {
        mScene = scene;
        c.addEventHandler(MouseEvent.MOUSE_MOVED, (e)->latestMouseP = new Point((int)e.getX(), (int)e.getY()));
        scene.setOnMouseClicked(e->{
            if (e.getButton().equals(MouseButton.SECONDARY))
                clearHand();
        });
    }

    public static void setCursorImage(Image i){
        image = new ImageCursor(i);
        mScene.setCursor(image);
    }

    public static void setCursor(javafx.scene.Cursor c){
        mScene.setCursor(c);
    }

    public static boolean hold(Object o) {
        if (heldObject != null)
            return false;
        heldObject = o;
        return true;
    }

    public static void clearHand(){
        heldObject = null;
        mScene.setCursor(javafx.scene.Cursor.DEFAULT);
    }

    public static DrawContributer getShadowComp(){
        return new DrawContributer() {
            @Override
            public void drawSelf(GraphicsContext gc) {
                gc.setEffect(new ColorAdjust(0, 0, -0.5, 0));
                ImageComponent imc = (ImageComponent)heldObject;
                Image im = ImageLibrary.getImage((imc).getImages()[0]);
                gc.drawImage(im, latestMouseP.x, latestMouseP.y, imc.getSize().width, imc.getSize().height);
                gc.setEffect(null);
            }

            @Override
            public boolean isEnabled() {
                return heldObject instanceof ImageComponent;
            }
        };
    }

    public static Object getHeldObject(){
        return heldObject;
    }
}