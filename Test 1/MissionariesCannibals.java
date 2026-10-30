import java.util.*;

public class MissionariesCannibals {
    static final int PEOPLE = 3;

    static class State {
        int missionariesLeft;
        int cannibalsLeft;
        int boatSide; // 0 = left, 1 = right
        State parent;
        String action;

        State(int missionariesLeft, int cannibalsLeft, int boatSide, State parent, String action) {
            this.missionariesLeft = missionariesLeft;
            this.cannibalsLeft = cannibalsLeft;
            this.boatSide = boatSide;
            this.parent = parent;
            this.action = action;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            State other = (State) obj;
            return missionariesLeft == other.missionariesLeft
                    && cannibalsLeft == other.cannibalsLeft
                    && boatSide == other.boatSide;
        }

        @Override
        public int hashCode() {
            return Objects.hash(missionariesLeft, cannibalsLeft, boatSide);
        }
    }

    static boolean isValidState(int missionariesLeft, int cannibalsLeft, int missionariesRight, int cannibalsRight) {
        if (missionariesLeft < 0 || cannibalsLeft < 0 || missionariesRight < 0 || cannibalsRight < 0) {
            return false;
        }

        if (missionariesLeft > 0 && missionariesLeft < cannibalsLeft) {
            return false;
        }

        if (missionariesRight > 0 && missionariesRight < cannibalsRight) {
            return false;
        }

        return true;
    }

    static void addState(Queue<State> queue, Set<State> visited, State next) {
        if (!visited.contains(next)) {
            visited.add(next);
            queue.add(next);
        }
    }

    static void solve() {
        Queue<State> queue = new LinkedList<>();
        Set<State> visited = new HashSet<>();

        State start = new State(PEOPLE, PEOPLE, 0, null, "Start");
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            State current = queue.poll();

            int missionariesRight = PEOPLE - current.missionariesLeft;
            int cannibalsRight = PEOPLE - current.cannibalsLeft;

            if (current.missionariesLeft == 0 && current.cannibalsLeft == 0 && current.boatSide == 1) {
                printPath(current);
                return;
            }

            for (int m = 0; m <= 2; m++) {
                for (int c = 0; c <= 2; c++) {
                    if ((m + c) == 0 || (m + c) > 2) {
                        continue;
                    }

                    int nextMissionariesLeft = current.missionariesLeft;
                    int nextCannibalsLeft = current.cannibalsLeft;

                    if (current.boatSide == 0) {
                        nextMissionariesLeft -= m;
                        nextCannibalsLeft -= c;
                    } else {
                        nextMissionariesLeft += m;
                        nextCannibalsLeft += c;
                    }

                    int nextMissionariesRight = PEOPLE - nextMissionariesLeft;
                    int nextCannibalsRight = PEOPLE - nextCannibalsLeft;

                    if (!isValidState(nextMissionariesLeft, nextCannibalsLeft, nextMissionariesRight, nextCannibalsRight)) {
                        continue;
                    }

                    if (current.boatSide == 0) {
                        if (m > current.missionariesLeft || c > current.cannibalsLeft) {
                            continue;
                        }
                    } else {
                        if (m > missionariesRight || c > cannibalsRight) {
                            continue;
                        }
                    }

                    int nextBoatSide = 1 - current.boatSide;
                    String action = "Boat crosses from "
                            + (current.boatSide == 0 ? "left" : "right")
                            + " to " + (nextBoatSide == 0 ? "left" : "right")
                            + " with " + m + " missionary(s) and " + c + " cannibal(s)";

                    State next = new State(nextMissionariesLeft, nextCannibalsLeft, nextBoatSide, current, action);
                    addState(queue, visited, next);
                }
            }
        }

        System.out.println("No solution exists.");
    }

    static void printPath(State goal) {
        List<State> path = new ArrayList<>();
        State current = goal;

        while (current != null) {
            path.add(current);
            current = current.parent;
        }

        Collections.reverse(path);

        System.out.println("Solution steps:");
        for (int i = 0; i < path.size(); i++) {
            State state = path.get(i);
            System.out.println((i == 0 ? "Start" : "Step " + i) + ": "
                    + (state.action == null ? "" : state.action)
                    + " | left side = (" + state.missionariesLeft + ", " + state.cannibalsLeft + ")"
                    + " | right side = (" + (PEOPLE - state.missionariesLeft) + ", " + (PEOPLE - state.cannibalsLeft) + ")"
                    + " | boat = " + (state.boatSide == 0 ? "left" : "right"));
        }
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Run missionary-cannibal solver? (y/n): ");
            String choice = sc.nextLine().trim().toLowerCase();

            if ("y".equals(choice) || "yes".equals(choice) || choice.isEmpty()) {
                solve();
            } else {
                System.out.println("Program ended.");
            }
        }
    }
}
