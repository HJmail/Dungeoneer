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
import model.DungeonTile;

/**
     * Graphical dungeon board in the center, using the same tiles as the demo.
     * It reads city_map1.txt and draws the map with a hero sprite at the entrance.
     */
  public final class DungeonBoardPanel extends JPanel {
    	
        private static final long serialVersionUID = 1L;

        private static final String MAP_FILE = "city_map1.txt";
        private static final int TILE_SIZE = 60;
        
     // Which way the hero is facing
        private enum Facing { UP, DOWN, LEFT, RIGHT }

        private Facing myFacing = Facing.DOWN;

        // Hero sprite images for each facing
        private final Map<Facing, Image> myHeroImages =
                new EnumMap<>(Facing.class);

        private Image myDeadHeroImage;
        private boolean myHeroDead = false;

        private DungeonTile[][] myGrid;
        private int myRows;
        private int myCols;

        // Where to draw the hero
        private int myHeroX;
        private int myHeroY;
        
        private int myEntranceX;
        private int myEntranceY;

        // Tile images
        private final Map<DungeonTile, Image> myTileImages = new HashMap<>();

        public DungeonBoardPanel(final String heroSpritePath,
        		                 final String deadSpritePath) {

        	loadHeroImages(heroSpritePath);
        	myDeadHeroImage = new ImageIcon(deadSpritePath).getImage();
        	
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
            //setPreferredSize(new Dimension(myCols * TILE_SIZE, myRows * TILE_SIZE));
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
                        
                     // Remember entrance for respawn
                        myEntranceY = r;
                        myEntranceX = c;
                    }
                }
            }
        }

        /** Map ASCII chars (from city_map1.txt) to tile types. */
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

                // items
                case '$' -> DungeonTile.GOLD;
                case 'H' -> DungeonTile.HEALING_POTION;
                case 'V' -> DungeonTile.VISION_POTION;

                // weapons
                case '1' -> DungeonTile.SPEAR;
                case '2' -> DungeonTile.FALCHION;
                case '3' -> DungeonTile.FLAIL;
                case '4' -> DungeonTile.MORNING_STAR;
                case '5' -> DungeonTile.STICK;

                // pillars  (match your city_map1.txt: A, N, I, P)
                case 'A' -> DungeonTile.ABSTRACTION_PILLAR;
                case 'N' -> DungeonTile.ENCAPSULATION_PILLAR;  // N for eNcapsulation
                case 'I' -> DungeonTile.INHERITANCE_PILLAR;
                case 'P' -> DungeonTile.POLYMORPHISM_PILLAR;

                default -> DungeonTile.FLOOR;
            };
        }

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

            // existing items
            myTileImages.put(DungeonTile.GOLD,
                    new ImageIcon("Dungeoneer_Items/gold.png").getImage());
            myTileImages.put(DungeonTile.HEALING_POTION,
                    new ImageIcon("Dungeoneer_Items/potion_healing.png").getImage());
            myTileImages.put(DungeonTile.VISION_POTION,
                    new ImageIcon("Dungeoneer_Items/potion_vision.png").getImage());

            // NEW: each weapon gets its own sprite
            myTileImages.put(DungeonTile.SPEAR,
                    new ImageIcon("Dungeoneer_Items/spear.png").getImage());
            myTileImages.put(DungeonTile.FALCHION,
                    new ImageIcon("Dungeoneer_Items/falchion.png").getImage());
            myTileImages.put(DungeonTile.FLAIL,
                    new ImageIcon("Dungeoneer_Items/flail.png").getImage());
            myTileImages.put(DungeonTile.MORNING_STAR,
                    new ImageIcon("Dungeoneer_Items/morning_star.png").getImage());
            myTileImages.put(DungeonTile.STICK,
                    new ImageIcon("Dungeoneer_Items/stick.png").getImage());

            myTileImages.put(DungeonTile.ABSTRACTION_PILLAR,
                    new ImageIcon("Dungeoneer_Items/abstraction_pillar.png").getImage());
            myTileImages.put(DungeonTile.ENCAPSULATION_PILLAR,
                    new ImageIcon("Dungeoneer_Items/encapsulation_pillar.png").getImage());
            myTileImages.put(DungeonTile.INHERITANCE_PILLAR,
                    new ImageIcon("Dungeoneer_Items/inheritance_Pillar.png").getImage());
            myTileImages.put(DungeonTile.POLYMORPHISM_PILLAR,
                    new ImageIcon("Dungeoneer_Items/polymorphism_pillar.png").getImage());

            // Maze pieces still using floor texture
            Image floor = myTileImages.get(DungeonTile.FLOOR);
            myTileImages.put(DungeonTile.HORIZONTAL, floor);
            myTileImages.put(DungeonTile.VERTICAL, floor);
            myTileImages.put(DungeonTile.INTERSECTION, floor);
        }

        @Override
        protected void paintComponent(final Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            // --- 1) Tiles + item overlays ---
            for (int r = 0; r < myRows; r++) {
                for (int c = 0; c < myCols; c++) {
                    DungeonTile tile = myGrid[r][c];

                    // BASE TILE
                    Image baseImg;
                    if (isItemTile(tile)
                            || tile == DungeonTile.HORIZONTAL
                            || tile == DungeonTile.VERTICAL
                            || tile == DungeonTile.INTERSECTION) {
                        baseImg = myTileImages.get(DungeonTile.FLOOR);
                    } else {
                        baseImg = myTileImages.get(tile);
                        if (baseImg == null) {
                            baseImg = myTileImages.get(DungeonTile.FLOOR);
                        }
                    }

                    g2.drawImage(baseImg,
                                 c * TILE_SIZE,
                                 r * TILE_SIZE,
                                 TILE_SIZE,
                                 TILE_SIZE,
                                 this);

                    // ITEM OVERLAY
                    if (isItemTile(tile)) {
                        Image itemImg = myTileImages.get(tile);
                        if (itemImg != null) {
                            g2.drawImage(itemImg,
                                         c * TILE_SIZE,
                                         r * TILE_SIZE,
                                         TILE_SIZE,
                                         TILE_SIZE,
                                         this);
                            // If you want a smaller inset, use:
                            // int off = 6, size = TILE_SIZE - 2 * off;
                            // g2.drawImage(itemImg,
                            //              c * TILE_SIZE + off,
                            //              r * TILE_SIZE + off,
                            //              size, size, this);
                        }
                    }
                }
            }

            // --- 2) Hero sprite on top ---
            Image heroImg;
            if (myHeroDead && myDeadHeroImage != null && myDeadHeroImage.getWidth(this) > 0) {
                heroImg = myDeadHeroImage;
            } else {
                heroImg = myHeroImages.get(myFacing);
                if (heroImg == null) {
                    heroImg = myHeroImages.get(Facing.DOWN);
                }
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
            if (target == DungeonTile.WALL) {
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
        
     // In DungeonBoardPanel
        @Override
        public Dimension getPreferredSize() {
            if (myGrid == null) {
                // default if nothing loaded yet
                return new Dimension(10 * TILE_SIZE, 10 * TILE_SIZE);
            }
            return new Dimension(myCols * TILE_SIZE, myRows * TILE_SIZE);
        }
        
        public boolean isHeroOnPit() {
            return myGrid[myHeroY][myHeroX] == DungeonTile.PIT;
        }

        public int getHeroX() {
            return myHeroX;
        }

        public int getHeroY() {
            return myHeroY;
        }

        public void resetHeroToEntrance() {
            myHeroX = myEntranceX;
            myHeroY = myEntranceY;
            myFacing = Facing.DOWN; // or whatever default you like
            repaint();
        }

        public void setHeroDead(final boolean theDead) {
            myHeroDead = theDead;
            repaint();
        }

        public DungeonTile getTileUnderHero() {
            return myGrid[myHeroY][myHeroX];
        }

        public void clearTileUnderHero() {
            myGrid[myHeroY][myHeroX] = DungeonTile.FLOOR;
            repaint();
        }

        public boolean isHeroOnWeapon() {
            DungeonTile t = myGrid[myHeroY][myHeroX];
            return t == DungeonTile.SPEAR
                || t == DungeonTile.FALCHION
                || t == DungeonTile.FLAIL
                || t == DungeonTile.MORNING_STAR
                || t == DungeonTile.STICK;
        }

        // === NEW: helper methods your DungeoneerFrame is calling ===

        public boolean isHeroOnGold() {
            return myGrid[myHeroY][myHeroX] == DungeonTile.GOLD;
        }

        public boolean isHeroOnHealingPotion() {
            return myGrid[myHeroY][myHeroX] == DungeonTile.HEALING_POTION;
        }

        public boolean isHeroOnVisionPotion() {
            return myGrid[myHeroY][myHeroX] == DungeonTile.VISION_POTION;
        }
        
        /** Tiles that are drawn as items on top of a floor tile. */
        private boolean isItemTile(final DungeonTile tile) {
            return tile == DungeonTile.GOLD
                || tile == DungeonTile.HEALING_POTION
                || tile == DungeonTile.VISION_POTION
                || tile == DungeonTile.SPEAR
                || tile == DungeonTile.FALCHION
                || tile == DungeonTile.FLAIL
                || tile == DungeonTile.MORNING_STAR
                || tile == DungeonTile.STICK
                || tile == DungeonTile.ABSTRACTION_PILLAR
                || tile == DungeonTile.ENCAPSULATION_PILLAR
                || tile == DungeonTile.INHERITANCE_PILLAR
                || tile == DungeonTile.POLYMORPHISM_PILLAR;
        }
  }
        
 