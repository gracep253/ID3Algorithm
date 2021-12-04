import javax.swing.tree.DefaultMutableTreeNode;

public class JTreeNode extends DefaultMutableTreeNode
{
    private final Table data;

    public JTreeNode(Object userObject)
    {
        super(userObject);
        data= null;
    }

    public JTreeNode(Object userObject, Table userData)
    {
        super(userObject);
        this.data= userData;
    }

    public Table getUserData()
    {
        return data;
    }
}
