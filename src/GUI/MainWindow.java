package GUI;

import EventWorker.EventWorker;
import GUI.BoardViewer.BoardViewer;
import GUI.BoardViewer.CableManipulator.CableLinkEditor;
import GUI.BoardViewer.ImageComponents.*;
import GUI.CompMenu.ComponentMenu;
import TimeHandle.SuperClock;
import UtilPackage.Cursor;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

public class MainWindow extends Application {

    private Timer superClockTimer;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group pGroup = new Group();
        Scene pScene = new Scene(pGroup);
        primaryStage.setScene(pScene);


        HBox mainHbox = new HBox();
        pGroup.getChildren().addAll(mainHbox);

        ComponentMenu cm = new ComponentMenu(200, 300);
        BoardViewer bv = new BoardViewer(700, 500);
        CableLinkEditor cle = new CableLinkEditor(200, 200, bv);

        VBox cmcle = new VBox();
        cmcle.getChildren().addAll(cm, cle);

        mainHbox.getChildren().addAll(cmcle, bv);

        cm.addMenuItem(new ButtonImageComponent());
        cm.addMenuItem(new LampImageComponent());
        cm.addMenuItem(new SplitterImageComponent());
        cm.addMenuItem(new LeverImageComponent());
        cm.addMenuItem(new NANDGateImageComponent());
        //ugly way of doing it but i need to disable the clock component of the imagecomponent, sorry
        ClockImageComponent cic = new ClockImageComponent();
        cic.toggleActive();
        cm.addMenuItem(cic);


        //EventWorker.setPrintStream(System.out);
        SuperClock.addTimeDependant(new EventWorker());

        superClockTimer = new Timer();
        superClockTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SuperClock.tick();
            }
        }, 0, 5);
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                bv.update();

            }
        }.start();

        Cursor.setSceneWithCanvas(pScene, bv);
        bv.addDrawContributor(Cursor.getShadowComp());

        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        superClockTimer.cancel();
        superClockTimer.purge();
    }
}
