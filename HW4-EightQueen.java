// Author: Nian Li

import java.util.*;
import java.lang.Math;

public class EightQueen {
    public static void main(String args[]) {
        if (args.length != 2 && args.length != 3) {
            System.out.println("Invalid Number of Input Arguments");
            return;
        }

        int flag = Integer.valueOf(args[0]);
        int option = flag / 100;
        int iteration = flag % 100;
        char[] board = new char[8];
        int seed = -1;
        int board_index = -1;

        if (args.length == 2 && (option == 1 || option == 2 || option == 4)) {
            board_index = 1;
        } else if (args.length == 3 && (option == 3 || option == 5)) {
            seed = Integer.valueOf(args[1]);
            board_index = 2;
        } else {
            System.out.println("Invalid Number of Input Arguments");
            return;
        }

        if (board_index == -1) return;
        for (int i = 0; i < 8; i++) {
            board[i] = args[board_index].charAt(i);
            int pos = board[i] - '0';
            if (pos < 0 || pos > 7) {
                System.out.println("Invalid input: queen position(s)");
                return;
            }
        }

        State init = new State(board);
        init.printState(option, iteration, seed);
    }
}

class State {
    char[] board;

    public State(char[] arr) {
        this.board = Arrays.copyOf(arr, arr.length);
    }

    public void printState(int option, int iteration, int seed) {

        if(option == 1){
            int result = findState(this.board);
            System.out.println(result);
        }

        else if(option == 2){
            int current_state = findState(this.board);
            if(current_state == 0) return;
            ArrayList<char[]> optimal_states = listState(this.board);
            for(char[] item : optimal_states)   System.out.println(item);
            System.out.println(findState(optimal_states.get(0)));
        }

        else if(option == 3){
            Random rng = new Random();
            if (seed != -1) rng.setSeed(seed);
            char[] temp = Arrays.copyOf(this.board, this.board.length);
            int count = 0;
            System.out.print("0:");
            for(int i = 0; i<8; i++) System.out.print(temp[i]);
            System.out.print(" " + findState(temp) + "\n");
            while(findState(temp) != 0 && count < iteration) {
                ArrayList<char[]> optimal_states = listState(temp);
                try{
                    int r = rng.nextInt(optimal_states.size());
                    char[] move = optimal_states.get(r);
                    temp = move;
                    count++;
                    System.out.print(count + ":");
                    for(int i = 0; i<8; i++) System.out.print(temp[i]);
                    System.out.print(" " + findState(temp) + "\n");
                }
                catch(Exception e){
                    return;
                }
            }
            if(findState(temp) == 0) System.out.println("Solved");
        }

        else if(option == 4){
            int count = 1;
            char[] temp = Arrays.copyOf(this.board, this.board.length);
            System.out.print("0:");
            for(int i = 0; i<8; i++) System.out.print(temp[i]);
            System.out.print(" " + findState(temp) + "\n");
            while(findState(temp) != 0 && count <= iteration){
                temp = betterMove(temp);
                if(temp == null){
                    System.out.println("Local optimum");
                    return;
                }
                System.out.print(count + ":");
                for(int i = 0; i<8; i++) System.out.print(temp[i]);
                System.out.print(" " + findState(temp) + "\n");
                count++;
            }
            if(findState(temp) == 0) System.out.println("Solved");
        }

        else if(option == 5){
            Random rng = new Random();
            if (seed != -1) rng.setSeed(seed);
            char[] temp = Arrays.copyOf(this.board, this.board.length);
            int count = 0;
            System.out.print("0:");
            for(int i = 0; i<8; i++) System.out.print(temp[i]);
            System.out.print(" " + findState(temp) + "\n");
            while(findState(temp) != 0 && count < iteration) {
                ArrayList<char[]> optimal_states = listState(temp);
                try{
                    int index = rng.nextInt(7);
                    int value = rng.nextInt(7);
                    double prob = rng.nextDouble();
                    if(prob == 0) continue;
                    temp[index] = Character.forDigit(value, 10);
                    count++;
                    System.out.print(count + ":");
                    for(int i = 0; i<8; i++) System.out.print(temp[i]);
                    System.out.print(" " + findState(temp) + "\n");
                }
                catch(IllegalArgumentException e){
                    return;
                }
            }
            if(findState(temp) == 0) System.out.println("Solved");
        }
    }

    // find the state of current board
    static int findState(char[] current_board){
        int stateNum = 0;
        for(int i=0; i<8; i++){
            int check_line = 1;     // check diagonal queens, increase by 1 after each column
            for(int j=i+1; j<8; j++){
                if(Math.abs(current_board[i]-current_board[j]) == check_line || current_board[i] == current_board[j])stateNum++;
                check_line++;
            }
        }
        return stateNum;
    }

    // find the optimal char array of current board
    static ArrayList<char[]> listState(char[] current_board){
        int current_state = findState(current_board);
        ArrayList<char[]> optimal_states = new ArrayList<>();
        for(int i=0; i<8; i++){     // check 8 different columns
            for(int j=0; j<8; j++)  // switch queen in each column to different row except the row it holds itself
            {
                char[] temp = Arrays.copyOf(current_board, current_board.length);
                if(j == current_board[i] - '0') continue;
                temp[i] = Character.forDigit(j, 10);
                int stateTest = findState(temp);
                if(stateTest < current_state){
                    current_state = stateTest;
                    optimal_states.clear();
                    optimal_states.add(temp);
                }
                else if(stateTest == current_state)optimal_states.add(temp);
            }
        }
        return optimal_states;
    }

    // find better move
    static char[] betterMove(char[] current_board){
        int current_state = findState(current_board);
        for(int i=0; i<8; i++){     // check 8 different columns
            for(int j=0; j<8; j++)  // switch queen in each column to different row except the row it holds itself
            {
                char[] temp = Arrays.copyOf(current_board, current_board.length);
                if(j == temp[i] - '0') continue;
                temp[i] = Character.forDigit(j, 10);
                int stateTest = findState(temp);
                if(stateTest < current_state)  return temp;
            }
        }
        return null;
    }
}
