import java.util.*;

public class Table
{
    public static final String ANSI_RESET = "\u001B[0m";


    private ArrayList<ArrayList<String>> table;
    private String name;

    public Table()
    {
        table= new ArrayList<>();
        name= "";
    }

    public Table(String name)
    {
        table= new ArrayList<>();
        this.name= name;
    }

    public Table(int height, int width)
    {
        name= "";
        String[][] table= new String[height][width];
        for(int i= 0; i < table.length; i++)
        {
            Arrays.fill(table[i],"");
        }

        setTable(table);

//        this.table.forEach(strings -> System.out.println(strings));
    }

    public Table(String name, int height, int width)
    {
        this.name= name;
        String[][] table= new String[height][width];
        for(int i= 0; i < table.length; i++)
        {
            Arrays.fill(table[i],"");
        }

        setTable(table);
    }

    public Table(String[][] table)
    {
        name= "";
        setTable(table);
    }

    public Table(String name, String[][] table)
    {
        this.name= name;
        setTable(table);
    }


    public void setTable(String[][] table)
    {
        ArrayList<ArrayList<String>> newTable= new ArrayList<>();
        for(int x= 0; x < table.length; x++)
        {
            ArrayList<String> row = new ArrayList<>(Arrays.asList(table[x]));
            newTable.add(row);
        }

        this.table = newTable;
    }

    public String[][] getTable()
    {
        String[][] retTable= new String[table.size()][];
        for(int i= 0; i < table.size(); i++)
        {
            retTable[i]= table.get(i).toArray(new String[0]);
        }

        return retTable;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getHeight()
    {
        return table.size();
    }

    public int getWidth()
    {
        return table.get(0).size();
    }



    public String getCell(int row, int col)
    {
        assert row < getHeight() && col < getWidth();
        return table.get(row).get(col);
    }

    public String[] getRow(int row)
    {
        return table.get(row).toArray(new String[0]);
    }

    public String[] getColumn(int col)
    {
//        assert col < table.size();
        String[] column= new String[table.size()];

        for(int i= 0; i < table.size(); i++)
        {
            column[i]= table.get(i).get(col);
        }

        return column;
    }

    public String[] getLastColumn()
    {
        return getColumn(table.get(0).size()-1);
    }

    public void addRow(String... row)
    {
        table.add(new ArrayList<>(Arrays.asList(row)));
    }

    public void addColumn(String... column)
    {
        for(int x= 0; x < table.size(); x++)
        {
            for(int y= 0; y < table.size(); y++)
            {
                table.get(x).add(column[x]);
            }
        }
    }

    public void removeRow(int row)
    {
        table.remove(row);
    }

    public void removeColumn(int column)
    {
        assert column < getWidth();
        for(int i= 0; i < table.size(); i++)
        {
            table.get(i).remove(column);
        }
    }

    public Table[] groupBy(String... columns) {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i=0; i<columns.length; i++) {
            for (int j=0; j<getWidth(); j++) {
                if(getCell(0, j).equals(columns[i])) {
                    indices.add(j);
                }
            }
        }
        if ((indices.size() != columns.length)) {
            throw new RuntimeException("Unknown column names: "+Arrays.toString(columns));
        }
        Map<String[], ArrayList<ArrayList<String>>> map = new HashMap<>();
        for (int r=1; r<getHeight(); r++) {
            String[] key = new String[indices.size()];
            for (int c=0; c<indices.size(); c++) {
                key[c] = getCell(r, indices.get(c));
            }
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }
            map.get(key).add(table.get(r));
        }

