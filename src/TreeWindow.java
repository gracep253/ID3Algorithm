import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.*;

public class TreeWindow
{
    private JPanel panel1;

    public TreeWindow(DefaultMutableTreeNode root)
    {
        tree1= new JTree(root);
        tree1.putClientProperty("JTree.lineStyle","Angled");
        tree1.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        JFrame frame= new JFrame();

        Dimension dimension= new Dimension(800,800);
        frame.setMinimumSize(dimension);
        frame.setPreferredSize(dimension);
        frame.setContentPane(tree1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setTitle("ID3 Decision Tree");


        tree1.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                if(e.getClickCount() == 2)
                {

                    JTreeNode node= (JTreeNode) tree1.getLastSelectedPathComponent();
                    if(node != null)
                    {
                        Table dataTable= node.getUserData();
                        if(dataTable != null)
                        {
                            String[][] tableArr= dataTable.getTable();
                            String[][] newArr= new String[tableArr.length-1][0];
                            System.arraycopy(tableArr,1,newArr,0,tableArr.length-1);
                            JTable table= new JTable(newArr, dataTable.getRow(0));
                            table.setDragEnabled(false);
                            table.setShowGrid(true);
                            table.setGridColor(new Color(0,0,0));

                            JScrollPane pane= new JScrollPane();
                            pane.setViewportView(table);

                            JFrame tableFrame= new JFrame();

                            Dimension dimension= new Dimension(400,400);
                            tableFrame.setMinimumSize(dimension);
                            tableFrame.setPreferredSize(dimension);
                            tableFrame.setContentPane(pane);
                            tableFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                            tableFrame.pack();
                            tableFrame.setVisible(true);
                            tableFrame.setTitle(node.toString());
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null,"This node has no associated table to display", "Error", JOptionPane.ERROR_MESSAGE);
                        }

                    }

                }
            }
        });
    }

    public static void main(String[] args)
    {
        JFrame frame= new JFrame();
    }

    private JTree tree1;
    private JCheckBox testCheckBox;
}
