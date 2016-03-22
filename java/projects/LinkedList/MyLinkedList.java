package myLinkedList;

import java.util.Iterator; import java.util.ListIterator;
/** *The MyLinkwsList class is an object that points to he next 
 *object and the previous object in the Linked List. Each object 
 *is connected to another object. 
 * Essentially, MyLinkedList is an 
 *editable List of Objects in which you can manipulate the list. 
 *These manipulations include adding, removing, and resizing the 
 *list of objects. 
 *@author : Nikhil Singh 
 *@version : 10/31/13 
 */ 
public class MyLinkedList<E> implements MyList<E> { 
    private DoubleNode first; 
    private DoubleNode last; 
    private int size; 
    /** 
     * constructor of the MyLinkedList Class 
     * initializes all the variables to null 
     */ 
    public MyLinkedList() 
    {
        first = null; 
        last = null; 
        size = 0; 
    }

    /** 
     * Creates a string display for the MyLinkedList class that
     * displays the values of the objects
     * @return a string containing a display for the MyLinkedList class
     */ 
    public String toString() 
    { 
        DoubleNode f = first; 
        if (size == 0 || f == null) 
            return "[]"; 
        String s = "["; 
        while (f.getNext() != null) 
        { 
            s += f.getValue() + ", "; f = f.getNext(); 
        } 
        return s + f.getValue() + "]"; 
    }

    /** 
     * Returns the size of the linked list 
     * @return the size of the linked list
     */ 
    public int size() 
    { 
        return size; 
    }

    /** 
     * Returns the element at the specified position in this list 
     * @param index of the element to return 
     * @return the doubleNode at the index 
     */ 
    public E get(int index) 
    { 
        DoubleNode temp = getNode(index); 
        return (E)(temp.getValue());

    }

    /** 
     * 
     * Replaces the element at the specified position in this list with the other specified element. 
     * precondition: the index must exist in the MyLinkedList 
     * @param index of the element to replace; 0<=index<size
     * @param Object to be stored at the specified position 
     * @return: the element formerly at the specified position 
     **/ 
    public E set(int index, E obj) 
    { 
        E temp = get(index);
        getNode(index).setValue(obj); 
        return temp;

    }

    /** 
     * Inserts the specified element at the specified position in this list. 
     * Shifts the element currently at that position and any elements after 
     * elements to the right, so one is added to the indices. 
     * @param the Object to be inserted 
     *  @return true if add is succesfull 
     */ 
    public boolean add(E obj) 
    { 
        if(last == null) 
        { 
            last = new DoubleNode(obj, null, null); 
            first = last; 
        } 
        else 
        {
            DoubleNode temp = new DoubleNode(obj, null, last); 
            temp.setPrev(last); last.setNext(temp); 
            last = temp; 
        } 
        size++; 
        return true; }

    /** 
     * Gets the node at the requested index from the first node 
     * precondition: 0 <= index <= size / 2 
     * @param index the index of the requested node 
     * @return the node that is at the index 
     */ 
    private DoubleNode getNodeFromFirst(int index) 
    {
        DoubleNode temp = first; 
        for(int i = 0; i < index; i++) 
        { 
            temp = temp.getNext(); 
        } 
        return temp; 
    }

    /** 
     * Gets the node at the requested index from the last node 
     * precondition: size / 2 <= index < size 
     * @param index the index of the node 
     * @return the node that is the last at the index 
     */ 
    private DoubleNode getNodeFromLast(int index) 
    { 
        DoubleNode temp = last; 
        for(int i = size - 1; i > index; i--) 
        { 
            temp = temp.getPrev(); 
        } return temp; 
    }

    /** 
     * Gets the node at the index 
     * precondition: 0 <= index < size 
     * postcondition: starting from first or last (whichever is closer), returns the node with given index 
     * @param index the index of the requested node 
     * @return the node that is at the index 
     */ 
    private DoubleNode getNode(int index) 
    { 
        if(index <= size/2) 
        { 
            return getNodeFromFirst(index); 
        } 
        return getNodeFromLast(index); 
    }

    /** 
     * Adds a new object to a specified index in the list 
     * precondition: 0 <= index <= size 
     * postcondition: inserts obj at position index, moving elements after the index out 1 and increments the size 
     * @param obj the object that is added to the list 
     * @param index the index at which the object is to be added 
     */ 
    public void add(int index, E obj) 
    { 
        DoubleNode temp = new DoubleNode(obj, null, null); 
        if (index == 0) { if (size == 0) 
            { 
                first = temp; 
                last = first; 
            } 
            else 
            { 
                temp.setNext(first); 
                first.setPrev(temp); 
                first = temp; 
            } 
        } 
        else if (index == size ) 
        { 
            temp.setPrev(last); 
            last.setNext(temp); 
            last = temp; 
        } 
        else 
        { 
            DoubleNode in = getNode(index); 
            temp.setPrev(in.getPrev()); 
            in.getPrev().setNext(temp); 
            temp.setNext(in); 
            in.setPrev(temp); 
        } 
        size++;
    }

    /** 
     * Removes the element at the specified position in this list. Shifts any subsequent elements 
     * to the left (subtracts one from their indices). 
     * @return the element that was removed from the list. 
     * @param index - the index of the element to be removed; 0<=index<size
     **/ 
    public E remove(int index) 
    { DoubleNode tempn; 
        DoubleNode tempp; 
        E temp; 
        if((size > 0)&&(index >= 0)&&(index < size)) 
        { 
            temp = get(index);
        } 
        else 
        { 
            return null; 
        } 
        if (size == 1) 
        { 
            first = null; 
            last = null; 
            size = 0; 
            return temp; 
        } 
        if((first != null)&&(index>0)) 
        { 
            tempp = getNode(index-1); 
        } else { tempp = null; } 
        if((last != null)&&(index<size-1)) 
        { 
            tempn = getNode(index+1); 
        } 
        else 
        { 
            tempn = null; 
        } 
        if (index == size-1) 
        { 
            last = tempp; 
        } 
        if (index == 0) 
        { 
            first = tempn; 
        } 
        if(tempp != null) 
        { 
            tempp.setNext(tempn); 
        } 
        if(tempn != null) 
        { 
            tempn.setPrev(tempp); 
        }
        size--;
        return temp; } 

    /** 
     * returns null because this has not been implemented yet 
     */ 
    public Iterator<E> iterator() 
    { 
        return null; 
    }

    /** 
     * returns null because this has not been implemented yet 
     */ 
    public MyListIterator<E> listIterator() 
    {
        return null; 
    }

}