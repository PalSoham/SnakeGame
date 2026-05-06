/**
 * SnakeCell.java
 *
 * A single cell in the SnakeGame.  Note that these cells do not contain
 * any information about the snake.  Instead, the ArrayList for the snake
 * will communicate with the 2D array of SnakeCell.
 *
 * @author Scott DeRuiter and Soham Pal
 * @version 1.0
 * @since 1/27/2023
 */

import java.awt.Color;

public class SnakeCell         
{	
	/**    A boolean to determine whether or not this cell contains an apple.                   */
	private boolean apple;
	
	/**
	 *  Creates a SnakeCell object, with the apple set to false.
	 */
	public SnakeCell ( )
	{
		apple = false;
	}

	/**
	 *  Draws the cell, at the appropriate ordered pair (CoordinatePair), controlled by the
	 *  2D array of SnakeCells.  If an apple exists here, it is also drawn.
	 *  @param  x      The x value (horizontal).
	 *  @param  y      The y value (vertical).
	 */
	public void drawCell(int x, int y)   
	{
		StdDraw.setPenColor(new Color(0,200,0));
		if((x + y) % 2 == 1)
		{
			StdDraw.setPenColor(new Color(0,220,0));
		}
		StdDraw.filledSquare(1 + x, 1 + y, 0.5);
		if(apple)
		{
			StdDraw.picture(1 + x, 1 + y, "apple.png", 1.5, 1.5);
		}
	}
	
	/**
	 *  A setter, setting the apple to true.
	 */
	public void setApple ( )
	{
		apple = true;
	}
	
	/**
	 *  A setter, setting the apple to false.
	 */
	public void eatApple ( )
	{
         apple = false;                                    
	}
	
	/**
	 *  A getter, getting the state of the apple (true or false).
	 *  @return          Returns true if apple is true, false otherwise.
	 */
	public boolean getApple ( )
	{
		return apple;
	}
}
