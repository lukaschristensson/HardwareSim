package CompBoard.Components;

public interface GeneratingComponent {
    int getOutputSize();
    boolean setOutputs(Link[] out);
    boolean addOutput(Link out);
    String generate();
}
