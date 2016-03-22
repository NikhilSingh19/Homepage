/**
 * @author: Nikhil Singh
 * @version: 12/22/13 
 */
public class MyTreeSet<E>
{
    private TreeNode root;
    private int size;
    private TreeDisplay display;

    /**
     * initializes a MyTreeSet object
     */
    public MyTreeSet()
    {
        root = null;
        size = 0;
        display = new TreeDisplay();

        //wait 1 millisecond when visiting a node
        display.setDelay(1);
    }

    /**
     * returns the size
     * @return the size
     */
    public int size()
    {
        return size;
    }

    /**
     * says whether or not a object is contained in the MyTreeSet
     * @return true if the object is in MyTreeSet, false if it is not in the MyTreeSet
     * @param Object the object to check if it is contained
     */
    public boolean contains(Object obj)
    {
        return BSTUtilities.contains(root, (Comparable)obj, display);
    }

    /**
     * adds an object to the MyTreeSet
     * @return true if it is added, false if it is already in the TreeSet
     * @param obj, the object to add into the MyTreeSet
     */
    public boolean add(E obj)
    {
        if(!contains((Object)obj))
        {
            root=BSTUtilities.insert(root, (Comparable) obj, display);
            size++;
            return true;
        }
        return false;
    }

    /**
     * removes obj from the list
     * @param obj, the object to remove
     * @return true if it is in the MyTreeSet and removed, false if it is not in the set
     */ 
    public boolean remove(Object obj) 
    { 
        Comparable x = (Comparable)(obj); 
        if(!contains(obj)) 
            return false; 
        root = BSTUtilities.delete(root, x, display); 
        size--; 
        display.displayTree(root); 
        return true; 
    } 

    /**
     * returns the format of the string
     * @return String, the format of the return
     */
    public String toString()
    {
        return toString(root);
    }

    /**
     * formats the output
     * @return String, the output
     * @param TreeNode t, the tree to output the string for
     */
    private String toString(TreeNode t)
    {
        if (t == null)
            return " ";
        return toString(t.getLeft()) + t.getValue() + toString(t.getRight());
    }
}