// Purpose:
// This class is used to create a map behind the game screen of the grass and path based off pixel color.
// Creates a 2D array that helps with colors over the tower placement on the game screen.
// Improvements down the road could assist with setting start and end point for enemyAi and make pathing more dynamic to level used.

package com.cs3a.tower;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;


public class TiledMap
{
    private int[][] tiles;

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

        tiles = new int[mapWidth][mapHeight];

        Color colorToMatch = hexToColor("4cf600ff");
        // f9c208ff - grass
        // 4cf600ff - path   <- this currently causes an unknown error. Did confirm pixel color.

        for(int x = 0; x <= mapWidth; x ++)
        {
            for(int y = 0; y < mapHeight; y ++)
            {
                if(x < tiles.length && y < tiles[0].length)
                {
                    int colorInt = pixmap.getPixel(x, y);
                    Color color = new Color();
                    Color.rgba8888ToColor(color, colorInt);
                    //     System.out.println(color);

                    if (color.equals(colorToMatch))
                        tiles[x][y] = 1;
                    else
                        tiles[x][y] = 0;
                }
            }
        }
    }

    // Takes the string of the hex value for the pixel color and converts to a color object
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

    public void resetPlayableArea(Tower tower) {
        int towerWidth = 30;
        int towerHeight = 30;

        int x = (int) (tower.interactionBox.x) + 2;
        int y = -((int) (tower.interactionBox.y) - tiles[0].length) - (int) tower.interactionBox.height + 2;

        for (int i = x; i < x + towerWidth * 2; i++) {
            for (int j = y; j < y + towerHeight * 2; j++) {
                if (i >= 0 && i < tiles.length && j >= 0 && j < tiles[0].length) {
                    tiles[i][j] = 1;
                }
            }
            System.out.println();
        }
    }

    // was in place purely for viewing behind the scenes and debugging
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


