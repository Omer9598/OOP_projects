/**
 * A factory to build the renderer
 */
public class RendererFactory {

    /** Building the renderer according to the given type */
    public Renderer buildRenderer(String type, int size) {
        return switch (type.toLowerCase()) {
            case "console" -> new ConsoleRenderer(size);
            case "none" -> new VoidRenderer();
            default -> null;
        };
    }
}
