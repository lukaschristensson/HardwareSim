package CompBoard.Components;


public interface ReactiveComponent {
    int getInputSize();
    boolean setInputs(Link [] in);
    boolean addInput(Link in);
    String react();
}
