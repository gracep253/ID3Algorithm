import java.util.Arrays;

public class MethodTester
{
    public static void main(String[] args)
    {
        generateSortedDoubleOrder(0.20944271516477508, 0.11615809193446258, 0.2419726756283741);
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
            order[x]= indexOf(newArr, arr[x]);
        }

        System.out.println(Arrays.toString(order));
        return order;
    }

    private static boolean contains(int[] arr, int num)
    {
        for(int val : arr)
        {
            if(val == num)
                return true;
        }
        return false;
    }

    private static int indexOf(double[] arr, double num)
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
}
