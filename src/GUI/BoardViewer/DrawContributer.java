package GUI.BoardViewer;

import javafx.scene.canvas.GraphicsContext;

public interface DrawContributer {
    void drawSelf(GraphicsContext gc);
    boolean isEnabled();
}
