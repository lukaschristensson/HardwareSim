package GUI;


import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class FeedbackLabel extends HBox {

    private static Label feedbackLabel;

    public FeedbackLabel(int width, int height){
        feedbackLabel = new Label();
        setPrefSize(width, height);
        getChildren().addAll(feedbackLabel);
    }

    public static void setMessage(String s){
        feedbackLabel.setText(s);
    }

    public static void clearLabel(){
        feedbackLabel.setText("");
    }

}
