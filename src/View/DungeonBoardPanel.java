package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
     * Graphical dungeon board in the center, using the same tiles as the demo.
     * It reads city_map1.txt and draws the map with a hero sprite at the entrance.
     */
  public final class DungeonBoardPanel extends JPanel {
    	
        private static final long serialVersionUID = 1L;

        private static final String MAP_FILE = "city_map1.txt";
        private static final int TILE_SIZE = 60;

        private enum DungeonTile {
            FLOOR, WALL, PIT, VOID,
            ENTRANCE, EXIT,
            DOOR_N, DOOR_S, DOOR_E, DOOR_W,
            HORIZONTAL, VERTICAL, INTERSECTION
        }
        
     // Which way the hero is facing
        private enum Facing { UP, DOWN, LEFT, RIGHT }

        private Facing myFacing = Facing.DOWN;

        // Hero sprite images for each facing
        private final Map<Facing, Image> myHeroImages =
                new EnumMap<>(Facing.class);


        private DungeonTile[][] myGrid;
        private int myRows;
        private int myCols;

        // Where to draw the hero
        private int myHeroX;
        private int myHeroY;

        // Tile images
        private final Map<DungeonTile, Image> myTileImages = new HashMap<>();

        public DungeonBoardPanel(final String heroSpritePath) {

        	loadHeroImages(heroSpritePath);
        	
            // read the ASCII map
            try (Scanner in = new Scanner(new File(MAP_FILE))) {
                readGrid(in);
            } catch (IOException e) {
                // Fallback: 10x10 floor grid if file missing
                myRows = 10;
                myCols = 10;
                myGrid = new DungeonTile[myRows][myCols];
                for (int r = 0; r < myRows; r++) {
                    for (int c = 0; c < myCols; c++) {
                        myGrid[r][c] = DungeonTile.FLOOR;
                    }
                }
                myHeroX = 1;
                myHeroY = 1;
            }

            loadTileImages();
            setPreferredSize(new Dimension(myCols * TILE_SIZE, myRows * TILE_SIZE));
            setBackground(Color.BLACK);
           
        }
        
        /** Load hero sprites for UP/DOWN/LEFT/RIGHT based on a *_down.png path. */
        private void loadHeroImages(final String heroSpritePath) {
            // Default: whatever path we were given is "DOWN"
            Image down = new ImageIcon(heroSpritePath).getImage();
            myHeroImages.put(Facing.DOWN, down);

            // Try to derive base + extension, e.g. "Dungeoneer_Characters/warrior" + "_down" + ".png"
            int dotIndex = heroSpritePath.lastIndexOf('.');
            int underscoreIndex = heroSpritePath.lastIndexOf('_');

            if (dotIndex > underscoreIndex && underscoreIndex != -1) {
                String base = heroSpritePath.substring(0, underscoreIndex); // before "_down"
                String ext  = heroSpritePath.substring(dotIndex);           // ".png"

                loadHeroImageOrFallback(base + "_up"    + ext, Facing.UP,    down);
                loadHeroImageOrFallback(base + "_left"  + ext, Facing.LEFT,  down);
                loadHeroImageOrFallback(base + "_right" + ext, Facing.RIGHT, down);
            } else {
                // If the naming doesn't match, just use the same sprite for all facings
                myHeroImages.put(Facing.UP,    down);
                myHeroImages.put(Facing.LEFT,  down);
                myHeroImages.put(Facing.RIGHT, down);
            }
        }

        /** Try to load a hero sprite; if it fails, fall back to the given default. */
        private void loadHeroImageOrFallback(final String path,
                                             final Facing facing,
                                             final Image fallback) {
            Image img = new ImageIcon(path).getImage();
            // If loading failed, width will be <= 0
            if (img == null || img.getWidth(null) <= 0) {
                img = fallback;
            }
            myHeroImages.put(facing, img);
        }

        /** Reads the grid layout (same format as the demo's city_map1.txt). */
        private void readGrid(final Scanner in) {
            int rows = in.nextInt();
            int cols = in.nextInt();
            in.nextLine();
            myRows = rows;
            myCols = cols;
            myGrid = new DungeonTile[rows][cols];

            for (int r = 0; r < rows; r++) {
                String line = in.nextLine();
                for (int c = 0; c < cols; c++) {
                    DungeonTile tile = convertASCII(line.charAt(c));
                    myGrid[r][c] = tile;

                    if (tile == DungeonTile.ENTRANCE) {
                        // Entrance tile: put hero there
                        myHeroY = r;
                        myHeroX = c;
                    }
                }
            }
        }

        /** Map ASCII chars (from city_map1.txt) to tile types. */
        private DungeonTile convertASCII(final char ch) {
            return switch (ch) {
                case 'M' -> DungeonTile.WALL;
                case '.' -> DungeonTile.FLOOR;
                case '-' -> DungeonTile.HORIZONTAL;
                case '|' -> DungeonTile.VERTICAL;
                case '+' -> DungeonTile.INTERSECTION;
                case 'X' -> DungeonTile.PIT;
                case 'E' -> DungeonTile.ENTRANCE;
                case 'O' -> DungeonTile.EXIT;
                case '^' -> DungeonTile.DOOR_N;
                case 'v' -> DungeonTile.DOOR_S;
                case '>' -> DungeonTile.DOOR_E;
                case '<' -> DungeonTile.DOOR_W;
                case ' ' -> DungeonTile.VOID;
                default  -> DungeonTile.FLOOR;
            };
        }

        /** Load terrain textures (same image files as the demo). */
        private void loadTileImages() {
            myTileImages.put(DungeonTile.WALL,
                    new ImageIcon("Dungeoneer_Terrain/wall.png").getImage());
            myTileImages.put(DungeonTile.FLOOR,
                    new ImageIcon("Dungeoneer_Terrain/floor.png").getImage());
            myTileImages.put(DungeonTile.PIT,
                    new ImageIcon("Dungeoneer_Terrain/floor_pit.png").getImage());
            myTileImages.put(DungeonTile.VOID,
                    new ImageIcon("Dungeoneer_Terrain/void.png").getImage());

            myTileImages.put(DungeonTile.ENTRANCE,
                    new ImageIcon("Dungeoneer_Terrain/entrance_tile.png").getImage());
            myTileImages.put(DungeonTile.EXIT,
                    new ImageIcon("Dungeoneer_Terrain/exit_tile.png").getImage());

            myTileImages.put(DungeonTile.DOOR_N,
                    new ImageIcon("Dungeoneer_Terrain/door_north.png").getImage());
            myTileImages.put(DungeonTile.DOOR_S,
                    new ImageIcon("Dungeoneer_Terrain/door_south.png").getImage());
            myTileImages.put(DungeonTile.DOOR_E,
                    new ImageIcon("Dungeoneer_Terrain/door_east.png").getImage());
            myTileImages.put(DungeonTile.DOOR_W,
                    new ImageIcon("Dungeoneer_Terrain/door_west.png").getImage());

            // For now, maze pieces use the floor texture
            Image floor = myTileImages.get(DungeonTile.FLOOR);
            myTileImages.put(DungeonTile.HORIZONTAL,  floor);
            myTileImages.put(DungeonTile.VERTICAL,    floor);
            myTileImages.put(DungeonTile.INTERSECTION, floor);
        }

        @Override
        protected void paintComponent(final Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            // Draw tiles
            for (int r = 0; r < myRows; r++) {
                for (int c = 0; c < myCols; c++) {
                    DungeonTile tile = myGrid[r][c];
                    java.awt.Image img = myTileImages.get(tile);
                    if (img == null) {
                        img = myTileImages.get(DungeonTile.FLOOR);
                    }
                    g2.drawImage(img,
                                 c * TILE_SIZE,
                                 r * TILE_SIZE,
                                 TILE_SIZE,
                                 TILE_SIZE,
                                 this);
                }
            }

         // Draw the hero sprite on top
            Image heroImg = myHeroImages.get(myFacing);
            if (heroImg == null) {
                // Fallback to DOWN if something went wrong
                heroImg = myHeroImages.get(Facing.DOWN);
            }
            if (heroImg != null) {
                g2.drawImage(heroImg,
                             myHeroX * TILE_SIZE,
                             myHeroY * TILE_SIZE,
                             TILE_SIZE,
                             TILE_SIZE,
                             this);
            }

        }
        
        public void moveHero(final int dx, final int dy) {
            int newX = myHeroX + dx;
            int newY = myHeroY + dy;

            if (newX < 0 || newX >= myCols || newY < 0 || newY >= myRows) {
                return;
            }

            DungeonTile target = myGrid[newY][newX];
            if (target == DungeonTile.WALL || target == DungeonTile.PIT) {
                return;
            }

            // Set facing based on movement
            if (dx > 0) {
                myFacing = Facing.RIGHT;
            } else if (dx < 0) {
                myFacing = Facing.LEFT;
            } else if (dy > 0) {
                myFacing = Facing.DOWN;
            } else if (dy < 0) {
                myFacing = Facing.UP;
            }

            myHeroX = newX;
            myHeroY = newY;
            repaint();
        }
    }
