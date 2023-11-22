public class RendererFactory {

    /** Building the renderer according to the given type */
    public Renderer buildRenderer(String renderer_type, int size) {
        return switch (renderer_type.toLowerCase()) {
            case "console" -> new ConsoleRenderer(size);
            case "none" -> new VoidRenderer();
            default -> null;
        };
    }
}
