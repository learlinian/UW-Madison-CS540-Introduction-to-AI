import java.util.*;

public class WaterJug {
    public static void main(String args[]) {
        if (args.length != 6) {
            System.out.println("Invalid Number of Input Arguments");
            return;
        }
        int flag = Integer.valueOf(args[0]);
        int cap_jug1 = Integer.valueOf(args[1]);
        int cap_jug2 = Integer.valueOf(args[2]);
        int curr_jug1 = Integer.valueOf(args[3]);
        int curr_jug2 = Integer.valueOf(args[4]);
        int goal = Integer.valueOf(args[5]);

        int option = flag / 100;
        int depth = flag % 100;

        if (option < 1 || option > 5) {
            System.out.println("Invalid flag input");
            return;
        }
        if (cap_jug1 > 9 || cap_jug2 > 9 || curr_jug1 > 9 || curr_jug2 > 9) {
            System.out.println("Invalid input: 2-digit jug volumes");
            return;
        }
        if (cap_jug1 < 0 || cap_jug2 < 0 || curr_jug1 < 0 || curr_jug2 < 0) {
            System.out.println("Invalid input: negative jug volumes");
            return;
        }
        if (cap_jug1 < curr_jug1 || cap_jug2 < curr_jug2) {
            System.out.println("Invalid input: jug volume exceeds its capacity");
            return;
        }
        State init = new State(cap_jug1, cap_jug2, curr_jug1, curr_jug2, goal, 0);
        init.printState(option, depth);
    }
}

class State {
    int cap_jug1;
    int cap_jug2;
    int curr_jug1;
    int curr_jug2;
    int goal;
    int depth;
    State parentPt;

    public State(int cap_jug1, int cap_jug2, int curr_jug1, int curr_jug2, int goal, int depth) {
        this.cap_jug1 = cap_jug1;
        this.cap_jug2 = cap_jug2;
        this.curr_jug1 = curr_jug1;
        this.curr_jug2 = curr_jug2;
        this.goal = goal;
        this.depth = depth;
        this.parentPt = null;
    }

    public State[] getSuccessors() {
        // TO DO: get all successors and return them in proper order
        int index = 0;  // record successor number
        State[] successors = new State[6];  // maximum 6 successors

        // empty the jug1 (e1)
        if(this.curr_jug1 > 0){
            successors[index] = new State(cap_jug1, cap_jug2, 0, curr_jug2, goal, depth + 1);
            index++;
        }

        // pour water from jug1 to jug2 (p12)
        if(this.curr_jug2 < this.cap_jug2 && this.curr_jug1 > 0){
            if(this.curr_jug2 + this.curr_jug1 < this.cap_jug2) {
                successors[index] = new State(cap_jug1, cap_jug2, 0, curr_jug1 + curr_jug2, goal, depth + 1);
            }
            else{
                successors[index] = new State(cap_jug1, cap_jug2, curr_jug1+curr_jug2-cap_jug2, cap_jug2, goal, depth + 1);
            }
            index++;
        }

        // empty the jug2 (e2)
        if(this.curr_jug2 >0){
            successors[index] = new State(cap_jug1, cap_jug2, curr_jug1, 0, goal, depth + 1);
            index++;
        }

        // fill the jug2 (f2)
        if(this.curr_jug2 < this.cap_jug2){
            successors[index] = new State(cap_jug1, cap_jug2, curr_jug1, cap_jug2, goal, depth + 1);
            index++;
        }

        // pour water from jug2 to jug1 (p21)
        if(this.curr_jug1 < this.cap_jug1 && this.curr_jug2 > 0){
            if(this.curr_jug2 + this.curr_jug1 < this.cap_jug1) {
                successors[index] = new State(cap_jug1, cap_jug2, curr_jug1 + curr_jug2, 0, goal, depth + 1);
            }
            else{
                successors[index] = new State(cap_jug1, cap_jug2, cap_jug1, curr_jug1+curr_jug2-cap_jug1, goal, depth + 1);
            }
            index++;
        }

        // fill the jug1 (f1)
        if(this.curr_jug1 < this.cap_jug1){
            successors[index] = new State(cap_jug1, cap_jug2, cap_jug1, curr_jug2, goal, depth + 1);
        }

        return successors;
    }


