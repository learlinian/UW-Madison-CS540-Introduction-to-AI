// Author: Nian Li

import java.util.*;

public class Number{

    public static int getStep(int x, int y) {
        // TO DO
        // Implement the getStep method
        int index = 0;  // index to count loop numbers
        List<Integer> result = new ArrayList<>();
        result.add(x);

        while (!result.contains(y)) {
            for (int i=0; i<Math.pow(4, index); i++){
                int parent = result.get(0);
                // add children in 4 different ways
                result.add(parent - 1);
                result.add(parent + 1);
                result.add(parent * 3);
                result.add(parent * parent);
                result.remove(0);   // dequeue the first element in list
            }
            index++;
        }

        return index;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Please have 2 inputs only");
            return;
        }

        System.out.println(getStep(Integer.parseInt(args[0]), Integer.parseInt(args[1])));

    }
}
