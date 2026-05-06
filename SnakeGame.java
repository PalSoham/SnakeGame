/**
 * SnakeGame.java
 *
 * The snake game consists of a 2D array, representing the game board,
 * and an ArrayList of ordered pairs (CoordinatePair), representing the
 * snake.  The game board contains apples (one at a time), and when the
 * head of the snake moves over an apple, the apple is eliminated, and
 * another apple is placed at random (not on the snake).  The score is
 * also increased by 1 for each apple that is "eaten".  A score and high
 * score are recorded and updated.  A variety of key strokes controls
 * the play of the game.
 *
 * @author Soham Pal
 * @version 1.0
 * @since 1/27/2023
 */

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.awt.event.KeyEvent;

public class SnakeGame
{

    /**    The object representing the game board.  This board will be 19 x 17,
     *     (19 across and 17 tall). Think of these cells as places on a coordinate plane.
     *     One cell will be chosen at random to place an apple.                               */
    private SnakeCell [][] board;

    /**    An ArrayList to hold the position(s) of the snake on the board.                    */
    private ArrayList<CoordinatePair> snake;

    /**    The direction of the snake head (UP, DOWN, LEFT, or RIGHT).                        */
    private Direction snakeDirection;

    /**    A score and high score for the game.  Each apple eaten will add one to the score.  */
    private int score, highScore;

    /**    The pause time, in milliseconds, between frames being shown.  The smaller the
     *     pause time, the faster the game.                                                   */
    private int pauseTime;

    /**    Booleans indicating when the snake is moving, when the game is over (and needs
     *     a reset), and if the small snake should be used to start the game.                 */
    private boolean snakeIsMoving, needsReset, smallSnake;

    /**
     *  Creates a SnakeGame object, with a sized graphics canvas, and a 2D (19 x 17) array
     *  of SnakeCell, setting up initial values.
     *  @param  sm           A boolean to indicate if game should be played with the small snake.
     *  @param  pauseIt      An int to indicate the pause time (in milliseconds).
     */
    public SnakeGame(boolean sm, int pauseIt)
    {
        StdDraw.setCanvasSize(640,640);
        StdDraw.setXscale(0,20);
        StdDraw.setYscale(0,20);
        StdDraw.enableDoubleBuffering();

        highScore = 0;
        smallSnake = sm;
        pauseTime = pauseIt;
        boardSetup();
    }

    /**
     *  Called by the constructor, and when the game needs to be restarted.  Initializes
     *  the game board, with one apple placed at random on the board (not on the snake).
     *  The score, direction of the snake, and a couple of the boolean values are reset.
     *  The small or large snake is initialized.  The game board is drawn and shown.
     */
    public void boardSetup ( )
    {
        board = new SnakeCell[19][17];
        for(int row = 0; row < board.length; row++)
        {
            for(int col = 0; col < board[row].length; col++)
            {
                board[row][col] = new SnakeCell();
            }
        }

        snakeDirection = Direction.RIGHT;
        score = 0;
        snakeIsMoving = false;
        needsReset = false;

        snake = new ArrayList<CoordinatePair>();
        if(smallSnake)
        {
            CoordinatePair head = new CoordinatePair(6,8);
            CoordinatePair body1 = new CoordinatePair(5,8);
            CoordinatePair body2 = new CoordinatePair(4,8);
            CoordinatePair tail = new CoordinatePair(3,8);

            snake.add(head);
            snake.add(body1);
            snake.add(body2);
            snake.add(tail);
        }

        else           //  BIG snake!
        {
            snake.add(new CoordinatePair(1, 0));
            snake.add(new CoordinatePair(0, 0));
            for(int i = 0; i < 18; i++)
            {
                for(int j = 1; j < 17; j++)
                {
                    if(i % 2 == 1)
                    {
                        snake.add(new CoordinatePair(i, 17 - j));
                    }
                    else
                    {
                        snake.add(new CoordinatePair(i, j));
                    }
                }
            }
        }
        placeRandomAppleNotOnSnake();
        drawAll();
        StdDraw.show();
    }

