package game;

import menu.GetFileList;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Main class used to test separate modules of the Game package.
 *
 * @author Pedro Caetano
 * @author Artur Zinnurov
 * @author Piotr Obara
 */
class Main {

    /**
     * Instantiates tests on individual modules.
     *
     * @param args Commandline arguments
     */
    public static void main(String[] args) {
        //messageOfTheDay();
        //mapReader();
        //map();
        //colours();
        //getNameTest();
        //profileTest();
        //mapReader();
        //map();
        //colours();
        //getNameTest();
        //testSmartEnemy();
        //testFile();
    }

    /**
     * Tests basic smartEnemy logic.
     */
    public static void testSmartEnemy() {
        GameManager g = new GameManager();
        Map map = new Map("test2Smart.map");
        Profile profile = new Profile("Pedro", 1, null, 0);
        Player player = new Player(profile, g);
        player.currentCell = map.getSpawnCell();
        map.getCell(player.getX(), player.getY()).setCharacter(player);
        g.setCurrentMap(map);
        g.setCurrentPlayer(player);
        g.postMovement();
    }

    /**
     * Tests MOTD module.
     */
    public static void messageOfTheDay() {
        MessageOfTheDay testMessage = new MessageOfTheDay();
    }

    /**
     * Tests mapReader module.
     */
    public static void mapReader() {
        MapReader testMapReader = new MapReader();
        testMapReader.readFile("Test9.map");
    }

    /**
     * Tests Profile timer.
     */
    private static void profileTest() {
        Log log = new Log("Profile Test");
        log.setImportance(0);
        Profile p = new Profile("Pedro", 1, null, 0);
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //log.add(p.getTimeElapsed().toString());
    }

    /**
     * Tests saving a profile.
     */
    public static void saveProfileTest() {
        Profile a = new Profile("test", 2, null, 0);
        GetFileList getFileList = new GetFileList();
        // File[] fileList = getFileList.finderResults("src/");
        File[] profileList = getFileList.finderResults("src/");
        System.out.println(Arrays.toString(profileList));

        a.save(1, 2);
    }


    /**
     * Tests Map module.
     */
    public static void map() {
        Log log = new Log("Main");
        Map testMap = new Map("test2Smart.map");
        ArrayList<Enemy> enemyTest = testMap.getEnemies();
        for (Enemy elem : enemyTest) {
            log.add(elem.toString(), 1);
        }
        log.add("Tokens = " + testMap.getTokenCount(), 2);
    }

    /**
     * Tests FileList class.
     */
    private static void testFile() {
        Log log = new Log("Main");
        GetFileList listMaps = new GetFileList();
        File[] fileList = listMaps.finderMap("C:/Users/Pedro Caetano/Documents/GitKraken/game");
        log.add((fileList[0].toString()));
    }
}