        Table[] result = new Table[map.size()];
        int i = 0;
        for (ArrayList<ArrayList<String>> t : map.values()) {
            ArrayList<String[]> a = new ArrayList<>();
            for (ArrayList<String> row : t) {
                a.add(row.toArray(new String[0]));
            }
            result[i++] = new Table(a.toArray(new String[0][0]));
        }
        return result;
    }

    public void removeRowIfNot(String str)
    {
        ArrayList<ArrayList<String>> newTable= new ArrayList<>();
        newTable.add(table.get(0));

        for(ArrayList<String> row : table)
        {
            if(row.contains(str))
                newTable.add(row);
        }

//        for(int i= 1; i < table.size(); i++)
//        {
//            if(rowContains(getTable()[i],str))
//            {
//                newTable.add(table.get(i));
//            }
//        }
        table= newTable;
    }

    public int columnMatches(int column, String typeType, String resultType)
    {
        String[] typeColumn= getColumn(column);
        String[] resultColumn= getLastColumn();
        assert typeColumn.length == resultColumn.length;

//        System.out.println("\nTargets: " + typeType + ", " + resultType);

        int num= 0;

        for(int i= 1; i < typeColumn.length; i++)
        {
//            System.out.println("Column: " + typeColumn[i]);
//            System.out.println("Result: " + resultColumn[i]);
            if(typeColumn[i].equalsIgnoreCase(typeType) && resultColumn[i].equalsIgnoreCase(resultType))
            {
                num++;
            }

        }
//        System.out.println("\nNumber matching: " + num + "\n");
        return num;
    }

    public String[] getUniqueColumnValues(int col)
    {
        ArrayList<String> list= new ArrayList<>();
        for(String str: getColumn(col))
        {
            if(!list.contains(str))
            {
                list.add(str);
            }
        }

        return list.toArray(new String[0]);
    }

    public int[] getColumnType(int col, String str)
    {
        ArrayList<Integer> list= new ArrayList<>();
        String[] column= getColumn(col);

        for(int i= 0; i < column.length; i++)
        {
            if(column[i].equalsIgnoreCase(str))
            {
                list.add(i);
            }
        }

        Integer[] integers= list.toArray(new Integer[0]);

        int[] arr= new int[integers.length];
        for(int i= 0; i < arr.length; i++)
        {
            arr[i]= integers[i];
        }

        return arr;
    }

    private boolean rowContains(String[] arr, String str)
    {
        for(String val : arr)
        {
            if(val.equals(str))
                return true;
        }
        return false;
    }


    @Override
    public boolean equals(Object o)
    {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        Table table1 = (Table) o;
        return table.equals(table1.table) &&
                name.equals(table1.name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(table, name);
    }

    @Override
    public String toString()
    {
        String[][] in= getTable();

        for(int i= 0; i < in.length; i++)
        {
            if(in[i].length != getWidth())
                throw new RuntimeException("ERROR");
        }

        StringBuilder stringBuilder = new StringBuilder();

        int[] columnWidths = new int[in[0].length];
        Arrays.fill(columnWidths, Integer.MIN_VALUE);

        for (String[] strings : in)
        {
            for (int y = 0; y < strings.length; y++)
            {
                if (columnWidths[y] < strings[y].length()) {
                    columnWidths[y]= strings[y].length();
                }
            }
        }

//        for(int i = 0; i < columnWidths.length; i++) {
//            columnWidths[i] = Math.min(columnWidths[i], 15);
//        }

        StringBuilder line;
        for(int x = 0; x < table.size(); x++)
        {
            line = new StringBuilder();
            for(int y= 0; y < in[x].length; y++)
            {
                line.append("|");
//                if(in[x][y].length() <= 15)
//                {
                    line.append(fixString(in[x][y],columnWidths[y]));
//                }
//                else
//                {
//                    line.append(fixString(in[x][y],columnWidths[y]));
//                }

                if(y == in[x].length-1)
                {
                    line.append("|");
                }
            }

            if(x == 0) {
                for(int i = 0; i < line.toString().length(); i++) {
                    if(line.toString().charAt(i) == '|') {
                        stringBuilder.append("+");
                    }
                    else {
                        stringBuilder.append("-");
                    }
                }
                stringBuilder.append("\n");
            }

            stringBuilder.append(line.toString()).append("\n");

            for(int i = 0; i < line.toString().length(); i++) {
                if(line.toString().charAt(i) == '|') {
                    stringBuilder.append("+");
                }
                else {
                    stringBuilder.append("-");
                }
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    public void print()
    {
        System.out.println(name + ": ");
        System.out.println(toString() + "\n");
    }

    public void print(String color)
    {
        System.out.println(color + name + ": " + ANSI_RESET);
        System.out.println(color + toString() + "\n" + ANSI_RESET);
    }

    private String fixString(String str, int targetLength)
    {
        return str.length() < targetLength ? str + " ".repeat(targetLength-str.length()) : str.substring(0,targetLength);
    }

    public static Table copyOf(Table table)
    {
        return new Table(table.getTable());
    }

    public static Table copyOfName(Table table)
    {
        return new Table(table.getName(), table.getTable());
    }

}
