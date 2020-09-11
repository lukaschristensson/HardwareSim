package CompBoard.Components;

public interface GeneratingComponent {
    int getOutputSize();
    boolean setOutputs(Link[] out);
    boolean addOutput(Link out);
    boolean removeOutput(Link l);
    String generate(boolean forced);
    String generate();
}