    /**
     *  Sets up and runs the game of SnakeGame, making use of args[0] and
     *  args[1], if the user decides to include them.
     */
    public static void main(String [] args)
    {
        boolean small = true;
        int pauseIt = 180;
        if(args.length > 0)
        {
            small = Boolean.parseBoolean(args[0]);
        }
        if(args.length > 1)
        {
            pauseIt = Integer.parseInt(args[1]);
        }
        SnakeGame game = new SnakeGame(small, pauseIt);
        game.run();
    }

    /**
     *  Runs an endless loop to play the game.  The game may be paused or
     *  in need of being reset, but the loop is always checking for key
     *  input.  If the game is running (snake is moving), the next cell
     *  is checked to see if the snake can move.  If the snake can move,
     *  it is moved, otherwise the game stops (and needs to be reset).
     */
    public void run ( )
    {
        do
        {
            respondToKeyBoard();
            if(snakeIsMoving && !needsReset)
            {
                if(canMoveSnake())
                {
                    moveSnake();
                }
                else
                {
                    stopGame();
                }
                drawAll();
                StdDraw.show();
            }
            StdDraw.pause(pauseTime);
        }
        while(true);
    }

    /**
     *  Draws the game, in its current state, to the GUI.  Also draws the scores
     *  and game messages.
     */
    public void drawAll ( )
    {
        StdDraw.clear(Color.GRAY);
        drawTopPanelMessages();
        drawBoard();
        drawSnake();
    }

    /**
     *  Draws the messages at the top of the GUI.
     */
    public void drawTopPanelMessages ( )
    {
        StdDraw.setPenColor(new Color(0,0,0));
        StdDraw.filledRectangle(3.05,18.2,2.6,0.55);
        StdDraw.filledRectangle(9.8,18.2,3.5,0.55);
        StdDraw.filledRectangle(16.8,18.2,2.8,0.55);
        StdDraw.setPenColor(new Color(255,255,255));
        StdDraw.filledRectangle(3.05,18.2,2.5,0.45);
        StdDraw.filledRectangle(9.8,18.2,3.4,0.45);
        StdDraw.filledRectangle(16.8,18.2,2.7,0.45);
        StdDraw.setPenColor(new Color(0,0,0));
        StdDraw.setFont(new Font("SansSerif", Font.BOLD, 22));
        StdDraw.text(2.3,18.1,"SCORE:");
        StdDraw.text(4.6,18.1,"" + score);
        StdDraw.text(9,18.1,"HIGH SCORE:");
        if(highScore > 0)
        {
            StdDraw.text(12.2,18.1,"" + highScore);
        }
        StdDraw.text(15.9,18.1,"PAUSE:");
        StdDraw.text(18.25,18.1,"" + pauseTime);
        if(needsReset)
        {
            StdDraw.text(6.3,19.2,"GAME OVER, PRESS R TO RESET");
        }
        else if(!snakeIsMoving)
        {
            StdDraw.text(5.6,19.2,"PRESS I, J, K, OR L TO MOVE");
        }
    }

    /**
     *  Draws the board (with the apple, but without the snake).
     */
    public void drawBoard ( )
    {
        for(int row = 0; row < board.length; row++)
        {
            for(int col = 0; col < board[row].length; col++)
            {
                board[row][col].drawCell(row, col);
            }
        }
    }

