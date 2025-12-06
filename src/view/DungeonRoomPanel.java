package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import model.GameConfig;
import model.Hero;
import model.Room;
import model.Tile;
import model.TileType;

public class DungeonRoomPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int TILE_SIZE = 60;
	
	private GameConfig myGameConfig;
	
	private Room myRoom;
	
    private final Map<Facing, Image> myHeroImages =
            new EnumMap<>(Facing.class);
    
    private final Map<TileType, Image> myTileImages =
            new EnumMap<>(TileType.class);
	
	private enum Facing {UP, DOWN, LEFT, RIGHT}
	
	private Facing myFacing;
	
	public DungeonRoomPanel(final Room theRoom, final GameConfig theGC)
	{
		myRoom = theRoom;
		myFacing = Facing.DOWN;
		myGameConfig = theGC;
		
		loadTileImages();
		loadHeroImages();
		
		setPreferredSize(new Dimension(myRoom.getTilesCols() * TILE_SIZE,
							myRoom.getTilesRows() * TILE_SIZE));
		
		setFocusable(true);
	}
	
	public void setRoom(final Room theRoom)
	{
		myRoom = theRoom;
		repaint();
	}
	
	@Override
	protected void paintComponent(final Graphics theGraphics)
	{
		super.paintComponent(theGraphics);
		
		drawRoom(theGraphics);
		drawHero(theGraphics);
	}
	
	private void loadTileImages()
	{
		for(TileType type: TileType.values())
		{
			String path = "/" + type.getFilePath();
            URL url = getClass().getResource(path);
            
            if (url == null) {
                System.err.println("Could not find image for " + type + " at " + path);
                continue; // skip this type instead of crashing
            }
            
            Image img = new ImageIcon(url).getImage();
            myTileImages.put(type, img);
		}
	}
	
	private void loadHeroImages() 
	{
		String heroSpritePath = myGameConfig.getHero().getImagePath() + ".png";
		
        Image down = new ImageIcon(heroSpritePath).getImage();
        myHeroImages.put(Facing.DOWN, down);

        // Try to derive base + extension, e.g. "Dungeoneer_Characters/warrior" + "_down" + ".png"
        int dotIndex = heroSpritePath.lastIndexOf('.');
        int underscoreIndex = heroSpritePath.lastIndexOf('_');

        if (dotIndex > underscoreIndex && underscoreIndex != -1) 
        {
            String base = heroSpritePath.substring(0, underscoreIndex); // before "_down"
            String ext  = heroSpritePath.substring(dotIndex);           // ".png"

            loadHeroImageOrFallback(base + "_up"    + ext, Facing.UP,    down);
            loadHeroImageOrFallback(base + "_left"  + ext, Facing.LEFT,  down);
            loadHeroImageOrFallback(base + "_right" + ext, Facing.RIGHT, down);
        }
        else 
        {
            // If the naming doesn't match, just use the same sprite for all facings
            myHeroImages.put(Facing.UP,    down);
            myHeroImages.put(Facing.LEFT,  down);
            myHeroImages.put(Facing.RIGHT, down);
        }
    }
	
    private void loadHeroImageOrFallback(final String path,
    									final Facing facing,
    									final Image fallback) 
    {
		Image img = new ImageIcon(path).getImage();
		// If loading failed, width will be <= 0
		if (img == null || img.getWidth(null) <= 0) {
		img = fallback;
		}
		myHeroImages.put(facing, img);
    }	
	
	private void drawRoom(final Graphics theGraphics)
	{
		int rows = myRoom.getTilesRows();
		int cols = myRoom.getTilesCols();
		
		for(int row = 0; row < rows; row++)
		{
			for(int col = 0; col < cols; col++)
			{
				Tile tile = myRoom.getTile(new Point(row, col));
				TileType type = tile.getTileType();
				
	            TileType base = type.getBaseType();
	            Image baseImg = myTileImages.get(base);
	            
	            if (baseImg != null) 
	            {
	                theGraphics.drawImage(baseImg,
	                        col * TILE_SIZE,
	                        row * TILE_SIZE,
	                        TILE_SIZE,
	                        TILE_SIZE,
	                        null);
	            }
	            
	            if (base != type) 
	            {
	                Image overlayImg = myTileImages.get(type);
	                if (overlayImg != null) 
	                {
	                    theGraphics.drawImage(overlayImg,
	                            col * TILE_SIZE,
	                            row * TILE_SIZE,
	                            TILE_SIZE,
	                            TILE_SIZE,
	                            null);
	                }
	            }
			}
		}
	}
	
	private void drawHero(final Graphics theGraphics)
	{
		Point heroPos = myRoom.getMyHeroRoomLocation();
		

        int col = heroPos.x;
        int row = heroPos.y;

        Image heroImg = myHeroImages.get(myFacing);
        
        if (heroImg != null) 
        {
            theGraphics.drawImage(heroImg,
                        col * TILE_SIZE,
                        row * TILE_SIZE,
                        TILE_SIZE,
                        TILE_SIZE,
                        null);
        } 
        else 
        {
            theGraphics.setColor(Color.BLUE);
            theGraphics.drawString("@",
                         col * TILE_SIZE + TILE_SIZE / 3,
                         row * TILE_SIZE + TILE_SIZE * 2 / 3);

        }
	}
}
