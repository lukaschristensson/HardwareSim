package GUI;

import EventWorker.EventWorker;
import GUI.BoardViewer.BoardViewer;
import GUI.BoardViewer.CableManipulator.CableLinkEditor;
import GUI.BoardViewer.ChipManipulator.ChipCreator;
import GUI.BoardViewer.ImageComponents.*;
import GUI.CompMenu.ComponentMenu;
import TimeHandle.SuperClock;
import UtilPackage.Cursor;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Timer;
import java.util.TimerTask;

public class MainWindow extends Application {

    private Timer superClockTimer;
    public static BoardViewer currentBV;
    public static VBox mainVBox;
    public static HBox mainHBox;
    public static BoardViewer bv;
    public static ChipCreator cc;
    public static ComponentMenu cm;
    public static CableLinkEditor cle;
    public static Stage pStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        pStage = primaryStage;
        Group pGroup = new Group();
        Scene pScene = new Scene(pGroup);
        primaryStage.setScene(pScene);

        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem clear = new MenuItem("Clear");
        clear.setOnAction(e->{
            if (currentBV == bv)
                currentBV.resetContent();
            else
                setCurrentActiveCanvas(bv);
        });
        fileMenu.getItems().addAll(clear);
        menuBar.getMenus().addAll(fileMenu);

        mainHBox = new HBox();

        cm = new ComponentMenu(200, 300);
        bv = new BoardViewer(700, 500);
        cc = new ChipCreator(700, 500);
        cle = new CableLinkEditor(200, 200, bv);

        menuBar.getMenus().addAll(cc.getAsMenu());

        VBox cmcle = new VBox();
        cmcle.getChildren().addAll(cm, cle);

        mainHBox.getChildren().addAll(cmcle);
        mainVBox = new VBox();
        mainVBox.getChildren().addAll(menuBar, mainHBox);

        pGroup.getChildren().addAll(mainVBox);

        cm.addMenuItem(new ButtonImageComponent());
        cm.addMenuItem(new LampImageComponent());
        cm.addMenuItem(new SplitterImageComponent());
        cm.addMenuItem(new LeverImageComponent());
        cm.addMenuItem(new NANDGateImageComponent());
        //ugly way of doing it but i need to disable the clock component of the imagecomponent, sorry
        ClockImageComponent cic = new ClockImageComponent();
        cic.toggleActive();
        cm.addMenuItem(cic);


        EventWorker.setPrintStream(System.out);
        SuperClock.addTimeDependant(new EventWorker());

        superClockTimer = new Timer();
        superClockTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SuperClock.tick();
            }
        }, 0, 5);

        Cursor.setScene(pScene);

        bv.addDrawContributor(Cursor.getShadowComp());
        cc.addDrawContributor(Cursor.getShadowComp());

        setCurrentActiveCanvas(bv);

        primaryStage.show();
    }

    public static void setCurrentActiveCanvas(BoardViewer bv){
        if (currentBV != null) {
            currentBV.stopUpdate();
            mainHBox.getChildren().set(1, bv);
        } else
            mainHBox.getChildren().addAll(bv);
        currentBV = bv;
        cle.switchBoardViewer(bv);
        Cursor.setCanvas(bv);
        bv.startUpdate();
    }

    @Override
    public void stop() throws Exception {
        superClockTimer.cancel();
        superClockTimer.purge();
    }
}
