/**
 * CoordinatePair.java
 *
 * A coordinate pair, with x (horizontal) and y (vertical).  Also, the enumerated
 * datatype Direction is included in this file.
 *
 * @author Scott DeRuiter and Soham Pal
 * @version 1.0
 * @since 1/27/2023
 */

public class CoordinatePair
{
	/**    An ordered pair, with the x is for the horizontal, the y for the vertical         */
	private int x, y;
	
	/**
	 *  Creates a CoordinatePair object, setting x and y.
	 *  @param  x          The x value (horizontal position) of the ordered pair.
	 *  @param  y          The y value (vertical position) of the ordered pair.
	 */
	public CoordinatePair(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 *  A getter, returning the horizontal value, x.
	 *  @return          Returns the horizontal value for the ordered pair, x.
	 */
	public int getX ( )
	{
		return x;
	}
	
	/**
	 *  A getter, returning the vertical value, y.
	 *  @return          Returns the vertical value for the ordered pair, y.
	 */
	public int getY ( )
	{
		return y;
	}
	
	/**
	 *  Compares this ordered pair with other, returning true if they share the same
	 *  values, false otherwise.
	 *  @param  other    The other ordered pair (CoordinatePair) to be compared.
	 *  @return          Returns true if the two pairs contain matching values,
	 *                   false otherwise.
	 */
	public boolean equals (CoordinatePair other)
	{
		if(x == other.x && y == other.y)
			return true;
		return false;                                            
	}
}

/**
 *  An enumerated (custom) data type, to be used for the direction of the snake
 *  (shown with the head of the snake).  These values can be described as:
 *  Direction.UP, Direction.DOWN, Direction.LEFT, and Direction.RIGHT.
 */
enum Direction
{
	UP, DOWN, LEFT, RIGHT
}
