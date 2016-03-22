package myLinkedList;


/**
 * Creates a ListNode, with the exception that the previous node can be acessed
 * 
 * @author Nikhil Singh
 * @version 10-31-13
 */
public class DoubleNode
{
    private Object value;
    private DoubleNode next;
    private DoubleNode prev;
    public DoubleNode(Object initValue, DoubleNode initNext, DoubleNode initPrev)
    { 
        value = initValue; 
        next = initNext; 
        prev=initPrev;
    }
    public Object getValue() 
    { 
        return value; 
    }
    public DoubleNode getNext() 
    { 
        return next; 
    }
    public void setValue(Object theNewValue) 
    { 
        value = theNewValue; 
    }
    public void setNext(DoubleNode theNewNext)
    { 
        next = theNewNext; 
    }
    public void setPrev(DoubleNode theNewPrev)
    {
        prev=theNewPrev;
    }
    public DoubleNode getPrev()
    {
        return prev;
    }

}
