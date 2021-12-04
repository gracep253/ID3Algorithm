import java.util.ArrayList;
import java.util.Objects;

/**
 * Author: Danny Horn
 * Date: 9/17/2020
 */
public class Node
{
    private String question;
    private Node parent;
    private ArrayList<Node> children;

    public Node(String question)
    {
        this.question = question;
        children= new ArrayList<>();
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }

    public String getQuestion()
    {
        return question;
    }

    private void setParent(Node parent)
    {
        this.parent = parent;
    }

    public Node getParent()
    {
        return parent;
    }

    public void setChildren(ArrayList<Node> children)
    {
        for(int i= 0; i < children.size(); i++)
        {
            children.get(i).setParent(this);
        }
        this.children= children;
    }

    public Node[] getChildren()
    {
        return children.toArray(new Node[0]);
    }

    public void setChild(int idx, Node child)
    {
        child.setParent(this);
        children.set(idx,child);
    }

    public void addChild(Node child)
    {
        child.setParent(this);
        children.add(child);
    }

    public Node getChild(int idx)
    {
        return children.get(idx);
    }

    public void printPreorder()
    {
        printPreorder(this);
    }

    private void printPreorder(Node node)
    {
        if(node != null)
        {
            System.out.println("\n" + node.getQuestion());
            for(Node child : node.getChildren())
            {
                printPreorder(child);
            }
        }
    }

    public void printPostorder()
    {
        printPostorder(this);
    }

    private void printPostorder(Node node)
    {
        if(node != null)
        {
            for(Node child : node.getChildren())
            {
                printPostorder(child);
            }
            System.out.println("\n" + node.getQuestion());
        }
    }

    public void printInOrder()
    {
        printInOrder(this);
    }

    private void printInOrder(Node node)
    {
        if(node != null)
        {
            for(int i= 0; i < node.getChildren().length/2; i++)
            {
                printInOrder(node.getChildren()[i]);
            }
            System.out.println("\n" + node.getQuestion());
            for(int i= node.getChildren().length/2; i < node.getChildren().length; i++)
            {
                printInOrder(node.getChildren()[i]);
            }
        }
    }

//    public String print()
//    {
//        return this.print("",true,"");
//    }

//    public String print(String prefix, boolean isTail, String sb)
//    {
//        if(childRight != null)
//            childRight.print(prefix + (isTail ? "│    " : "    "), false, sb);
//        System.out.println( prefix+(isTail ? " \\-- " : " /-- ")+value);
//        if(childLeft != null)
//            childLeft.print(prefix+(isTail ? "     " : "│   "), true, sb);
//        return sb;
//    }


    @Override
    public boolean equals(Object o)
    {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        Node node = (Node) o;
        return question == node.question &&
                parent.equals(node.parent) &&
                children.equals(node.children);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(question, parent, children);
    }

    @Override
    public String toString()
    {
        String str= "Node{value= " + question + ", parent= ";
        if(parent != null)
        {
            str+= parent.getQuestion();
        }
        else
        {
            str+= "null (this is root)";
        }

        str+= ", Children= ";
        if(children != null)
        {
            StringBuilder stringBuilder= new StringBuilder();
            for(int i= 0; i < children.size(); i++)
            {
                stringBuilder.append(children.get(i).getQuestion()).append("/");
            }
            str+= stringBuilder.toString();
        }
        else
        {
            str+= "null (no children)";
        }


        str+= "}";

        return str;
    }

//    public void print2()
//    {
//        print2(this);
//    }
//
//    public void print2(Node node)
//    {
//        while(node != null)
//        {
//            System.out.println();
//        }
//    }
}