    public void printState(int option, int depth) {

        // TO DO: print a State based on option (flag)
        State[] result = getSuccessors();
        if (option == 1){
            for(State item : result){
                if(item != null){
                    String state = item.curr_jug1 + Integer.toString(item.curr_jug2);
                    System.out.println(state);
                }
            }
        }

        else if (option == 2){
            for(State item : result){
                if(item != null){
                    String state = item.curr_jug1 + Integer.toString(item.curr_jug2);
                    if(item.curr_jug1 == item.goal || item.curr_jug2 == item.goal) {
                        System.out.println(state + " true");
                    }
                    else{
                        System.out.println(state + " false");
                    }
                }
            }
        }

        else if (option == 3){
            UninformedSearch.bfs(this);
        }

        else if (option == 4){
            UninformedSearch.dfs(this);
        }

        else if (option == 5){
            UninformedSearch.iddfs(this, depth);
        }
    }

}

class UninformedSearch {
    static void bfs(State curr_state) {
        // TO DO: run breadth-first search algorithm

        // First to find BFS sequence
        // If the initial state is our goal
        ArrayList<String> OPENED = new ArrayList<>();
        ArrayList<String> CLOSED = new ArrayList<>();
        String initial_state = curr_state.curr_jug1 + Integer.toString(curr_state.curr_jug2);

        if(curr_state.curr_jug1 != curr_state.goal && curr_state.curr_jug2 != curr_state.goal) {
            System.out.println(curr_state.curr_jug1 + Integer.toString(curr_state.curr_jug2));
            do {
                CLOSED.add(curr_state.curr_jug1 + Integer.toString(curr_state.curr_jug2));

                State[] successors = curr_state.getSuccessors();
                // Append successors to list OPENED
                for (State item : successors) {
                    if (item != null && !CLOSED.contains(item.curr_jug1 + Integer.toString(item.curr_jug2))
                            && !OPENED.contains(item.curr_jug1 + Integer.toString(item.curr_jug2))) {
                        OPENED.add(item.curr_jug1 + Integer.toString(item.curr_jug2));
                    }
                }

                System.out.print(curr_state.curr_jug1 + Integer.toString(curr_state.curr_jug2) + " ");
                System.out.print(OPENED);
                System.out.print(" ");
                System.out.println(CLOSED);

                // break out the loop if there is no item in OPENED list
                if(OPENED.isEmpty()){
                    break;
                }

                // append visited node to list CLOSED
                if (!CLOSED.contains(curr_state.curr_jug1 + Integer.toString(curr_state.curr_jug2))) {
                    CLOSED.add(curr_state.curr_jug1 + Integer.toString(curr_state.curr_jug2));
                }

                char jug1 = OPENED.get(0).charAt(0);
                char jug2 = OPENED.get(0).charAt(1);

                OPENED.remove(0);   // remove the first item in OPENED

                // change the current state
                curr_state = new State(curr_state.cap_jug1, curr_state.cap_jug2, Integer.parseInt(String.valueOf(jug1)),
                        Integer.parseInt(String.valueOf(jug2)), curr_state.goal, curr_state.depth);
            }
            while (curr_state.curr_jug1 != curr_state.goal && curr_state.curr_jug2 != curr_state.goal);
        }

        // print out the goal and path if the current state satisfies the requirement
        if(curr_state.curr_jug1 == curr_state.goal || curr_state.curr_jug2 == curr_state.goal) {
            System.out.print(curr_state.curr_jug1 + Integer.toString(curr_state.curr_jug2));
            System.out.println(" Goal");


            // print the path
            System.out.print("Path");

            ArrayList<String> path = new ArrayList<>();     // define path
            path.add(curr_state.curr_jug1 + Integer.toString(curr_state.curr_jug2));    // add the goal state
            String findingState = curr_state.curr_jug1 + Integer.toString(curr_state.curr_jug2);

            while(!path.get(path.size()-1).equals(initial_state)) {
                for (String state : CLOSED) {
                    State test_state = new State(curr_state.cap_jug1, curr_state.cap_jug2,
                            Character.getNumericValue(state.charAt(0)), Character.getNumericValue(state.charAt(1)), curr_state.goal, curr_state.depth);
                    State[] children = test_state.getSuccessors();
                    ArrayList<String> children_state = new ArrayList<>();
                    for(State child : children){
                        if(child != null) {
                            children_state.add(child.curr_jug1 + Integer.toString(child.curr_jug2));
                        }
                    }
                    if(children_state.contains(findingState)){
                        path.add(state);
                        findingState = state;
                        break;
                    }
                }
            }
            Collections.reverse(path);
            for(String path_state : path) {
                System.out.print(" " + path_state);
            }
        }
    }

