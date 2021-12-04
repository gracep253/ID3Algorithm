
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvReader
{
    public static void main(String[] args) throws IOException
    {
        CsvReader reader= new CsvReader(System.getProperty("user.dir") + File.separator + "Play.csv");
        reader.print();
        System.out.println();
        System.out.println(Arrays.toString(reader.getRow(0)) + "\n");
        System.out.println(Arrays.toString(reader.getColumn(0)) + "\n");
        System.out.println(Arrays.toString(reader.getUniqueColumnValues(0)));

        reader.removeColumn(1);
        reader.print();

    }

    private String[][] in;
    private int line;

    public CsvReader(String filePath) throws IOException
    {
        in= getData(filePath,0,0);
        line= 0;
    }

    /**
     *
     * @param filePath location of csv file
     * @param firstX how many columns (starting from index 0) should be removed when the file is read
     * @param firstY how many rows (starting from index 0) should be removed when the file is read
     * @throws IOException because BufferedReader
     */
    public CsvReader(String filePath, int firstX, int firstY) throws IOException
    {
        in= getData(filePath,firstX,firstY);
        line= 0;
    }

    /**
     *
     * @param filePath location of csv file
     * @param firstX how many columns (starting from index 0) should be removed when the file is read
     * @param firstY how many rows (starting from index 0) should be removed when the file is read
     * @return the csv in the form of a 2D array
     * @throws IOException because BufferedReader
     */
    private String[][] getData(String filePath, int firstX, int firstY) throws IOException
    {
        ArrayList<String> fileLines= new ArrayList<>();
        BufferedReader reader= new BufferedReader(new FileReader(filePath));
        String line;
        while((line= reader.readLine()) != null)
        {
            fileLines.add(line);
        }
        reader.close();
        for(; firstY != 0; firstY--)
        {
            fileLines.remove(0);
        }


        String[][] fileData= new String[fileLines.size()][];
        int x;
        for(int i= 0; i < fileLines.size(); i++)
        {
            x= firstX;
            String thisLine= fileLines.get(i);
            for(; x != 0; x--)
            {
                thisLine= thisLine.substring(thisLine.indexOf(",")+1);
            }
            fileData[i]= thisLine.split(",");
        }
        return fileData;
    }

    public String[] next()
    {
        if(line == in.length)
        {
            return null;
        }
        else
        {
            return in[line++];
        }
    }

    public int length()
    {
        return in.length;
    }

    public void skipLine() {
        next();
    }

    public String[][] getCsv()
    {
        return in;
    }

    public String getCell(int row, int col)
    {
        return in[row][col];
    }

    public String[] getRow(int row) {return in[row];}

    public String[] getColumn(int col)
    {
        String[] column= new String[in.length];
        int idx= 0;
        for(String[] arr : in)
        {
            column[idx++]= arr[col];
        }
        return column;
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

    public int getHeight()
    {
        return in.length;
    }

    public int getWidth()
    {
        return in[0].length;
    }

    public void removeColumn(int col)
    {
        String[][] newTable= new String[in.length][in[0].length-1];

        for(int i= 0; i < newTable.length; i++)
        {
            ArrayList<String> row= new ArrayList<>(Arrays.asList(in[i]));
            System.out.println("List: " + row.toString());
            row.remove(col);
            newTable[i]= row.toArray(new String[0]);
        }

        in= newTable;
    }

    public void removeRow(int row)
    {
        String[][] newTable= new String[in.length-1][];

        if(row != 0)
        {
            System.arraycopy(in,0,newTable,0,row);
            System.arraycopy(in,row+1,newTable,row,newTable.length-row);
        }
        else
        {
            System.arraycopy(in,1,newTable,0,newTable.length);
        }

        in= newTable;
    }

    public Table getTable()
    {
        return new Table("Csv",in);
    }

    public void print()
    {
        int[] columnWidths= new int[in.length];
        Arrays.fill(columnWidths,Integer.MIN_VALUE);

        for (String[] strings : in)
        {
            for (int y = 0; y < strings.length; y++)
            {
                if (columnWidths[y] < strings[y].length()) {
                    columnWidths[y]= strings[y].length();
                }
            }
        }

        for(int i= 0; i < columnWidths.length; i++)
        {
            columnWidths[i]= Math.min(columnWidths[i], 15);
        }

        StringBuilder line;
        for(int x= 0; x < in.length; x++)
        {
            line= new StringBuilder();
            for(int y= 0; y < in[x].length; y++)
            {
                line.append("|");
                if(in[x][y].length() <= 15)
                {
                    line.append(fixString(in[x][y],columnWidths[y]));
                }
                else
                {
                    line.append(fixString(in[x][y].substring(0,10) + "...",columnWidths[y]));
                }

                if(y == in[x].length-1)
                {
                    line.append("|");
                }
            }

            if(x == 0)
            {
                for(int i= 0; i < line.toString().length(); i++)
                {
                    if(line.toString().charAt(i) == '|')
                    {
                        System.out.print("+");
                    }
                    else
                    {
                        System.out.print("-");
                    }
                }
                System.out.println();
            }

            System.out.println(line.toString());
            
            for(int i= 0; i < line.toString().length(); i++)
            {
                if(line.toString().charAt(i) == '|')
                {
                    System.out.print("+");
                }
                else
                {
                    System.out.print("-");
                }
            }
            System.out.println();
        }
    }

    private String fixString(String str, int targetLength)
    {
        StringBuilder strBuilder = new StringBuilder(str);
        while(strBuilder.length() != targetLength){
            strBuilder.append(" ");
        }
        str = strBuilder.toString();
        return str;
    }

    public String toString()
    {
        StringBuilder str= new StringBuilder();
        for(String[] line : in)
        {
            str.append(Arrays.toString(line).substring(1, Arrays.toString(line).length() - 1)).append("\n");
        }
        return str.toString();
    }

    public boolean contains(ArrayList<String> list, String obj)
    {
        for(String str : list)
        {
            if(str.equals(obj))
                return true;
        }
        return false;
    }
}