    /**
     * Draws the snake.  The head uses its image, scaled to the cell size.
     * The tail is drawn as a triangle pointing away from the body.
     * All body segments in between are drawn as solid blue squares.
     */
    public void drawSnake ( )
    {
        for(int i = 0; i < snake.size(); i++)
        {
            double x = 1 + snake.get(i).getX();
            double y = 1 + snake.get(i).getY();

            if(i == 0) // Draw Head
            {
                double angle = 0;
                if(snakeDirection == Direction.UP) angle = 90;
                else if(snakeDirection == Direction.DOWN) angle = 270;
                else if(snakeDirection == Direction.LEFT) angle = 180;

                // Adjusted to draw 1x1 width and length (0.5 half-width = 1.0 full width)
                StdDraw.picture(x, y, "snakehead.png", 1.0, 1.0, angle);
            }
            else if (i == snake.size() - 1) // Draw Tail as Triangle
            {
                StdDraw.setPenColor(new Color(80, 130, 230));

                // Get the position of the segment before the tail to determine orientation
                CoordinatePair beforeTail = snake.get(i - 1);
                int dx = snake.get(i).getX() - beforeTail.getX();
                int dy = snake.get(i).getY() - beforeTail.getY();

                double[] xCoords = new double[3];
                double[] yCoords = new double[3];

                if (dx > 0) // Tail is to the right of the body (pointing right)
                {
                    xCoords = new double[]{x - 0.5, x - 0.5, x + 0.5};
                    yCoords = new double[]{y - 0.5, y + 0.5, y};
                }
                else if (dx < 0) // Tail is to the left of the body (pointing left)
                {
                    xCoords = new double[]{x + 0.5, x + 0.5, x - 0.5};
                    yCoords = new double[]{y - 0.5, y + 0.5, y};
                }
                else if (dy > 0) // Tail is above the body (pointing up)
                {
                    xCoords = new double[]{x - 0.5, x + 0.5, x};
                    yCoords = new double[]{y - 0.5, y - 0.5, y + 0.5};
                }
                else // Tail is below the body (pointing down)
                {
                    xCoords = new double[]{x - 0.5, x + 0.5, x};
                    yCoords = new double[]{y + 0.5, y + 0.5, y - 0.5};
                }
                StdDraw.filledPolygon(xCoords, yCoords);
            }
            else // Draw Body
            {
                StdDraw.setPenColor(new Color(80, 130, 230));
                StdDraw.filledSquare(x, y, 0.5);
            }
        }
    }

    /**
     *  Places an apple at a random ordered pair (CoordinatePair) on the game board, making
     *  sure that the apple is not placed on the snake.
     */
    public void placeRandomAppleNotOnSnake ( )
    {
        int x = (int)(Math.random() * 19);
        int y = (int)(Math.random() * 17);
        while(pointOnSnake(x, y))
        {
            x = (int)(Math.random() * 19);
            y = (int)(Math.random() * 17);
        }
        board[x][y].setApple();
    }

    /**
     *  Checks to see if there is a conflict with the apple and the snake.  Checks to see if the
     *  ordered pair is a part of the snake.  A helper method for placeRandomAppleNotOnSnake.
     *  @param  x          The x value (horizontal position) of the ordered pair.
     *  @param  y          The y value (vertical position) of the ordered pair.
     *  @return            Returns true if the position is on the snake, false otherwise.
     */
    public boolean pointOnSnake(int x, int y)
    {
        for(int i = 0; i < snake.size(); i++)
        {
            if(snake.get(i).getX() == x && snake.get(i).getY() == y)
                return true;
        }
        return false;
    }

    /**
     *  Checks to see if the snake can be moved, given its current direction.  Returns true if it
     *  can move to the new position, false otherwise.  There are two reasons the snake would not
     *  be able to move to the new position; either the position is off the board, or the position
     *  is a point on the snake itself.
     *  @return            Returns true if the snake can move to the new position, false otherwise.
     */
    public boolean canMoveSnake ( )
    {
        CoordinatePair head = snake.get(0);
        if(snakeWouldHitSelf())
            return false;
        if(snakeDirection == Direction.RIGHT)
        {
            if(head.getX() + 1 >= board.length)
                return false;
        }
        else if(snakeDirection == Direction.UP)
        {
            if(head.getY() + 1 >= board[0].length)
                return false;
        }
        else if(snakeDirection == Direction.DOWN)
        {
            if(head.getY() - 1 < 0)
                return false;
        }
        else if(snakeDirection == Direction.LEFT)
        {
            if(head.getX() - 1 < 0)
                return false;
        }
        return true;
    }

