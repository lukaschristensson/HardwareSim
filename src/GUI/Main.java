package GUI;

import CompBoard.Components.*;
import EventWorker.EventWorker;
import TimeHandle.SuperClock;
import UtilPackage.ImageLibrary;
import javafx.application.Application;


public class Main {
    public static void main(String[] args) {
        /*
        SuperClock.addTimeDependant(new EventWorker());
        //EventWorker.setPrintStream(System.out);

        Link link1 = new Link();
        Link link2 = new Link();
        Link link3 = new Link();


        Clock cl1 = new Clock(1000, 1000);
        cl1.setName("Clock 1 test ");
        cl1.setOutputs(new Link[]{link1});
        cl1.toggleActive();

        Clock cl2 = new Clock(1000,1000);
        cl2.setName("Clock 2 test ");
        cl2.setOutputs(new Link[]{link2});
        EventWorker.addTriggerEvent((ps)->cl2.toggleActive(),500);

        NANDGate nandGate = new NANDGate();
        nandGate.setName("nandegate test ");
        nandGate.setInputs(new Link[]{link1, link2});
        nandGate.setOutputs(new Link[]{link3});

        Lamp lamp = new Lamp();
        lamp.setName("lamp test ");
        lamp.setInputs(new Link[]{link3});

        //link2.setRaised(true);
        //link2.setSunk(true);

        while(true){
            SuperClock.tick();
        }
         */
        MainWindow mw = new MainWindow();
        ImageLibrary.loadImages();
        mw.run(args);
    }

}