    static void dfs(State curr_state) {
        // TO DO: run depth-first search algorithm

        ArrayList<String> OPENED = new ArrayList<>();
        ArrayList<String> CLOSED = new ArrayList<>();
        ArrayList<State>  OPENEDSTATE = new ArrayList<>();
        ArrayList<State>  CLOSEEDSTATE = new ArrayList<>();

        String initial_state = curr_state.curr_jug1 + Integer.toString(curr_state.curr_jug2);

        if(curr_state.curr_jug1 != curr_state.goal && curr_state.curr_jug2 != curr_state.goal) {
            System.out.println(curr_state.curr_jug1 + Integer.toString(curr_state.curr_jug2));
            do {
                CLOSED.add(curr_state.curr_jug1 + Integer.toString(curr_state.curr_jug2));
                CLOSEEDSTATE.add(curr_state);

                State[] successors = curr_state.getSuccessors();
                // Append successors to list OPENED
                for (State item : successors) {
                    if (item != null && !CLOSED.contains(item.curr_jug1 + Integer.toString(item.curr_jug2))
                            && !OPENED.contains(item.curr_jug1 + Integer.toString(item.curr_jug2))) {
                        OPENED.add(item.curr_jug1 + Integer.toString(item.curr_jug2));
                        OPENEDSTATE.add(item);
                    }
                }

                System.out.print(curr_state.curr_jug1 + Integer.toString(curr_state.curr_jug2) + " ");
                System.out.print(OPENED);
                System.out.print(" ");
                System.out.println(CLOSED);

                // break out the loop if there is no item in OPENED list
                if(OPENED.isEmpty()){
                    break;
                }

                // append visited node to list CLOSED
                if (!CLOSED.contains(curr_state.curr_jug1 + Integer.toString(curr_state.curr_jug2))) {
                    CLOSED.add(curr_state.curr_jug1 + Integer.toString(curr_state.curr_jug2));
                    CLOSEEDSTATE.add(curr_state);
                }

                int len = OPENED.size() - 1;
                curr_state = OPENEDSTATE.get(len);
                OPENED.remove(len);      // remove the last state item in OPENED
                OPENEDSTATE.remove(len); // remove the last State item in OPENED

            }
            while (curr_state.curr_jug1 != curr_state.goal && curr_state.curr_jug2 != curr_state.goal);
        }

        if(curr_state.curr_jug1 == curr_state.goal || curr_state.curr_jug2 == curr_state.goal) {
            System.out.print(curr_state.curr_jug1 + Integer.toString(curr_state.curr_jug2));
            System.out.println(" Goal");

            // print the path
            System.out.print("Path");

            ArrayList<String> path = new ArrayList<>();     // define path
            path.add(curr_state.curr_jug1 + Integer.toString(curr_state.curr_jug2));    // add the goal state
            String findingState = curr_state.curr_jug1 + Integer.toString(curr_state.curr_jug2);
            int findingStateDepth = curr_state.depth;
            int CLOSED_len = CLOSED.size() - 1;
            while(!path.get(path.size()-1).equals(initial_state)) {
                for (int k = CLOSED_len; k >= 0; k--) {
                    String state = CLOSED.get(k);
                    State test_state = new State(curr_state.cap_jug1, curr_state.cap_jug2,
                            Character.getNumericValue(state.charAt(0)), Character.getNumericValue(state.charAt(1)), curr_state.goal, curr_state.depth);
                    State[] children = test_state.getSuccessors();
                    ArrayList<String> children_state = new ArrayList<>();
                    for(State child : children){
                        if(child != null) {
                            children_state.add(child.curr_jug1 + Integer.toString(child.curr_jug2));
                        }
                    }
                    CLOSED_len = k - 1; // no latter node is allowed
                    if(children_state.contains(findingState) && CLOSEEDSTATE.get(k).depth+1 == findingStateDepth){
                        path.add(state);
                        findingState = state;
                        findingStateDepth--;
                        break;
                    }
                }
            }
            Collections.reverse(path);
            for(String path_state : path) {
                System.out.print(" " + path_state);
            }
        }
    }

