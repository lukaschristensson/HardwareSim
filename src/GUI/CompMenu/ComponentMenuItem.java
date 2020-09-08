package GUI.CompMenu;


import GUI.BoardViewer.ImageComponents.ImageComponent;
import UtilPackage.Cursor;
import UtilPackage.ImageLibrary;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.awt.*;

public abstract class ComponentMenuItem extends Button {
    private static Dimension BUTTON_DIMENSION = new Dimension(60,60);
    protected ComponentMenuItem(){
        this.setOnMouseClicked(getMouseOnClickEvent());

        this.setPrefWidth(BUTTON_DIMENSION.width);
        this.setPrefHeight(BUTTON_DIMENSION.height);
        this.setBackground(new Background(null,
                new BackgroundImage(ImageLibrary.getImage(getImageComp().getImages()[0]), BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(BUTTON_DIMENSION.width, BUTTON_DIMENSION.height,false,false,false,false)
        ),null,null));
    }

    public abstract ImageComponent getImageComp();
    private EventHandler<MouseEvent> getMouseOnClickEvent() {
        return (e) -> {
            if (e.getButton().equals(MouseButton.PRIMARY)){
                UtilPackage.Cursor.setCursorImage(ImageLibrary.getImage(getImageComp().getImages()[0]));
                Cursor.hold(getImageComp().getEmptyCopy());
            }
        };
    }
}