package search;

public class BinarySearch {
    // считаю, что array[x] -> x >= 0

    // Pred: array.length >= 1 && x >= array[array.length - 1] && array is sorted
    // Post: ((R > 0 && x < array[R - 1]) || (R == 0)) &&  x >= array[R]
    private static int iterBinSearch(int x, int[] array) {
        if (array[0] <= x) {
            // 0 == 0 && x >= array[0]
            return 0;
            // R == 0 && x >= array[R]
        }
        // r > 0 && array[0] > x && array[array.length - 1] <= x
        int l = 0;
        // r > 0 && array[l] > x && array[array.length - 1] <= x
        int r = array.length - 1;
        // inv: r > 0 && array[l] > x && array[r] <= x
        while (l < r - 1) {  // (l < r) && ((l' < l && r' = r) || (l' = l && r' < r)) -> while is finite -> iterBinSearch is finite
            // r > 0 && array[l] > x && array[r] <= x
            int m  = l + (r - l) / 2;
            // r > 0 && array[l] > x && array[r] <= x
            if (array[m] <= x) {
                // r > 0 && array[l] > x && array[m] <= x
                r = m;
                // r > 0 && array[l] > x && array[r] <= x
            } else {
                // r > 0 && array[m] > x && array[r] <= x
                l = m;
                // r > 0 && array[l] > x && array[r] <= x
            }
            // r > 0 && array[l] > x && array[r] <= x
        }
        // r > 0 && array[l] > x && array[r] <= x && array is sorted && l >= r - 1 -> l = r - 1
        // r > 0 && x < array[l] && x >= array[r]
        // r > 0 && x < array[r - 1] && x >= array[r]
        return r;
        // R > 0 && x < array[R - 1] && x >= array[R]
    }

    // Pred: array.length >= 1 (l == 0 || array[l] > x) && array[r] <= x && array is sorted
    // Post: ((R > 0 && x < array[R - 1]) || (R == 0)) &&  x >= array[R]
     private static int recBinSearch(int x, int l, int r, int[] array) {
        // array.length >= 1 (l' == 0 || array[l'] > x) && array[r'] <= x && array is sorted
        if (array[0] <= x) {
            // (0 == 0) && x >= array[0]
            return 0;
            // (R == 0) && x >= array[R]
        }
        // array.length >= 1 array[l'] > x && array[r'] <= x && array is sorted
        int m = l + (r - l) / 2;
        // array.length >= 1 array[l'] > x && array[r'] <= x && array is sorted
        if (array[m] <= x) {
            // array.length >= 1 array[l'] > x && array[m] <= x && array is sorted
            r = m;
            // array.length >= 1 array[l'] > x && array[r'] <= x && array is sorted
        } else {
            // array.length >= 1 array[m] > x && array[r'] <= x && array is sorted
            l = m;
            // array.length >= 1 array[l'] > x && array[r'] <= x && array is sorted
        }
        // array.length >= 1 array[l'] > x && array[r'] <= x && array is sorted
        if (l >= r - 1) {
            // r' > 0 && array[l'] > x && array[r'] <= x && array is sorted && l' >= r' - 1 -> l' = r' - 1
            // r' > 0 && x < array[l'] && x >= array[r']
            // r' > 0 && x < array[r' - 1] && x >= array[r']
            return r;
            // R > 0 && x < array[R - 1] && x >= array[R]
        }
        // array.length >= 1 array[l'] > x && array[r'] <= x && array is sorted
        // ((recBinSearch(int x, int l', int r', int[] array) > 0 && x < array[recBinSearch(int x, int l', int r', int[] array) - 1])
        //      || (recBinSearch(int x, int l', int r', int[] array) == 0)) &&  x >= array[recBinSearch(int x, int l', int r', int[] array)]
        return recBinSearch(x, l, r, array); // (l < r) && ((l' < l && r' = r) || (l' = l && r' < r)) -> recursion is finite -> recBinSearch is finite
        // ((R > 0 && x < array[R - 1]) || (R == 0)) &&  x >= array[R]
    }

    // Pred: args[0] ∈ ℤ && ∀ x : (x > 0 && x + 1 < args.length) args[x] ∈ ℤ && args[x] >= args[x + 1]
    // Post: let array = {args[1], args[2], ... , args[args.length - 1]}
    //      let x = args[0]
    //  ((R > 0 && x < array[R - 1]) || (R == 0)) &&  x >= array[R]
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int[] array = new int[args.length];
        array[args.length - 1] = Integer.MIN_VALUE;
        for (int ind = 1; ind < args.length; ind++) {
            array[ind - 1] = Integer.parseInt(args[ind]);
        }
        // Integer.MIN_VALUE <= args[args.length - 2] -> array = args[1], args[2], ... , args[length - 1], Integer.MIN_VALUE -> array is sorted

        // array.length >= 1 && x >= array[array.length - 1] && array is sorted
        int result = iterBinSearch(x, array);
        // ((result > 0 && x < array[result - 1]) || (result == 0)) &&  x >= array[result]
        System.out.println(result);
        // ((R > 0 && x < array[R - 1]) || (R == 0)) &&  x >= array[R]
    }
}
