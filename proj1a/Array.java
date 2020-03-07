public class Array {

    public static int[] insert(int[] arr, int item, int position) {
        int[] result = new int[arr.length + 1];
        for (int i = 0; i < position; i++) {
            result[i] = arr[i];
        }
        result[position] = item;
        for (int i = position; i < arr.length; i++) {
            result[i + 1] = arr[i];
        }
        return result;
    }

    public static void reverse(int[] arr) {
        for (int i = 0; i < arr.length / 2; i++) {
            int tmp = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = arr[i];
            arr[i] = tmp;
        }
    }

    public static int[] replicate(int[] arr) {
        int total = 0;
        for (int i=0; i<arr.length;i++) {
            total = arr[i] + total;
        }
        int[] result = new int[total];
        int counter = 0;
        for (int item : arr) {
            for (int i = 0;i < item; i ++) {
                result[counter] = item;
                counter ++;
            }
        }
        return result;

        }
}