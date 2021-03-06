package infproj.game.gfx;

public class Screen {
	
	public static final int MAP_WIDTH = 64;
	public static final int MAP_WIDTH_MASK = MAP_WIDTH -1;
	
	public static final byte BIT_MIRROR_X = 0x01;
	public static final byte BIT_MIRROR_Y = 0x02;
	
	public int[] pixels;
	
	public int xOffset = 0;
	public int yOffset = 0;
	
	public int width = 0;
	public int height = 0;
	
	public SpriteSheet sheet;
	
	public Screen(int width, int height, SpriteSheet sheet)
	{
		this.width =width;
		this.height=height;
		this.sheet = sheet;
		
		pixels = new int[width*height];
	}
	
	public void renderField(int targetX, int targetY, int color, int x1, int x2, int y1, int y2, int scale)
	{
		int tempY=0;
		for (int yC=0; yC+y1<y2+1; yC++)
		{	
			int tempX=0;
			for (int xC=0; xC+x1<x2; xC++)
			{
					this.render(
							targetX+(tempX*8*scale), targetY+(tempY*8*scale),
							(x1+xC) + ((y1+yC) * 32),
							color, 0, scale);
					tempX++;
			}
			tempY++;
		}
	}
	
	public void render (int xPos, int yPos, int tile, int color, int mirrorDir, int scale)
	{
		xPos -=xOffset;
		yPos -=yOffset;
		
		boolean xMirror = (mirrorDir & BIT_MIRROR_X) > 0 ;
		boolean yMirror = (mirrorDir & BIT_MIRROR_Y) > 0;
		
		int scaleMap=scale-1;
		int xTile = tile % 32;
		int yTile = tile / 32;
		
		int tileOffset = (xTile << 3) + (yTile << 3)*sheet.width;
		
		for (int y = 0; y<8;y++)
		{	
			int ySheet = y;
			if (yMirror) ySheet = 7 - y;
			
			int yPixel = y+yPos + (y*scaleMap - ((scaleMap << 3)/2));
			for (int x = 0; x < 8; x++) 
			{
				int xSheet = x;
				if (xMirror) xSheet = 7 - x;
				int xPixel = x+xPos + (x*scaleMap - ((scaleMap << 3)/2));
				int col = (color>>(sheet.pixels[xSheet+ySheet*sheet.width+tileOffset]*8))& 255;
				
				if (col < 255)
				{
					for (int yScale = 0; yScale < scale; yScale++)
					{
						if (yPixel + yScale < 0 || yPixel + yScale >= height) continue;
						for (int xScale = 0; xScale < scale; xScale++)
						{
							if (xPixel + xScale < 0 || xPixel + xScale >= width) continue;
							pixels[(xPixel+xScale)+(yPixel+yScale)*width] = col;
						}
					}
				}
			}
		}
	}

	public void setOffset(int x, int y) {
		xOffset = x;
		yOffset = y;
	}
}
