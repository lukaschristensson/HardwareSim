package GUI.CompMenu;

import GUI.BoardViewer.ImageComponents.ImageComponent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.awt.*;
import java.util.ArrayList;

public class ComponentMenu extends HBox{
    private ArrayList<ComponentMenuItem> menuItems;
    private Dimension dim;
    private FlowPane buttonPane;

    public ComponentMenu(int width, int height){
        this.setPrefSize(width, height);
        this.dim = new Dimension(width, height);

        ScrollPane sp = new ScrollPane();
        sp.setMaxSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
        sp.setStyle("-fx-background-color: blue;");

        getChildren().addAll(sp);
        buttonPane = new FlowPane(4, 4);
        buttonPane.setPadding(new Insets(5));
        buttonPane.setMaxWidth(width - 2);
        HBox.setHgrow(buttonPane, Priority.ALWAYS);
        buttonPane.setAlignment(Pos.TOP_LEFT);
        sp.setContent(buttonPane);


        menuItems = new ArrayList<>();
    }


    public ComponentMenuItem addMenuItem(ImageComponent im) {
        ComponentMenuItem cmi = im.getMenuItem();
        menuItems.add(cmi);
        buttonPane.getChildren().addAll(cmi);
        return cmi;
    }
}
