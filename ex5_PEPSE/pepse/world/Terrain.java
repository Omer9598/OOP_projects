package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;

public class Terrain {
    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private int groundHeightAtX0;

    public Terrain(GameObjectCollection gameObjects,
                   int groundLayer, Vector2 windowDimensions,
                   int seed) {

        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
    }

}
