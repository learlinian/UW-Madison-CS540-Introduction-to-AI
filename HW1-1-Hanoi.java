// Author: Nian Li

import java.util.*;

public class Hanoi {

    public static List<List> getSuccessor(String[] hanoi) {
        // TO DO
        // Implement the getSuccessor method
        List<List> result = new ArrayList<>();
        String j_rep;
        String i_rep;
        int i;  // starting rod
        int j;  // target rod
        int j_max;

        for (i=0; i<hanoi.length; ++i){
            char top_disk = hanoi[i].charAt(0); // get the top disk value in current rod

            // continue for loop if there is no disk starting rod
            if (top_disk == '0'){
                continue;
            }

            // determine possible moving unit(s)
            // starting rod is first rod
            if (i == 0){
                j = 1;
                j_max = 1;
            }
            // starting rod is(are) middle rod(s)
            else if (i == hanoi.length-1){
                j_max = i;
                j = i - 1;
            }
            // starting rod is last rod
            else{
                j = i - 1;
                j_max = i + 1;
            }

            for (; j<=j_max; j++){
                List<String> solution = new ArrayList<>();
                if (top_disk < hanoi[j].charAt(0) || hanoi[j].charAt(0) == '0'){

                    // no disk on target rod
                    if (hanoi[j].charAt(0) == '0'){
                        j_rep = Character.toString(top_disk);
                    }
                    // disk on target rod
                    else{
                        j_rep = top_disk + hanoi[j];
                    }

                    // only 1 disk on starting rod
                    if (hanoi[i].length() == 1){
                        i_rep = "0";
                    }
                    // more than 1 disks on starting rod
                    else{
                        i_rep = hanoi[i].substring(1);
                    }

                    // copy hanoi and replace disk numbers on starting and result rod
                    for (String element : hanoi){
                        solution.add(element);
                    }
                    solution.set(i, i_rep);
                    solution.set(j, j_rep);

                    // add possible solution to result
                    result.add(solution);
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Please input at least 3 strings");
            return;
        }

        List<List> sucessors = getSuccessor(args);
        for (List item : sucessors) {
            for (Object disk : item) {
                System.out.print(disk + " ");
            }
            System.out.print("\n");
        }
    }
}
