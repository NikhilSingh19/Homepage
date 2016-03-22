import java.io.*;
import java.util.*;


/**
 * Does several methods for binary trees like finding information about the tree and saving and rebuilding it.
 * The methods are, leftmost, rightmost, maxDepth, createRandom, countNodes, countLeaves, postOrder, preOrder, inOrder, fillList, saveTree, buildTree
 * loadTree, copy, sameShape, createDecodingTree, insertMorst, decodeMorse, eval, createExpressionTree
 * @author Nikhil Singh
 * @version 12-6-13
 */
public class TreeUtil
{

    private static Scanner in = new Scanner(System.in);

    /**
     * finds the leftmost node
     * @param t the tree to find the leftmost value of
     * precondition:  t is non-empty
     * postcondition: returns the VALUE in the leftmost node of t.
     * @return the left most value of the tree
     */
    public static Object leftmost(TreeNode t)
    {
        while(t.getLeft()!=null)
        {
            t=t.getLeft(); 
        }
        return t.getValue(); 
    }

    /**
     * finds the rightmost node
     * @param t the tree to find the rightmost value of
     * precondition:  t is non-empty
     * postcondition: returns the VALUE in the rightmost node of t.
     * @return the right most value of the tree
     */
    public static Object rightmost(TreeNode t)
    {
        if(t.getRight()==null)
        {
            return t.getValue();
        }
        else
        {
            return rightmost(t.getRight());
        }
    }

    /**
     * finds the maximum depth of the tree
     * @param t to find the maxdepth of
     * postcondition: returns the maximum depth of t, where an empty tree
     * has depth 0, a tree with one node has depth 1, etc
     * @return the maximum depth
     */ 
    public static int maxDepth(TreeNode t)
    {
        if(t==null)
        {
            return 0;
        }
        else if((t.getLeft()==null) && (t.getRight()==null))
        {
            return 1;
        }
        else
        {
            return Math.max(maxDepth(t.getRight())+1, maxDepth(t.getRight())+1);   
        }
    }

    /**
     * creates  a random tree
     * @param depth the depth to create the tree
     * @return a new random tree
     */
    public static TreeNode createRandom(int depth)
    {
        if (Math.random() * Math.pow(2, depth) < 1)
            return null;
        return new TreeNode(((int)(Math.random() * 10)),
            createRandom(depth - 1),
            createRandom(depth - 1));
    }

    /**
     * returns the number of nodes
     * @param t to find the number of nodes in tree t
     * @return the number of nodes in tree t
     */
    public static int countNodes(TreeNode t)
    {
        int total=0; 
        if(t==null)
        {
            return 0;
        }
        else if(t.getLeft()==null && t.getRight()==null)
        {
            return 1;
        }
        else
        {
            total++;
            total+=countNodes(t.getLeft());
            total+=countNodes(t.getRight());
        }
        return total;
    }

    /**
     * returns the number of leaves
     * @param t to find the number of leaves in tree t
     * @return the number of leaves in tree t
     */
    public static int countLeaves(TreeNode t)
    {
        int total=0;
        if(t==null)
        {
            return 0;
        }
        else if(t.getLeft()==null && t.getRight()==null)
        {
            return 1;
        }
        else
        {
            total+=countLeaves(t.getLeft());
            total+=countLeaves(t.getRight());
        }
        return total;
    }

    /**
     * goes through the tree in preorder
     * @param t the tree to go through
     * @param display the display to show the pre order on
     * postcondition: each node in t has been lit up on display
     * in a pre-order traversal
     */
    public static void preOrder(TreeNode t, TreeDisplay display)
    {
        if(t!=null)
        {
            display.visit(t);
            preOrder(t.getLeft(), display);
            preOrder(t.getRight(), display);
        }
    }

    /**
     * goes through the tree in inorder
     * @param t the tree to go through
     * @param display the display to show the in order on
     * postcondition: each node in t has been lit up on display
     * in a in-order traversal
     */
    public static void inOrder(TreeNode t, TreeDisplay display)
    {
        if(t!=null)
        {
            inOrder(t.getLeft(), display);
            display.visit(t);
            inOrder(t.getRight(), display);
        }
    }

    /**
     * goes through the tree in postorder
     * @param t the tree to go through
     * @param display the display to show the post order on
     * postcondition: each node in t has been lit up on display
     * in a post-order traversal
     */
    public static void postOrder(TreeNode t, TreeDisplay display)
    {
        if(t!=null)
        {
            postOrder(t.getLeft(), display);
            postOrder(t.getRight(), display);
            display.visit(t);
        }
    }

