/** 
 * @author: Nikhil Singh 
 * @version: 12/22/13 
 */ public abstract class BSTUtilities 
{ 
    /** 
     * Tests to see if the tree contains x 
     * precondition: t is a binary search tree in ascending order 
     * postcondition: returns true if t contains the value x; otherwise, returns false 
     * @param t the TreeNode that is being tested 
     * @param x a thing that can be compared with the individual TreeNode values and is being tested to see if it is in the Tree 
     * @param display the display that shows the Tree 
     * @return true if x is in t; false otherwise 
     */ 
    public static boolean contains(TreeNode t, Comparable x, TreeDisplay display) 
    { 
        while(t != null) 
        {
            display.visit(t); 
            int compare = x.compareTo(t.getValue()); 
            if(compare > 0) 
                t = t.getRight(); 
            else if(compare < 0) 
                t = t.getLeft(); 
            if(compare == 0) 
                return true; 
        } 
        return false; 
    } 

    /** 
     * precondition: t is a binary search tree in ascending order 
     * postcondition: if t is empty, returns a new tree containing x; otherwise, returns t, with x having been inserted at the appropriate position 
     * to maintain the binary search tree property; x is ignored if it is a duplicate of an element already in t; only one new TreeNode 
     * is created in the course of the traversal 
     * @param t the TreeNode that is being used 
     * @param x a thing that can be compared with the individual TreeNode values and is being added to the Tree 
     * @param display the display that shows the Tree 
     * @return the TreeNode that was added and contains the value 
     */ 
    public static TreeNode insert(TreeNode t, Comparable x, TreeDisplay display) 
    { 
        TreeNode temp = t; 
        boolean d = true; 
        if(t == null) 
            return new TreeNode(x); 
        while(d) 
        { 
            display.visit(temp); 
            int compare = x.compareTo(temp.getValue()); 
            if(compare > 0 && temp.getRight() != null) 
                temp = temp.getRight(); 
            else if(compare < 0 && temp.getLeft() != null) 
                temp = temp.getLeft(); 
            else if(compare == 0) 
                return t; else d = false; } 
        int compare = x.compareTo(temp.getValue()); 
        if(compare > 0) temp.setRight(new TreeNode(x)); 
        else if(compare < 0) 
            temp.setLeft(new TreeNode(x)); 
        return t; 
    } 

    /** 
     * Deletes a treenode from a tree 
     * precondition: t is a binary search tree in ascending order 
     * postcondition: returns a pointer to a binary search tree, in which the value at node t has been deleted (and no new TreeNodes have been created) 
     * @param t the TreeNode that is being deleted 
     * @param display the display that shows the Tree 
     * @return the tree without the TreeNode that was deleted 
     */ 
    private static TreeNode deleteNode(TreeNode t, TreeDisplay display) 
    { 
        if(t.getRight() == null && t.getLeft() == null) 
        { 
            return null; 
        } 
        if(t.getRight() == null) 
        { 
            t.setValue(t.getLeft().getValue()); 
            t.setRight(t.getLeft().getRight()); 
            t.setLeft(t.getLeft().getLeft()); 
            return t; 
        } 
        TreeNode right = t.getRight(); 
        TreeNode left = t.getLeft(); 
        TreeNode rl = right.getLeft(); 
        if(rl == null) 
        { 
            t.setValue(right.getValue()); t.setRight(right.getRight()); 
        } 
        else 
        { 
            t.setValue(rl.getValue()); 
            t.setLeft(rl.getLeft()); 
            TreeNode l2 = t.getLeft(); 
            while(l2 != null && l2.getLeft() != null) 
            { 
                l2 = l2.getLeft(); 
            } 
            if(l2 != null) 
            { 
                l2.setLeft(left); 
            } 
            else 
            { 
                t.setLeft(left); 
            } 
            rl.setLeft(null); 
            right.setLeft(deleteNode(rl, display)); 
        } 
        return t; 
    } 

    /** 
     * Deletes a treenode from a tree 
     * precondition: t is a binary search tree in ascending order 
     * postcondition: returns a pointer to a binary search tree, in which the value at node t has been deleted (and no new TreeNodes have been created) 
     * @param t the TreeNode that is being deleted 
     * @param x a thing that can be compared with the individual TreeNode values and is being deleted from the Tree 
     * @param display the display that shows the Tree 
     * @return the tree without the TreeNode that was deleted 
     */ 
    public static TreeNode delete(TreeNode t, Comparable x, TreeDisplay display) 
    { 
        if(!contains(t,x,display)) 
            return t; 
        if(t == null) 
            return null; 
        if(x.compareTo(t.getValue()) == 0) 
            return deleteNode(t,display); 
        if(x.compareTo(t.getValue()) > 0) 
        { 
            t.setRight(delete(t.getRight(),x,display)); 
        } 
        else if( x.compareTo(t.getValue()) <0) 
        { 
            t.setLeft(delete(t.getLeft(),x,display)); 
        } 
        return t; 
    } 
}