package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Reads a MapFile in order to create a Cell[][].
 *
 * @author Pedro Caetano
 * @author James Henry
 * @author Piotr Obara
 */
class MapReader {
    private final Log log = new Log(getClass().getSimpleName());

    /**
     * Reads provided file line by line, character by character.
     * Creates a Cell array representation of the Map.
     *
     * @param fileName File location of the map
     * @return Completed Cell[][] of the Map
     */
    Cell[][] readFile(String fileName) {
        log.setImportance(1);
        File inputFile = new File(fileName);
        Scanner in = null;
        try {
            in = new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            System.out.println("Cannot open " + fileName);
            System.exit(-1);
        }
        Scanner mapSizeLine = new Scanner(in.nextLine());
        mapSizeLine.useDelimiter(",");
        int maxX = mapSizeLine.nextInt();
        int maxY = mapSizeLine.nextInt();
        Scanner mapSpawnLine = new Scanner(in.nextLine());
        mapSpawnLine.useDelimiter(",");
        Cell[][] cells = new Cell[maxX][maxY];
        log.add("Map Size: " + maxX + "," + maxY);
        readLayout(cells, in, maxX, maxY);
        readSpecials(cells, in);
        int spawnX = mapSpawnLine.nextInt();
        int spawnY = mapSpawnLine.nextInt();
        cells[spawnX][spawnY].setSpawn(true);
        return cells;
    }

    /**
     * Reads the layout of the map and converts it into a cell array.
     *
     * @param cells Cell array to read into
     * @param in    Scanner containing file
     * @param maxX  Size of the X axis
     * @param maxY  Size of the Y axis
     */
    private void readLayout(Cell[][] cells, Scanner in, int maxX, int maxY) {
        for (int y = 0; y < maxY; y++) {
            Scanner line = new Scanner(in.nextLine());
            char[] currentLine = line.nextLine().toCharArray();
            for (int x = 0; x < maxX; x++) {
                char c = currentLine[x];
                Cell currentCell = null;
                if (c == '#') {
                    log.add("Wall: " + x + ", " + y);
                    currentCell = new Wall(x, y);
                } else if (c == ' ') {
                    log.add("Ground: " + x + ", " + y);
                    currentCell = new Ground(x, y);
                } else if (c == '*') {
                    log.add("SnowGround: " + x + ", " + y);
                    currentCell = new SnowGround(x, y);
                } else if (c == 's') {
                    log.add("SnowWall: " + x + ", " + y);
                    currentCell = new SnowWall(x, y);
                } else if (c == 'F') {
                    log.add("Wind: " + x + ", " + y);
                    currentCell = new Wind(x, y);
                } else if (c == 'W') {
                    log.add("Rain: " + x + ", " + y);
                    currentCell = new Rain(x, y);
                } else if (c == 'R') {
                    log.add("redDoor: " + x + ", " + y);
                    currentCell = new KeyDoor(x, y, ColorEnum.RED);
                } else if (c == 'G') {
                    log.add("greenDoor: " + x + ", " + y);
                    currentCell = new KeyDoor(x, y, ColorEnum.GREEN);
                } else if (c == 'B') {
                    log.add("blueDoor: " + x + ", " + y);
                    currentCell = new KeyDoor(x, y, ColorEnum.BLUE);
                } else if (c == '!') {
                    log.add("Target: " + x + ", " + y);
                    currentCell = new Target(x, y);
                } else if (c == 'I') {
                    log.add("Ice cell " + x + ", " + y);
                    currentCell = new Ice(x, y);
                } else if (c == 'C') {
                    log.add("IceCubes: " + x + ", " + y);
                    currentCell = new IceCubes(x, y);
                } else if (c == 'r') {
                    log.add("Ground with redKey: " + x + ", " + y);
                    currentCell = new Ground(x, y);
                    currentCell.setItem(new Key(ColorEnum.RED));
                    currentCell.getItem().setCurrentCell(currentCell);
                } else if (c == 'g') {
                    log.add("Ground with greenKey: " + x + ", " + y);
                    currentCell = new Ground(x, y);
                    currentCell.setItem(new Key(ColorEnum.GREEN));
                    currentCell.getItem().setCurrentCell(currentCell);
                } else if (c == 'b') {
                    log.add("Ground with blueKey: " + x + ", " + y);
                    currentCell = new Ground(x, y);
                    currentCell.setItem(new Key(ColorEnum.BLUE));
                    currentCell.getItem().setCurrentCell(currentCell);
                } else if (c == 'S') {
                    log.add("Ground with Sword: " + x + ", " + y);
                    currentCell = new Ground(x, y);
                    currentCell.setItem(new Sword());
                    currentCell.getItem().setCurrentCell(currentCell);
                } else if (c == 'P') {
                    log.add("Ground with Picexe: " + x + ", " + y);
                    currentCell = new Ground(x, y);
                    currentCell.setItem(new Pickaxe());
                    currentCell.getItem().setCurrentCell(currentCell);
                } else if (c == 'f') {
                    log.add("Ground with windJacket: " + x + ", " + y);
                    currentCell = new Ground(x, y);
                    currentCell.setItem(new WindJacket());
                    currentCell.getItem().setCurrentCell(currentCell);
                } else if (c == 'w') {
                    log.add("Ground with windJacket: " + x + ", " + y);
                    currentCell = new Ground(x, y);
                    currentCell.setItem(new RainUmbrella());
                    currentCell.getItem().setCurrentCell(currentCell);
                } else if (c == 't') {
                    log.add("Ground with token: " + x + ", " + y);
                    currentCell = new Ground(x, y);
                    currentCell.setItem(new Token());
                    currentCell.getItem().setCurrentCell(currentCell);
                } else {
                    log.add("Invalid Input: " + x + ", " + y, 1);
                    System.exit(-1);
                }
                cells[x][y] = currentCell;
            }
        }
    }