    /**
     * fills the given list with the values of tree t,
     * using a pre-order traversal, with "$" values added list
     * whenever null is encountered
     * @param t the tree to get values from
     * @param list the list to fill with values from t
     **/
    public static void fillList(TreeNode t, List<String> list)
    {
        int x=0;
        if(t!=null)
        {
            list.add(String.valueOf(t.getValue()));
            fillList(t.getLeft(), list);
            fillList(t.getRight(), list);
        }
        else
        {
            list.add("$");
        }
    }

    /**
     * saves the contents of t to a file with given name
     * @param filename, the name to name the file
     * @param t the treenode to save
     */
    public static void saveTree(String fileName, TreeNode t)
    {
        List<String> list=new ArrayList<String>();
        fillList(t,list);
        FileUtil.saveFile( fileName, list.iterator());
    }

    /**
     * builds a tree 
     * @param it the iterator with the values for the tree
     * precondition:  it will iterate through a valid description
     * of a tree, including string values and "$" markers
     * postcondition: returns the tree described by it
     */
    public static TreeNode buildTree(Iterator<String> it)
    {
        String here = it.next(); 
        if(here.equals("$")) 
            return null; 
        TreeNode temp = new TreeNode(here); 
        temp.setLeft(buildTree(it)); 
        temp.setRight(buildTree(it)); 
        return temp;
    }

    /**
     * 
     * loads a tree from a save file
     * @param filename, the name of the file storing the tree
     * postcondition: returns the tree described in the given file
     * @return the loaded tree
     */
    public static TreeNode loadTree(String fileName)
    {
        Iterator<String> it =FileUtil.loadFile(fileName);
        TreeNode newTree=buildTree(it);
        return newTree;
    }

    /**
     * waits for the user to type text and press enter.
     * returns that text
     * @return the next line
     */
    private static String getUserInput()
    {
        return in.nextLine();
    }

   
    private static void twentyQuestionsRound(TreeNode t, TreeDisplay display)
    {
        throw new RuntimeException("Implement me!");
    }


    public static void twentyQuestions()
    {
        throw new RuntimeException("Implement me!");
    }

    /**
     * copies the tree
     * @param t the tree to copy
     * @return the copied tree
     * postcondition:  returns a new tree, which is a complete copy
     * of t with all new TreeNode objects pointing
     * to the same values as t (in the same order, shape, etc)
     */
    public static TreeNode copy(TreeNode t)
    {
        List<String> list=new ArrayList<String>();
        fillList(t,list);
        TreeNode newTree=buildTree(list.iterator());
        return newTree;
    }

    /**
     * tests if two trees have the same shape
     * postcondition:  returns true if t1 and t2 have the same
     * shape (but not necessarily the same values);
     * otherwise, returns false
     * @param t1, the first tree to test
     * @param t2, the second tree to test
     * 
     */
    public static boolean sameShape(TreeNode t1, TreeNode t2)
    {
        if(t1 != null && t2 != null)
        {
            if(sameShape(t1.getLeft(),t2.getLeft())) 
                if(sameShape(t1.getLeft(),t2.getLeft())) 
                    return true; 
                else 
                    return false; 
        } 
        if(t1 == null && t2 == null) 
            return true; 
        return false; 
    }

    /**
     * creates a tree that can be used to decode morse code by inserting all the letters into the tree
     * postcondition:  returns a tree for decoding Morse code
     * @param display, the display to show the decoding tree in
     * @return the completed decoding tree
     */
    public static TreeNode createDecodingTree(TreeDisplay display)
    {
        TreeNode tree = new TreeNode("Morse Tree");
        display.displayTree(tree);
        insertMorse(tree, "a", ".-", display);
        insertMorse(tree, "b", "-...", display);
        insertMorse(tree, "c", "-.-.", display);
        insertMorse(tree, "d", "-..", display);
        insertMorse(tree, "e", ".", display);
        insertMorse(tree, "f", "..-.", display);
        insertMorse(tree, "g", "--.", display);
        insertMorse(tree, "h", "....", display);
        insertMorse(tree, "i", "..", display);
        insertMorse(tree, "j", ".---", display);
        insertMorse(tree, "k", "-.-", display);
        insertMorse(tree, "l", ".-..", display);
        insertMorse(tree, "m", "--", display);
        insertMorse(tree, "n", "-.", display);
        insertMorse(tree, "o", "---", display);
        insertMorse(tree, "p", ".--.", display);
        insertMorse(tree, "q", "--.-", display);
        insertMorse(tree, "r", ".-.", display);
        insertMorse(tree, "s", "...", display);
        insertMorse(tree, "t", "-", display);
        insertMorse(tree, "u", "..-", display);
        insertMorse(tree, "v", "...-", display);
        insertMorse(tree, "w", ".--", display);
        insertMorse(tree, "x", "-..-", display);
        insertMorse(tree, "y", "-.--", display);
        insertMorse(tree, "z", "--..", display);
        return tree;
    }

