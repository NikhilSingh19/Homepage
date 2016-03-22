
/**
 * The different cards in an ordinary 52 card deck
 * 
 * @author Varun Cherukuri
 * @version January 6, 2013
 */
public class Card
{
    // instance variables - replace the example below with your own
    private int rank;
    private String suit;
    private boolean isFaceUp;

    /**
     * Constructor for objects of class Card
     */
    public Card(int r, String s)
    {
        // initialise instance variables
        rank = r;
        suit = s;
        isFaceUp = false;
    }

    /**
     * Returns the rank of the card
     */
    public int getRank()
    {
        return rank;
    }
    
    /**
     * Returns the suit of the card
     */
    public String getSuit()
    {
        return suit;
    }
    
    /**
     * Return true if the card is red; false if the card is black
     */
    public boolean isRed()
    {
        return (suit.equals("d") || suit.equals("h"));
    }
    
    /**
     * Returns true if the card is faced up; false if faced down
     */
    public boolean isFaceUp()
    {
        return isFaceUp;
    }
    
    /**
     * Turns up the card
     */
    public void turnUp()
    {
        isFaceUp = true;
    }
    
    /**
     * Turns down the card
     */
    public void turnDown()
    {
        isFaceUp = false;
    }
    
    /**
     * Returns the file name for the cards image
     */
    public String getFileName()
    {
        String c = "";
        if(!isFaceUp)
        {
            c = "cards/back.gif";
        }
        else if(rank > 1 && rank < 10)
        {
            c = "cards/" + rank + suit + ".gif";
        }
        else
        {
            String other = "";
            if(rank == 1)
            {
                other = "a";
            }
            if(rank == 10)
            {
                other = "t";
            }
            if(rank == 11)
            {
                other = "j";
            }
            if(rank == 12)
            {
                other = "q";
            }
            if(rank == 13)
            {
                other = "k";
            }
            c = "cards/" + other + suit + ".gif";
        }
        return c; 
    }
}