    /**
     *  Checks to see if the snake would move onto itself.  This is a helper method for canMoveSnake.
     *  @return            Returns true if the snake would move onto itself, false otherwise.
     */
    public boolean snakeWouldHitSelf ( )
    {
        CoordinatePair head = new CoordinatePair(0,0);
        if(snakeDirection == Direction.UP)
            head = new CoordinatePair(snake.get(0).getX(), snake.get(0).getY() + 1);
        else if(snakeDirection == Direction.DOWN)
            head = new CoordinatePair(snake.get(0).getX(), snake.get(0).getY() - 1);
        else if(snakeDirection == Direction.LEFT)
            head = new CoordinatePair(snake.get(0).getX() - 1, snake.get(0).getY());
        else if(snakeDirection == Direction.RIGHT)
            head = new CoordinatePair(snake.get(0).getX() + 1, snake.get(0).getY());

        for(int i = 1; i < snake.size(); i++)
        {
            if(head.equals(snake.get(i)))
                return true;
        }
        return false;
    }

    /**
     *  Moves the snake, readjusting the ArrayList that represents the snake.  Note that many of
     *  the snake parts do not need to be modified (just the head and the tail).  If an apple
     *  is eaten, the snake grows by one (Hint: don't remove the last CoordinatePair).
     */
    public void moveSnake ( )
    {
        CoordinatePair head = snake.get(0);
        if(snakeDirection == Direction.RIGHT)
        {
            CoordinatePair tail = new CoordinatePair(head.getX() + 1, head.getY());
            snake.add(0, tail);
        }
        else if(snakeDirection == Direction.LEFT)
        {
            CoordinatePair tail = new CoordinatePair(head.getX() - 1, head.getY());
            snake.add(0, tail);
        }
        else if(snakeDirection == Direction.UP)
        {
            CoordinatePair tail = new CoordinatePair(head.getX(), head.getY() + 1);
            snake.add(0, tail);
        }
        else if(snakeDirection == Direction.DOWN)
        {
            CoordinatePair tail = new CoordinatePair(head.getX(), head.getY() - 1);
            snake.add(0, tail);
        }
        if(board[head.getX()][head.getY()].getApple())
        {
            board[head.getX()][head.getY()].eatApple();
            placeRandomAppleNotOnSnake();
            score++;
        }
        else
            snake.remove(snake.size() - 1);
    }

    /**
     *  Stops the game, because the snake has attempted an illegal move.  The game is over, and must
     *  be reset in order to play again.
     */
    public void stopGame ( )
    {
        snakeIsMoving = false;
        needsReset = true;
        if(score > highScore)
        {
            highScore = score;
        }
    }

    /**
     *  Responds to the user's keystrokes.  Note that the keystrokes are saved into a buffer, and dealt
     *  with one at a time.
     */
    public void respondToKeyBoard ( )
    {
        char typedChar = 'x';
        if(StdDraw.hasNextKeyTyped())
        {
            typedChar = StdDraw.nextKeyTyped();
        }
        if(needsReset)
        {
            if(typedChar == 'r' || typedChar == 'R')
            {
                snakeIsMoving = false;
                needsReset = false;
                boardSetup();
            }
            return;
        }
        else if(typedChar == 'l' && snakeDirection != Direction.LEFT)
        {
            snakeDirection = Direction.RIGHT;
            snakeIsMoving = true;
        }
        else if(typedChar == 'k' && snakeDirection != Direction.UP)
        {
            snakeDirection = Direction.DOWN;
            snakeIsMoving = true;
        }
        else if(typedChar == 'j' && snakeDirection != Direction.RIGHT)
        {
            snakeDirection = Direction.LEFT;
            snakeIsMoving = true;
        }
        else if(typedChar == 'i' && snakeDirection != Direction.DOWN)
        {
            snakeDirection = Direction.UP;
            snakeIsMoving = true;
        }
        else if(typedChar == 'p')
        {
            snakeIsMoving = false;
            drawAll();
            StdDraw.show();
        }
    }
}