    /**
     * inserts the letter into the morse tree
     * @param decoding tree, the tree to insert it into
     * @param letter the letter to insert
     * @param code, the code of the letter
     * @param display, the display to alter
     */
    private static void insertMorse(TreeNode decodingTree, String letter, String code, TreeDisplay display) 
    { 
        int i = 0; 
        display.displayTree(decodingTree); 
        while(i < code.length()) 
        { 
            display.visit(decodingTree); 
            String c = code.substring(i, i+1); 
            i++; 
            if(c.equals(".")) 
            { 
                if(decodingTree.getLeft() == null) 
                { 
                    decodingTree.setLeft(new TreeNode(null)); 
                } 
                decodingTree = decodingTree.getLeft(); 
            } 
            else 
            { 
                if(decodingTree.getRight() == null) 
                { 
                    decodingTree.setRight(new TreeNode(null)); 
                } 
                decodingTree = decodingTree.getRight(); 
            } 
        } 
        decodingTree.setValue(letter); 
    }

    /**
     * decodes code based on a decoding tree given
     * @param decoding tree the tree to decode with
     * @param cipherText the text to decode
     * @param display, the display to show the decoding on
     * @return the decoded message from cipher text
     */
    public static String decodeMorse(TreeNode decodingTree, String cipherText, TreeDisplay display) 
    { 
        if(cipherText.length() == 0) 
            return ""; 
        int i = 0; 
        TreeNode node = decodingTree; 
        int index = cipherText.indexOf(" "); 
        if(index != -1) 
        { 
            while(i < index) 
            { 
                display.visit(node); 
                String code = cipherText.substring(i, i+1); 
                i++; 
                if(code.equals(".")) { 
                    node = node.getLeft(); 
                } 
                else 
                { 
                    node = node.getRight(); 
                } 
            } 
        } 
        else 
        { while(i < cipherText.length()) 
            { 
                display.visit(node); 
                String code = cipherText.substring(i, i+1); 
                i++; 
                if(code.equals(".")) 
                { 
                    node = node.getLeft(); 
                } 
                else 
                { 
                    node = node.getRight(); 
                } 
            } 
            index = cipherText.length()-1; 
        } 
        String decoded = node.getValue() + decodeMorse(decodingTree, cipherText.substring(index+1), display); 
        return decoded; 
    }
    /**
     * evaluates an expression tree
     * @param expTree the expression tree to evaluate
     */
    public static int eval(TreeNode expTree) 
    { 
        if(expTree == null) 
            return 0; 
        if(!(expTree.getValue() instanceof Integer)) 
        { 
            if(expTree.getValue().equals("*")) 
            { 
                return eval(expTree.getLeft()) * eval(expTree.getRight()); 
            } 
            if(expTree.getValue().equals("/")) 
            { 
                return eval(expTree.getLeft()) / eval(expTree.getRight()); 
            } 
            if(expTree.getValue().equals("+")) 
            { 
                return eval(expTree.getLeft()) + eval(expTree.getRight()); 
            } 
            if(expTree.getValue().equals("-")) 
            { 
                return eval(expTree.getLeft()) - eval(expTree.getRight());
            }
        }
        return (Integer)(expTree.getValue()); 
    }
    /**
     * creates an expression tree
     * @param exp, the expression to create a tree with
     * @return the fully made expression tree.
     */
    public static TreeNode createExpressionTree(String exp) 
    { 
        int count = 0; 
        int count2 = 0; 
        int index = 0; 
        TreeNode node = null; if(exp.length() > 1) 
        { 
            if(exp.substring(index, index+1).equals("(")) 
            { 
                count++; count2++; index++; 
            } 
            while(!(exp.substring(index, index+1).equals("*") || exp.substring(index, index+1).equals("/") || exp.substring(index, index+1).equals("+") || exp.substring(index, index+1).equals("-")) || count != 0) 
            { 
                if(exp.substring(index, index+1).equals("(")) 
                { 
                    count++; 
                    count2++; 
                } 
                else if(exp.substring(index, index+1).equals(")")) 
                { count--; } index++; } node = new TreeNode(exp.substring(index, index+1)); 
            if(count2 > 0) 
                node.setLeft(createExpressionTree(exp.substring(1,index-1))); 
            else 
                node.setLeft(createExpressionTree(exp.substring(0,index))); 
            if(exp.substring(index+1, index+2).equals("(")) 
                node.setRight(createExpressionTree(exp.substring(index+2, exp.length()-1))); 
            else 
                node.setRight(createExpressionTree(exp.substring(index+1)));
        } 
        else if(exp.length() == 1) { 
            node = new TreeNode(Integer.parseInt(exp)); 
        } 
        return node; 
    } 
}

