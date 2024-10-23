package edu.uob;

import org.junit.jupiter.api.Test;
import java.nio.file.Paths;
import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EntitiesTests {

    @Test
    void testBasicEntitiesFileIsReadable() {
        File entitiesFile = Paths.get("config" + File.separator + "basic-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "basic-actions.xml").toAbsolutePath().toFile();
        GameServer server = new GameServer(entitiesFile, actionsFile);
        GameState state = server.getState();
        GameMap map = state.getMap();
        Map<String, GameLocation> locations = map.getLocations();
        assertEquals("cabin", locations.get("cabin").getName(), "Expected location to be cabin");
        assertEquals("A log cabin in the woods", locations.get("cabin").getDescription(), "Saved description is incorrect");
        assertTrue(locations.get("cabin").getEntities().containsKey("potion"), "Expected artifact to be potion");
        assertEquals("Magic potion", locations.get("cabin").getEntities().get("potion").getDescription(), "Saved description is incorrect");
        assertTrue(locations.get("cabin").getEntities().containsKey("trapdoor"), "Expected furniture to be trapdoor");
        assertEquals("Wooden trapdoor", locations.get("cabin").getEntities().get("trapdoor").getDescription(), "Saved description is incorrect");
        assertEquals(2, locations.get("cabin").getEntities().size(), "Only two entities expected");
        assertEquals(1, locations.get("cabin").getPaths().size(), "Only one path expected");
        assertTrue(locations.get("cabin").getPaths().get("forest").getName().contains("forest"), "Path expected to be to the forest");

        assertEquals("forest", locations.get("forest").getName(), "Expected location to be forest");
        assertEquals("A dark forest", locations.get("forest").getDescription(), "Saved description is incorrect");
        assertTrue(locations.get("forest").getEntities().containsKey("key"), "Expected artifact to be key");
        assertEquals("Brass key", locations.get("forest").getEntities().get("key").getDescription(), "Saved description is incorrect");
        assertEquals(2, locations.get("forest").getEntities().size(), "Only two entities expected");
        assertTrue(locations.get("forest").getEntities().containsKey("tree"), "Expected furniture to be tree");
        assertEquals("A big tree", locations.get("forest").getEntities().get("tree").getDescription(), "Saved description is incorrect");
        assertEquals(1, locations.get("forest").getPaths().size(), "Only one path expected");
        assertTrue(locations.get("forest").getPaths().get("cabin").getName().contains("cabin"), "Path expected to be to the cabin");

        assertEquals("cellar", locations.get("cellar").getName(), "Expected location to be cellar");
        assertEquals("A dusty cellar", locations.get("cellar").getDescription(), "Saved description is incorrect");
        assertTrue(locations.get("cellar").getEntities().containsKey("elf"), "Expected character to be elf");
        assertEquals("Angry Elf", locations.get("cellar").getEntities().get("elf").getDescription(), "Saved description is incorrect");
        assertEquals(1, locations.get("cellar").getEntities().size(), "Only one entity expected");
        assertEquals(1, locations.get("cellar").getPaths().size(), "Only one path expected");
        assertTrue(locations.get("cellar").getPaths().get("cabin").getName().contains("cabin"), "Path expected to be to the cabin");

        assertEquals("storeroom", locations.get("storeroom").getName(), "Expected location to be storeroom");
        assertEquals("Storage for any entities not placed in the game", locations.get("storeroom").getDescription(), "Saved description is incorrect");
        assertTrue(locations.get("storeroom").getEntities().containsKey("log"), "Expected artifact to be log");
        assertEquals("A heavy wooden log", locations.get("storeroom").getEntities().get("log").getDescription(), "Saved description is incorrect");
        assertEquals(1, locations.get("storeroom").getEntities().size(), "Only one entity expected");
        assertEquals(0, locations.get("storeroom").getPaths().size(), "No path expected");

    }

    @Test
    void testExtendedMoves(){
        File entitiesFile = Paths.get("config" + File.separator + "extended-entities.dot").toAbsolutePath().toFile();
        File actionsFile = Paths.get("config" + File.separator + "extended-actions.xml").toAbsolutePath().toFile();
        GameServer server = new GameServer(entitiesFile, actionsFile);
        GameState state = server.getState();
        GameMap map = state.getMap();
        Map<String, GameLocation> locations = map.getLocations();
        assertEquals("cabin", locations.get("cabin").getName(), "Expected location to be cabin");
        assertEquals("A log cabin in the woods", locations.get("cabin").getDescription(), "Saved description is incorrect");
        assertTrue(locations.get("cabin").getEntities().containsKey("potion"), "Expected artifact to be potion");
        assertEquals("A bottle of magic potion", locations.get("cabin").getEntities().get("potion").getDescription(), "Saved description is incorrect");
        assertTrue(locations.get("cabin").getEntities().containsKey("axe"), "Expected artifact to be axe");
        assertEquals("A razor sharp axe", locations.get("cabin").getEntities().get("axe").getDescription(), "Saved description is incorrect");
        assertTrue(locations.get("cabin").getEntities().containsKey("coin"), "Expected artifact to be coin");
        assertEquals("A silver coin", locations.get("cabin").getEntities().get("coin").getDescription(), "Saved description is incorrect");
        assertTrue(locations.get("cabin").getEntities().containsKey("trapdoor"), "Expected furniture to be trapdoor");
        assertEquals("A locked wooden trapdoor in the floor", locations.get("cabin").getEntities().get("trapdoor").getDescription(), "Saved description is incorrect");
        assertEquals(4, locations.get("cabin").getEntities().size(), "Only four entities expected");
        assertEquals(1, locations.get("cabin").getPaths().size(), "Only one path expected");
        assertTrue(locations.get("cabin").getPaths().get("forest").getName().contains("forest"), "Path expected to be to the forest");

        assertEquals("forest", locations.get("forest").getName(), "Expected location to be forest");
        assertEquals("A deep dark forest", locations.get("forest").getDescription(), "Saved description is incorrect");
        assertTrue(locations.get("forest").getEntities().containsKey("key"), "Expected artifact to be key");
        assertEquals("A rusty old key", locations.get("forest").getEntities().get("key").getDescription(), "Saved description is incorrect");
        assertTrue(locations.get("forest").getEntities().containsKey("tree"), "Expected furniture to be tree");
        assertEquals("A tall pine tree", locations.get("forest").getEntities().get("tree").getDescription(), "Saved description is incorrect");
        assertEquals(2, locations.get("forest").getEntities().size(), "Only two entities expected");
        assertEquals(2, locations.get("forest").getPaths().size(), "Two paths expected");
        assertTrue(locations.get("forest").getPaths().get("cabin").getName().contains("cabin"), "Path expected to be to the cabin");
        assertTrue(locations.get("forest").getPaths().get("riverbank").getName().contains("riverbank"), "Path expected to be to the riverbank");

        assertEquals("cellar", locations.get("cellar").getName(), "Expected location to be cellar");
        assertEquals("A dusty cellar", locations.get("cellar").getDescription(), "Saved description is incorrect");
        assertTrue(locations.get("cellar").getEntities().containsKey("elf"), "Expected character to be elf");
        assertEquals("An angry looking Elf", locations.get("cellar").getEntities().get("elf").getDescription(), "Saved description is incorrect");
        assertEquals(1, locations.get("cellar").getEntities().size(), "Only one entity expected");
        assertEquals(1, locations.get("cellar").getPaths().size(), "Only one path expected");
        assertTrue(locations.get("cellar").getPaths().get("cabin").getName().contains("cabin"), "Path expected to be to the cabin");

        assertEquals("riverbank", locations.get("riverbank").getName(), "Expected location to be riverbank");
        assertEquals("A grassy riverbank", locations.get("riverbank").getDescription(), "Saved description is incorrect");
        assertTrue(locations.get("riverbank").getEntities().containsKey("horn"), "Expected artifact to be key");
        assertEquals("An old brass horn", locations.get("riverbank").getEntities().get("horn").getDescription(), "Saved description is incorrect");
        assertTrue(locations.get("riverbank").getEntities().containsKey("river"), "Expected character to be river");
        assertEquals("A fast flowing river", locations.get("riverbank").getEntities().get("river").getDescription(), "Saved description is incorrect");
        assertEquals(2, locations.get("riverbank").getEntities().size(), "Only two entities expected");
        assertEquals(1, locations.get("riverbank").getPaths().size(), "Only one path expected");
        assertTrue(locations.get("riverbank").getPaths().get("forest").getName().contains("forest"), "Path expected to be to the forest");

        assertEquals("clearing", locations.get("clearing").getName(), "Expected location to be clearing");
        assertEquals("A clearing in the woods", locations.get("clearing").getDescription(), "Saved description is incorrect");
        assertTrue(locations.get("clearing").getEntities().containsKey("ground"), "Expected furniture to be ground");
        assertEquals("It looks like the soil has been recently disturbed", locations.get("clearing").getEntities().get("ground").getDescription(), "Saved description is incorrect");
        assertEquals(1, locations.get("clearing").getEntities().size(), "One entity expected");
        assertEquals(1, locations.get("clearing").getPaths().size(), "Only one path expected");
        assertTrue(locations.get("clearing").getPaths().get("riverbank").getName().contains("riverbank"), "Path expected to be to the riverbank");

        assertEquals("storeroom", locations.get("storeroom").getName(), "Expected location to be storeroom");
        assertEquals("Storage for any entities not placed in the game", locations.get("storeroom").getDescription(), "Saved description is incorrect");
        assertTrue(locations.get("storeroom").getEntities().containsKey("log"), "Expected artifact to be log");
        assertEquals("A heavy wooden log", locations.get("storeroom").getEntities().get("log").getDescription(), "Saved description is incorrect");
        assertTrue(locations.get("storeroom").getEntities().containsKey("shovel"), "Expected artifact to be shovel");
        assertEquals("A sturdy shovel", locations.get("storeroom").getEntities().get("shovel").getDescription(), "Saved description is incorrect");
        assertTrue(locations.get("storeroom").getEntities().containsKey("gold"), "Expected artifact to be gold");
        assertEquals("A big pot of gold", locations.get("storeroom").getEntities().get("gold").getDescription(), "Saved description is incorrect");
        assertEquals(5, locations.get("storeroom").getEntities().size(), "Only three entities expected");
        assertTrue(locations.get("storeroom").getEntities().containsKey("hole"), "Expected furniture to be hole");
        assertEquals("A deep hole in the ground", locations.get("storeroom").getEntities().get("hole").getDescription(), "Saved description is incorrect");
        assertTrue(locations.get("storeroom").getEntities().containsKey("lumberjack"), "Lumberjack expected in the storeroom");
        assertEquals("A burly wood cutter", locations.get("storeroom").getEntities().get("lumberjack").getDescription(), "Saved description is incorrect");
        assertEquals(0, locations.get("storeroom").getPaths().size(), "No path expected");
    }
}