    static void iddfs(State curr_state, int depth) {
        // TO DO: run IDDFS search algorithm

        ArrayList<String> OPENED = new ArrayList<>();
        ArrayList<String> CLOSED = new ArrayList<>();
        ArrayList<State>  OPENEDSTATE = new ArrayList<>();
        ArrayList<State>  CLOSEEDSTATE = new ArrayList<>();

        String initial_state = curr_state.curr_jug1 + Integer.toString(curr_state.curr_jug2);
        int max_depth = 0;  // record the maximum depth, initialize as 0
        if(curr_state.curr_jug1 != curr_state.goal && curr_state.curr_jug2 != curr_state.goal) {
            while (curr_state.curr_jug1 != curr_state.goal && curr_state.curr_jug2 != curr_state.goal && max_depth <= depth) {
                OPENED.clear();
                CLOSED.clear();
                OPENEDSTATE.clear();
                CLOSEEDSTATE.clear();
                curr_state = new State(curr_state.cap_jug1, curr_state.cap_jug2, Integer.parseInt(String.valueOf(initial_state.charAt(0))),
                        Integer.parseInt(String.valueOf(initial_state.charAt(1))), curr_state.goal, 0);
                System.out.println(max_depth + ":" + curr_state.curr_jug1 + curr_state.curr_jug2 + " ");
                while (curr_state.depth <= max_depth) {
                    CLOSED.add(curr_state.curr_jug1 + Integer.toString(curr_state.curr_jug2));
                    CLOSEEDSTATE.add(curr_state);
                    State[] successors = curr_state.getSuccessors();

                    // Append successors to list OPENED
                    for (State item : successors) {
                        if (item != null && !CLOSED.contains(item.curr_jug1 + Integer.toString(item.curr_jug2))
                                && !OPENED.contains(item.curr_jug1 + Integer.toString(item.curr_jug2)) && item.depth <= max_depth) {
                            OPENED.add(item.curr_jug1 + Integer.toString(item.curr_jug2));
                            OPENEDSTATE.add(item);
                        }
                    }

                    // break out the loop if there is no item in OPENED list
                    if (OPENED.isEmpty()) {
                        break;
                    }

                    System.out.print(max_depth + ":" + curr_state.curr_jug1 + curr_state.curr_jug2 + " ");
                    System.out.print(OPENED);
                    System.out.print(" ");
                    System.out.println(CLOSED);

                    // append visited node to list CLOSED
                    if (!CLOSED.contains(curr_state.curr_jug1 + Integer.toString(curr_state.curr_jug2))) {
                        CLOSED.add(curr_state.curr_jug1 + Integer.toString(curr_state.curr_jug2));
                        CLOSEEDSTATE.add(curr_state);
                    }

                    int len = OPENED.size() - 1;

                    // change the current state
                    curr_state = OPENEDSTATE.get(len);
                    OPENED.remove(len);      // remove the last state item in OPENED
                    OPENEDSTATE.remove(len); // remove the last State item in OPENED
                }
                max_depth++;
            }
        }

        if(curr_state.curr_jug1 == curr_state.goal || curr_state.curr_jug2 == curr_state.goal) {
            System.out.print(curr_state.depth + ":" + curr_state.curr_jug1 + curr_state.curr_jug2);
            System.out.println(" Goal");

            // print the path
            System.out.print("Path");

            ArrayList<String> path = new ArrayList<>();     // define path
            path.add(curr_state.curr_jug1 + Integer.toString(curr_state.curr_jug2));    // add the goal state
            String findingState = curr_state.curr_jug1 + Integer.toString(curr_state.curr_jug2);
            int findingStateDepth = curr_state.depth;

            int CLOSED_len = CLOSED.size() - 1;
            while(!path.get(path.size()-1).equals(initial_state)) {
                for (int k = CLOSED_len; k >= 0; k--) {
                    String state = CLOSED.get(k);
                    State test_state = new State(curr_state.cap_jug1, curr_state.cap_jug2,
                            Character.getNumericValue(state.charAt(0)), Character.getNumericValue(state.charAt(1)), curr_state.goal, curr_state.depth);
                    State[] children = test_state.getSuccessors();
                    ArrayList<String> children_state = new ArrayList<>();
                    for(State child : children){
                        if(child != null) {
                            children_state.add(child.curr_jug1 + Integer.toString(child.curr_jug2));
                        }
                    }
                    CLOSED_len = k - 1; // no latter node is allowed
                    if(children_state.contains(findingState) && CLOSEEDSTATE.get(k).depth+1 == findingStateDepth){
                        path.add(state);
                        findingState = state;
                        findingStateDepth--;
                        break;
                    }
                }
            }

            Collections.reverse(path);
            for(String path_state : path) {
                System.out.print(" " + path_state);
            }
        }
    }
}
