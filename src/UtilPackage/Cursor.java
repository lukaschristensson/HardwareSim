package UtilPackage;

import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;

public class Cursor {
    private static Object heldObject;
    private static ImageCursor image;
    private static Scene mScene;

    public static void setScene(Scene scene) {
        mScene = scene;
        scene.setOnMouseClicked(e->{
            if (e.getButton().equals(MouseButton.SECONDARY))
                clearHand();
        });
    }

    public static void setCursorImage(Image i){
        image = new ImageCursor(i);
        mScene.setCursor(image);
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
    public static Object getHeldObject(){
        return heldObject;
    }
}