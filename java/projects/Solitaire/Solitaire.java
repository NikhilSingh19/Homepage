import java.util.*;

/**
 * Class Solitaire creates a game of solitaire
 * 
 * @author Nikhil Singh
 * @version 1-16-13
 * plays the game of solitaire using card
 */
public class Solitaire
{
    public static void main(String[] args)
    {
        new Solitaire();
    }

    private Stack<Card> stock;
    private Stack<Card> waste;
    private Stack<Card>[] foundations;
    private Stack<Card>[] piles;
    private SolitaireDisplay display;

    public Solitaire()
    {
        foundations = new Stack[4];
        for(int i = 0; i <= 3; i++)
        {
            foundations[i] = new Stack<Card>();
        }
        piles = new Stack[7];
        for(int i = 0; i <= 6; i++)
        {
            piles[i] = new Stack<Card>();
        }
        
        stock = new Stack<Card>();
        waste = new Stack<Card>();
        createStock();
        deal();
        
        display = new SolitaireDisplay(this);
    }
    /**
     * Returns the top card in the stock
     * 
     * @return the top card, or null if it is empty
     */
    public Card getStockCard()
    {
        if(stock.isEmpty())
        {
            return null;
        }
        return stock.peek();
    }
    /**
     * Returns the top card in the waste
     * 
     * @return the top card, or null if it is empty
     */
    public Card getWasteCard()
    {
        if(waste.isEmpty())
        {
            return null;
        }
        return waste.peek();
    }    
    /**
     * Returns the top card in a given foundation
     * 
     * Precondition: 0 <= index < 4
     * 
     * @param index the index to check
     * 
     * @return the top card, or null if it is empty
     */
    public Card getFoundationCard(int index)
    {
        if(foundations[index].isEmpty())
        {
            return null;
        }
        return foundations[index].peek();
    }    
    /**
     * Returns a  pile based on the given index
     * 
     * Precondition:  0 <= index < 7
     * 
     * @param index the index to check
     * 
     * @return the pile based on the index
     */
    public Stack<Card> getPile(int index)
    {
        return piles[index];
    } 
    /**
     * Creates and inserts 52 cards into a given stack. The cards are inserted in a RANDOM order
     * 
     * 
     */
    private void createStock()
    {
        ArrayList<Card> car = new ArrayList<Card>();
        for(int i = 1; i <= 13; i++)
        {
            car.add(new Card(i, "d"));
        }
        for(int i = 1; i <= 13; i++)
        {
            car.add(new Card(i, "h"));
        }
        for(int i = 1; i <= 13; i++)
        {
            car.add(new Card(i, "s"));
        }
        for(int i = 1; i <= 13; i++)
        {
            car.add(new Card(i, "c"));
        }
        for(int count = 1; count <= 52; count++)
        {
            int shufflec = (int)(Math.random() * car.size());
            stock.push(car.get(shufflec));
            car.remove(shufflec);
        }
    }
    /**
     * Deals cards from the stack to the 7 piles.
     */
    private void deal() 
    {
        for(int i = 0; i <= 6; i++)
        {
            int count = i + 1;
            while(count > 0) 
            {
                Card car = stock.pop();
                car.turnUp();
                piles[i].push(car);
                Card temp = piles[i].peek();
                temp.turnDown();
                count--;
            }
            piles[i].peek().turnUp();
        }
    }    
    /**
     * Moves three cards from the stock to the waste pile
     */
    private void dealThreeCards()
    {
        for(int i = 0; i <= 2; i++)
        {
            if(!stock.isEmpty())
            {
                Card c = stock.pop();
                c.turnUp();
                waste.push(c);
            }
        }
    }    
    /**
     * Moves all cards from waste to stack
     */
    private void resetStock()
    {
        while(!waste.isEmpty())
        {
            Card c = waste.pop();
            c.turnDown();
            stock.push(c);
        }   
    }    
    /**
     * When the stock is clicked, the stock is reset if empty, 
     * otherwise 3 cards are sent to waste
     */
    public void stockClicked()
    {
        if(display.isWasteSelected() || display.isPileSelected())
        {
            
        }
        else if(stock.isEmpty())
        {
            resetStock();
        }
        else
        {
            dealThreeCards();
        }
        System.out.println("stock clicked");
        
    }
    /**
     * Called when the waste is clicked. It unselects it
     * or selects it, depending on the state of the game
     * 
     */
    public void wasteClicked()
    {
        if(display.isWasteSelected())
        {
            display.unselect();
        }
        else if(waste.isEmpty() || display.isPileSelected())
        {
            display.unselect();
        }
        else
        {
            display.selectWaste();
        }
        System.out.println("waste clicked");
    }   
    /**
     * Called when the foundation is clicked. May add a card to the foundations, if possible.
     * 
     * Precondition:  0 <= index < 4
     * 
     * @param index index of foundation clicked.
     */
    public void foundationClicked(int index)
    {
        if(display.isWasteSelected())
        {
            Card c = waste.peek();
            if(c.isFaceUp())
            {
                if(canAddToFoundation(c, index))
                {
                    foundations[index].push(c);
                    waste.pop();
                }
            }
            display.unselect();
        }
        else if(display.isPileSelected())
        {
            int i = display.selectedPile();
            Card c = piles[i].peek();
            if(c.isFaceUp())
            {
                if(canAddToFoundation(c, index))
                {
                    foundations[index].push(c);
                    piles[i].pop();
                    if(!piles[i].isEmpty())
                    {
                        piles[i].peek().turnUp();
                    }
                }
            }
            display.unselect();
        }
        
        System.out.println("foundation #" + index + " clicked");
    }    
    /**
     * Checks if the card can be legally added to a foundation
     * 
     * Precondition: 0<= index < 4
     * 
     * @param card card to check
     * @param index index of pile
     * 
     * @return true if the card can be legally added, otherwise
     *         false
     */
    private boolean canAddToFoundation(Card card, int index)
    {
        if(foundations[index].isEmpty())
        {
            if(card.getRank() != 1)
            {
                return false;
            }
            return true;
        }
        else
        {
            Card f = foundations[index].peek();
            String s1 = card.getSuit();
            String s2 = f.getSuit();
            if(s1.equals(s2))
            {
                int r1 = card.getRank();
                int r2 = f.getRank();
                if(r1 == r2 + 1)
                {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Called whien pile is clicked. Adds things on top or remove them into a different pile/foundation, if possible.
     * @param the index of the pile clicked
     * Precondition: 0 <= index <= 7
     */
    public void pileClicked(int index)
    {
        if(display.isPileSelected())
        {
            int i = display.selectedPile();
            
            Stack<Card> temp = new Stack<Card>();
            while(!piles[i].isEmpty() && piles[i].peek().isFaceUp())
            {
                temp.push(piles[i].pop());
            }
            Card c = temp.peek();
            if(canAddToPile(c, index))
            {
                addToPile(temp, index);
                if(!piles[i].isEmpty())
                {    
                    piles[i].peek().turnUp();
                }
            }
            else
            {
                while(!temp.isEmpty())
                {
                    piles[i].push(temp.pop());
                }
                System.out.println("Can't legally add");
            }
            display.unselect();
        }
        else if(display.isWasteSelected())
        {
            Card c = waste.peek();
            if(canAddToPile(c, index))
            {
                c = waste.pop();
                piles[index].push(c);
                display.unselect();
            }
        }
        else if(!display.isPileSelected())
        { 
            if(!piles[index].isEmpty())
            {
                if(piles[index].peek().isFaceUp())
                {
                    display.selectPile(index);
                }
            }
            else // face down
            {
                piles[index].peek().turnUp();
            }
        }
        else if(index == display.selectedPile())
        {
            display.unselect();
        }
        System.out.println("pile #" + index + " clicked");
    }
    /**
     * Checks if a given card can be legally added to the given pile
     * 
     * Precondition: 0 <= index < 7
     * 
     * @param card card to check
     * @param index pile to check
     * 
     * @return true if the card can be legally added, otherwise
     *         false
     */
    private boolean canAddToPile(Card card, int index)
    {
        if(piles[index].isEmpty())
        {
            if(card.getRank() != 13) 
            {
                return false;
            }
            return true;
        }
        Card p = piles[index].peek();
        if(!p.isFaceUp())// face down
        {
            return false;
        }
        else if( (p.isRed() && !card.isRed()) || (!p.isRed() && card.isRed()) )
        {
            if(card.getRank() == p.getRank() -1) 
            {
                return true;
            }
        }
        return false;
    }
    /**
     * Removes face up cards from a given pile and returns a stack containing them
     * 
     * Precondition: 0 <= index < 7
     * 
     * @param index pile to check
     * 
     * @return a stack of the removed cards.
     */
    private Stack<Card> removeFaceUpCards(int index)
    {
        Stack<Card> s = new Stack<Card>();
        if(piles[index].isEmpty())
        {
            return null;
        }
        else 
        {
            for(int i = 0; i < piles[index].size(); i++)
            {
                Card c = piles[index].peek();
                if(c.isFaceUp())
                {
                    c = piles[index].pop();
                    s.push(c);
                }
            }
        }
        return s;
    }
    /**
     * Adds given cards to a pile at a given idnex
     * 
     * Precondition 0<= index < 7
     * 
     * @param cards cards to add to pile
     * @param index index to check
     */
    private void addToPile(Stack<Card> cards, int index)
    {
        while(!cards.isEmpty())
        {
            piles[index].push(cards.pop());
        }
    }
}