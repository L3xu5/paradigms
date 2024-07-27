package search;

import static java.lang.Math.*;

public class BinarySearchClosestI {

    // Pred : r >= 0 && r < array.length && array is sorted
    // Post : R <= r && (R == 0 || x > array[R - 1]) && (x <= array[R] || R == array.length - 1)
    private static int iterBisectLeft(int x, int r, int[] array) {
        if (array[0] >= x) {
            // x <= array[0]
            return 0;
            // R <= r && R == 0 && x <= array[R]
        }
        // array[0] < x -> r' != 0
        // array[0] < x <= array[r']
        int l = 0;
        // inv: array[l] < x && (x <= array[r'] || r' == array.length - 1)
        while (l < r - 1) {  // ((l' = (l + r) / 2 && r' = r) || (l' = l && r' = (l + r) / 2)) -> while is finite && O(log n)
                // -> iterBisectLeft is finite && O(log n)
            int m = l + (r - l) / 2;
            if (array[m] < x) {
                // array[m] < x && (x <= array[r'] || r == array.length - 1)
                l = m;
                // array[l] < x && (x <= array[r'] || r' == array.length - 1)
            } else {
                // array[l] < x && (x <= array[m] || m == array.length - 1)
                r = m;
                // array[l] < x && (x <= array[r'] || r' == array.length - 1)
            }
            // array[l] < x && (x <= array[r'] || r' == array.length - 1)
        }
        // r' <= r &&  x > array[r' - 1] && (x <= array[r'] || r' == array.length - 1)
        return r;
        // R <= r && (R == 0 || x > array[R - 1]) && (x <= array[R] || R == array.length - 1)
    }

    // Pred : (r == 0 || x > array[l]) && (x <= array[r] || r == array.length - 1)
    // Post : R <= r && (R == 0 || x > array[R - 1]) && (x <= array[R] || R == array.length - 1)
    private static int recBisectLeft(int x, int l, int r, int[] array) {
        if (array[0] >= x) {
            // 0 <= r && x <= array[0]
            return 0;
            // R <= r && (R == 0 || x > array[R - 1]) && (x <= array[R] || R == array.length - 1)
        }
        // array[0] < x -> r' != 0
        // array[0] < x <= array[r']

        int m = l + (r - l) / 2;
        if (array[m] < x) {
            // x > array[m] && (x <= array[r'] || r' == array.length - 1)
            l = m;
            // x > array[l'] && (x <= array[r'] || r' == array.length - 1)
        } else {
            // x > array[l'] && (x <= array[m] || m == array.length - 1)
            r = m;
            // x > array[l'] && (x <= array[r'] || r' == array.length - 1)
        }
        // x > array[l'] && (x <= array[r'] || r' == array.length - 1)
        if (l >= r - 1) {
            // r' <= r && (r' == 0 || x > array[r' - 1]) && (x <= array[r'] || r' == array.length - 1)
            return r;
            // R <= r && (R == 0 || x > array[R - 1]) && (x <= array[R] || R == array.length - 1)
        }

        // (r' == 0 || x > array[l']) && (x <= array[r'] || r' == array.length - 1)
        return recBisectLeft(x, l, r, array);  // ((l' = (l + r) / 2 && r' = r) || (l' = l && r' = (l + r) / 2)) -> recursion is finite && && O(log n)
            // -> recBisectLeft is finite && && O(log n)
        // R <= r && (R == 0 || x > array[R - 1]) && (x <= array[R] || R == array.length - 1)
    }


    // Pred: args[0] ∈ ℤ && ∀ x : (x > 0 && x + 1 < args.length) args[x] ∈ ℤ && args[x] <= args[x + 1] && array in not empty
    // Post: let array = {args[1], args[2], ... , args[args.length - 1]}
    //      let x = args[0]
    //      R >= 0 && R <= array.length - 1
    //          && (R == 0 || abs(array[R - 1] - x) > abs(array[R] - x))
    //          && (R == array.length - 1 || abs(array[R] - x) <= abs(array[R + 1] - x))
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int[] array = new int[args.length - 1];
        for (int ind = 1; ind < args.length; ind++) {
            array[ind - 1] = Integer.parseInt(args[ind]);
        }
        if (array[0] >= x || array.length == 1) {
            // array[0] >= x && array is sorted -> abs(array[1] - x) > abs(array[0] - x)
            // (0 == array.length - 1 || abs(array[0] - x) <= abs(array[1] - x))
            System.out.println("0");
            return;
            // R >= 0 && R <= array.length - 1
            //      && (R == 0 || abs(array[R - 1] - x) > abs(array[R] - x))
            //      && (R == array.length - 1 || abs(array[R] - x) <= abs(array[R + 1] - x))
        }
        // array[0] < x && array.length > 1

        // array.length - 1 < array.length && array is sorted
        int r = array.length - 1;
        // r >= 0 && r < array.length && array is sorted
        r = iterBisectLeft(x, r, array);
        // r' <= r && (r' == 0 || x > array[r' - 1]) && (x <= array[r'] || r' == array.length - 1)

        if (abs((long) x - array[r - 1]) > abs((long) x - array[r])) {
            // r' <= r && x > array[r' - 1] && (x <= array[r'] || r' == array.length - 1) &&  abs(x - array[r' - 1]) > abs(x - array[r']) && array is sorted
            //           -> abs(array[r' - 1] - x) > abs(array[r'] - x)
            //            && (r' == array.length - 1 || abs(array[r'] - x) <= abs(array[r' + 1] - x))
            System.out.println(r);
            return;
            // R >= 0 && R <= array.length - 1
            //      && (R == 0 || abs(array[R - 1] - x) > abs(array[R] - x))
            //      && (R == array.length - 1 || abs(array[R] - x) <= abs(array[R + 1] - x))
        }
        // abs(array[r' - 1] - x) <= abs(array[r'] - x)
        r--;
        // abs(array[r'] - x) <= abs(array[r' + 1] - x)

        // r' >=0 && r' < array.length && array is sorted
        r = iterBisectLeft(array[r], r, array);
        // r'' <= r' && (r'' == 0 || array[r'] > array[r'' - 1]) && (array[r'] <= array[r''] || r'' == array.length - 1)
        //      -> array[r'' - 1] < array[r'] == array[r'']
        //      -> (r'' == 0 || abs(array[r'' - 1] - x) > abs(array[r''] - x))
        //           && abs(array[r''] - x) <= abs(array[r'' + 1] - x)

        System.out.println(r);
        // R >= 0 && R <= array.length - 1
        //      && (R == 0 || abs(array[R - 1] - x) > abs(array[R] - x))
        //      && (R == array.length - 1 || abs(array[R] - x) <= abs(array[R + 1] - x))
    }
}
