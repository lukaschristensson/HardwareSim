package UtilPackage;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ImageLibrary {
    private static Map<String, Image> library;
    private static ArrayList<String> accpetedFileTypes = new ArrayList<String>(){{add("jpeg"); add("png"); add("jpg");}};
    public static String ESCAPE_CHAR = "\\";
    public static String RES_URL = ESCAPE_CHAR + "HardwareProj" + ESCAPE_CHAR + "HardWareSimRef" + ESCAPE_CHAR + "Res";

    public static void loadImages() {
        library = new HashMap<>();
        loadFromDir(RES_URL);
    }

    public static Image getImage(String name){
        if (library == null)
            loadImages();
        return library.get(name);
    }

    private static void loadFromDir(String url){
        File dir = new File((url));
        File[] subFiles = dir.listFiles();
            for (File f: subFiles){
                if (f.isDirectory())
                    loadFromDir(f.getAbsolutePath());
            else if (accpetedFileTypes.contains(f.getName().split("\\.")[1])){
                try {
                    FileInputStream iStream = new FileInputStream(f);
                    library.put(f.getName().split("\\.")[0], new Image(iStream));
                }catch (Exception e){e.printStackTrace();}
            }
        }
    }
}
