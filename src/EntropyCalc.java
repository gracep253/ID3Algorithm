import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class EntropyCalc
{
    public static void main(String[] args) throws IOException
    {
        EntropyCalc entropyCalc= new EntropyCalc("Play.csv");
    }

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    private static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";


    private static final String[] colorOrder= new String[] {ANSI_BLUE, ANSI_RED, ANSI_CYAN, ANSI_GREEN, ANSI_PURPLE, ANSI_YELLOW, ANSI_BLACK, ANSI_WHITE};



    private final String path= System.getProperty("user.dir") + File.separator;
    private Table csv;
    private JTreeNode root;

    public EntropyCalc(String csvFile) throws IOException
    {
        String csvPath= path + csvFile;
        CsvReader reader= new CsvReader(csvPath);

        csv= reader.getTable();
        csv.print(ANSI_BLUE);


        primaryGains();
        makeFinalResult(root);


        TreeWindow treeWindow= new TreeWindow(root);
    }


    //        Table[] ts = csv.groupBy("Past Performance Level", "Attend Extra Help?");
//        for (Table t : ts) {
//            t.print();
//        }

    public void primaryGains()
    {

        ArrayList<ArrayList<Table>> csvSubTables= new ArrayList<>();
        ArrayList<Double> gains= new ArrayList<>();

        int totalYes= csv.getColumnType(csv.getWidth()-1,"yes").length;
        int totalNo= csv.getColumnType(csv.getWidth()-1,"no").length;

        System.out.println("Total Yes: " + totalYes);
        System.out.println("Total No: " + totalNo);

        System.out.println("Entropy for " + csv.getCell(0,csv.getWidth()-1) + ": " + calculateEntropy(totalYes,totalNo) + "\n\n");

        for(int i= 0; i < csv.getWidth()-1; i++)
        {
            Table table= new Table(csv.getCell(0,i));
            table.addRow(csv.getCell(0,i), "Yes", "No", "Total", "Entropy", "Indiv. Gain","Total Gain");

            double[] individualGains= new double[csv.getUniqueColumnValues(i).length-1];

            ArrayList<Table> subTables= new ArrayList<>();

            for(int x= 1; x < csv.getUniqueColumnValues(i).length; x++)
            {
                int numYes= csv.columnMatches(i, csv.getUniqueColumnValues(i)[x], "yes");
                int numNo= csv.columnMatches(i, csv.getUniqueColumnValues(i)[x], "no");
                individualGains[x-1]= (double)(numNo + numYes)/(csv.getHeight()-1)*calculateEntropy(numYes,numNo);

                table.addRow(csv.getUniqueColumnValues(i)[x], "" + numYes, "" + numNo, "" + (numNo + numYes), "" + calculateEntropy(numYes,numNo), "" + individualGains[x-1],"");

                Table subTable= Table.copyOf(csv);
                subTable.removeRowIfNot(csv.getUniqueColumnValues(i)[x]);
                System.out.println();
                subTable.setName(csv.getUniqueColumnValues(i)[x]);
                subTable.removeColumn(i);
                subTables.add(subTable);
            }

            csvSubTables.add(subTables);

            table.addRow("","","","","","" + sum(individualGains),"" + (calculateEntropy(totalYes,totalNo) - sum(individualGains)));

            table.print(colorOrder[0]);
            gains.add(calculateEntropy(totalYes,totalNo) - sum(individualGains));
        }


        int[] order= generateSortedDoubleOrder(gains.toArray(new Double[0]));

        for(int i= 0; i < 1; i++)
        {
            JTreeNode newNode= new JTreeNode(csv.getCell(0,order[i]),csv);
            root= newNode;
            System.out.println(colorOrder[0] + "Highest: " + csv.getCell(0,order[i]) + ANSI_RESET);
            System.out.println(colorOrder[1] + "------------------------------------------------------------------------------------------------------------------------------------------------------" + ANSI_RESET);
            childGains(csvSubTables.get(order[i]),csv.getCell(0,order[i]), newNode, 1,"", true);
        }
    }



    public void childGains(ArrayList<Table> childTables, String branchName, DefaultMutableTreeNode node, int color, String gap, boolean first)
    {
        for(int y= 0; y < childTables.size(); y++)
        {
            Table child= childTables.get(y);

            if(first)
                child.print(colorOrder[color]);

            ArrayList<ArrayList<Table>> childSubTables= new ArrayList<>();
            ArrayList<Double> gains= new ArrayList<>();
            ArrayList<Table> calculationTableList= new ArrayList<>();



            int totalYes= child.getColumnType(child.getWidth()-1,"yes").length;
            int totalNo= child.getColumnType(child.getWidth()-1,"no").length;

            if(first)
            {
                System.out.println("Total Yes: " + totalYes);
                System.out.println("Total No: " + totalNo);

                System.out.println("Entropy for " + child.getCell(0,child.getWidth()-1) + ": " + calculateEntropy(totalYes,totalNo) + "\n\n");
            }


            for(int i= 0; i < child.getWidth()-1; i++)
            {
                Table calculationTable= new Table(child.getCell(0,i));
                calculationTable.addRow(child.getCell(0,i), "Yes", "No", "Total", "Entropy", "Indiv. Gain","Total Gain");

                double[] individualGains= new double[child.getUniqueColumnValues(i).length-1];
                HashMap<Integer,Double> map= new HashMap<>();
                double[] entropyArr= new double[1];

                ArrayList<Table> subTables= new ArrayList<>();

                for(int x= 1; x < child.getUniqueColumnValues(i).length; x++)
                {
                    int numYes= child.columnMatches(i, child.getUniqueColumnValues(i)[x], "yes");
                    int numNo= child.columnMatches(i, child.getUniqueColumnValues(i)[x], "no");
                    individualGains[x-1]= (double)(numNo + numYes)/(child.getHeight()-1)*calculateEntropy(numYes,numNo);

                    calculationTable.addRow(child.getUniqueColumnValues(i)[x], "" + numYes, "" + numNo, "" + (numNo + numYes), "" + calculateEntropy(numYes,numNo), "" + individualGains[x-1],"");

                    map.put(x-1, calculateEntropy(numYes,numNo));
                    entropyArr[x-1]= calculateEntropy(numYes,numNo);
                    entropyArr= Arrays.copyOf(entropyArr, entropyArr.length+1);


                    Table subTable= Table.copyOf(child);
                    subTable.removeRowIfNot(child.getUniqueColumnValues(i)[x]);
                    subTable.removeColumn(i);
                    subTable.setName(child.getUniqueColumnValues(i)[x]);
                    subTables.add(subTable);

                }

                childSubTables.add(subTables);

                calculationTable.addRow("","","","","","" + sum(individualGains),"" + (calculateEntropy(totalYes,totalNo) - sum(individualGains)));

                gains.add(calculateEntropy(totalYes,totalNo) - sum(individualGains));


                if(first)
                    calculationTable.print(colorOrder[color]);
                calculationTableList.add(calculationTable);
            }

            Double[] gainsArray= gains.toArray(new Double[0]);

            int[] order= generateSortedDoubleOrder(gainsArray);

//            if(color == colorOrder.length-1)
//            {
//                System.out.println(colorOrder[0] + "---------------------------------------------------" + ANSI_RESET);
//            }
//            else
//            {
//                System.out.println(colorOrder[color+1] + "---------------------------------------------------" + ANSI_RESET);
//            }

            int numOccurances= numOccurances(gainsArray,getMax(gainsArray));

            System.out.println("Number of occurances of max: " + numOccurances);

            for(int i= 0; i < order.length; i++)
            {
                JTreeNode newNode= new JTreeNode( childTables.get(y).getName() + ": " + calculationTableList.get(order[i]).getName(), childTables.get(y));
                node.add(newNode);


                if(color + 1 == colorOrder.length)
                    color= 0;

                childGains(childSubTables.get(order[i]),childTables.get(y).getName(),newNode, color+1,gap + "|   ", false);


                if(first)
                    System.out.println(gap + colorOrder[color] + "---------------------------------------------------" + ANSI_RESET);

                break;
            }



        }

    }

    private double calculateEntropy(int numYes, int numNo)
    {
        double total= (double) numYes + (double) numNo;

        if(numYes != numNo)
        {
            if(numYes == 0 || numNo == 0)
            {
                return 0;
            }

            return -((double) numYes /total)*log2(numYes/total) - ((double) numNo /total)*log2(numNo/total);
        }
        else
        {
            return 1;
        }

    }




    private double log(double num, int base)
    {
        return (Math.log(num)/ Math.log(base));
    }

    private double log2(double num)
    {
        return log(num,2);
    }

    private double sum(double... arr)
    {
        double num= 0;
        for(double d : arr)
            num+= d;

        return num;
    }

    private static int[] generateSortedDoubleOrder(Double... arr)
    {
        int n = arr.length;
        double[] newArr= new double[n];
        int[] order= new int[n];

        for(int i= 0; i < arr.length; i++)
        {
            newArr[i]= arr[i];
        }


        for (int i= 0; i < n-1; i++)
        {
            int idx = i;
            for (int j= i+1; j < n; j++)
            {
                if (newArr[j] > newArr[idx])
                    idx= j;
            }

            double temp= newArr[idx];
            newArr[idx]= newArr[i];
            newArr[i]= temp;
        }

        for(int x= 0; x < newArr.length; x++)
        {
            order[x]= indexOf(arr, newArr[x]);
        }

        return order;
    }

    private static int indexOf(Double[] arr, double num)
    {
        for(int i= 0; i < arr.length; i++)
        {
            if(arr[i] == num)
            {
                return i;
            }
        }
        return -1;
    }

    private void makeFinalResult(JTreeNode node)
    {
        if(node != null)
        {
            for(int i= 0; i < node.getChildCount(); i++)
            {
                makeFinalResult((JTreeNode) node.getChildAt(i));
            }

            if(node.getChildCount() == 0)
            {
                Table table= node.getUserData();
                if(numOccurances(table.getLastColumn(),"yes") == table.getHeight()-1 || numOccurances(table.getLastColumn(),"yes") > numOccurances(table.getLastColumn(),"no"))
                {
                    node.add(new JTreeNode(csv.getLastColumn()[0] + ": Yes"));
                }
                else if(numOccurances(table.getLastColumn(),"no") == table.getHeight()-1  || numOccurances(table.getLastColumn(),"yes") < numOccurances(table.getLastColumn(),"no"))
                {
                    node.add(new JTreeNode(csv.getLastColumn()[0] + ": No"));
                }
                else if (numOccurances(table.getLastColumn(),"yes") == numOccurances(table.getLastColumn(),"no"))
                {
                    node.add(new JTreeNode(csv.getLastColumn()[0] + ": UNABLE TO DETERMINE"));
                }
            }
        }
    }

    private void fooBar(JTreeNode node)
    {
        if(node != null)
        {
            for(int i= 0; i < node.getChildCount(); i++)
            {
                fooBar((JTreeNode) node.getChildAt(i));

                if(node.getChildCount() == 1)
                {
                    JTreeNode child= (JTreeNode) node.getChildAt(0);
                    String childName= (String) child.getUserObject();
                }
            }

        }
    }

    private int numOccurances(String[] arr, String str)
    {
        int num= 0;
        for(String s : arr)
        {
            if(s.equalsIgnoreCase(str))
                num++;
        }
        return num;
    }


    private int numOccurances(double[] arr, double value)
    {
        int num= 0;
        for(double i : arr)
        {
            if(i == value)
                num++;
        }
        return num;
    }

    private int numOccurances(Double[] arr, double value)
    {
        double[] newArr= new double[arr.length];
        for(int i= 0; i < arr.length; i++)
        {
            newArr[i]= arr[i];
        }
        return numOccurances(newArr,value);
    }

    private double getMax(double[] arr)
    {
        double max= Double.MIN_VALUE;
        for(double d : arr)
            max= Math.max(max,d);

        return max;
    }

    private double getMax(Double[] arr)
    {
        double[] newArr= new double[arr.length];
        for(int i= 0; i < arr.length; i++)
        {
            newArr[i]= arr[i];
        }

        return getMax(newArr);
    }
}