    /**
     * Reads the special Cells/Items/Characters of the map and adds them to the Cell array.
     *
     * @param cells Cell array to read into
     * @param in    Scanner containing file
     */
    private void readSpecials(Cell[][] cells, Scanner in) {
        while (in.hasNextLine()) {
            Scanner line = new Scanner(in.nextLine());
            line.useDelimiter(",");
            String token = line.next();
            int x;
            int y;
            int speed;
            String direction;
            if ("enemyStraight".equals(token)) {
                x = line.nextInt();
                y = line.nextInt();
                speed = line.nextInt();
                direction = line.next();
                cells[x][y] = new Ground(x, y);
                cells[x][y].setCharacter(new StraightLine(speed, readDirection(direction)));
                cells[x][y].getOccupier().setCurrentCell(cells[x][y]);
                log.add("EnemyStraight: " + x + ", " + y);
            } else if ("enemySmart".equals(token)) {
                x = line.nextInt();
                y = line.nextInt();
                speed = line.nextInt();
                cells[x][y] = new Ground(x, y);
                cells[x][y].setCharacter(new SmartTarget(speed));
                cells[x][y].getOccupier().setCurrentCell(cells[x][y]);
                log.add("EnemySmart: " + x + ", " + y);
            } else if ("enemyDumb".equals(token)) {
                x = line.nextInt();
                y = line.nextInt();
                cells[x][y] = new Ground(x, y);
                cells[x][y].setCharacter(new DumbTarget(line.nextInt()));
                cells[x][y].getOccupier().setCurrentCell(cells[x][y]);
                log.add("EnemyDumb: " + x + ", " + y);
            } else if ("enemyWall".equals(token)) {
                x = line.nextInt();
                y = line.nextInt();
                speed = line.nextInt();
                direction = line.next();
                cells[x][y] = new Ground(x, y);
                cells[x][y].setCharacter(new WallFollow(speed, readDirection(direction)));
                cells[x][y].getOccupier().setCurrentCell(cells[x][y]);
                log.add("enemyWall: " + x + ", " + y);
            } else if ("tokenDoor".equals(token)) {
                x = line.nextInt();
                y = line.nextInt();
                int requirement = line.nextInt();
                cells[x][y] = new TokenDoor(x, y, requirement);
                log.add("TokenDoor: " + x + ", " + y);
            } else if ("teleporter".equals(token)) {
                x = line.nextInt();
                y = line.nextInt();
                int xx = line.nextInt();
                int yy = line.nextInt();
                Teleporter t1 = new Teleporter(x, y);
                Teleporter t2 = new Teleporter(xx, yy);
                t1.setPairedTeleporter(t2);
                t2.setPairedTeleporter(t1);
                cells[x][y] = t1;
                cells[xx][yy] = t2;
                log.add("Teleporter: " + x + ", " + y);
                log.add("Teleporter: " + xx + ", " + yy);
            } else {
                log.add("Invalid input: " + token, 1);
            }
        }
    }

    /**
     * Reads direction and finds correct Enum type.
     *
     * @param dir direction
     * @return direction as an enumerated type
     */
    private Direction readDirection(String dir) {
        if ("LEFT".equals(dir)) {
            return Direction.LEFT;
        } else if ("RIGHT".equals(dir)) {
            return Direction.RIGHT;
        } else if ("UP".equals(dir)) {
            return Direction.UP;
        } else if ("DOWN".equals(dir)) {
            return Direction.DOWN;
        }
        log.add("Invalid Direction", 1);
        return null;
    }
}
