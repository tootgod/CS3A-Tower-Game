package com.cs3a.tower;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;


public class TiledMap
{
    private int[][] tiles;
    private int tileWidth = 1;
    private int tileHeight = 1;
    private int mapWidth;
    private int mapHeight;

    public TiledMap ()
    {
          readBackground("LevelBackground.png");
    }

    public void readBackground(String imagePath)
    {
        Pixmap pixmap = new Pixmap(Gdx.files.internal(imagePath));
        mapWidth = pixmap.getWidth() - 256;
        mapHeight = pixmap.getHeight();

        tiles = new int[mapWidth/tileWidth][mapHeight/tileHeight];

        Color colorToMatch = hexToColor("4cf600ff");
        // f9c208ff - grass
        // 4cf600ff - path

        for(int x = 0; x <= mapWidth; x += tileWidth)
        {
            for(int y = 0; y < mapHeight; y += tileHeight)
            {
                if(x /tileWidth < tiles.length && y / tileHeight < tiles[0].length)
                {
                    int colorInt = pixmap.getPixel(x, y);
                    Color color = new Color();
                    Color.rgba8888ToColor(color, colorInt);
               //     System.out.println(color);

                    if (color.equals(colorToMatch))
                    {
                            tiles[x / tileWidth][y / tileHeight] = 1;
                    }
                    else
                    {
                        if(x == 0)
                        {
                            tiles[x / tileWidth][y / tileHeight] = 2;
                        }
                        else if (x == mapWidth)
                        {
                            tiles[x / tileWidth][y / tileHeight] = 3;
                        }
                        else
                        {
                            tiles[x / tileWidth][y / tileHeight] = 0;
                        }
                    }
                }
            }
        }
    }

    private Color hexToColor(String hex)
    {
        Color color = new Color();
        Color.rgba8888ToColor(color, Integer.parseInt(hex, 16));
    //    System.out.println("matched to: " + color);
        return color;
    }

    public int[][] getMap()
    {
        return tiles;
    }

    public void showMap()
    {
        for (int[] tile : tiles) {
            for (int y = 0; y < tiles[0].length; y++) {
                System.out.print(tile[y] + " ");
            }
            System.out.println();
        }
    }

}







