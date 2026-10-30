import java.util.*;

public class WaterJug {

    // Represents the state of both jugs
    static class State {
        int a, b;
        State parent;
        String action;

        State(int a, int b, State parent, String action) {
            this.a = a;
            this.b = b;
            this.parent = parent;
            this.action = action;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            State state = (State) obj;
            return a == state.a && b == state.b;
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b);
        }
    }

    public static void solve(int m, int n, int target) {

        // A solution is impossible if target exceeds the sum of both jugs
        // or target is not divisible by gcd(m, n).
        if (target > m + n
                || target % gcd(m, n) != 0) {
            System.out.println("No solution exists.");
            return;
        }

        Queue<State> queue = new LinkedList<>();
        Set<State> visited = new HashSet<>();

        State start = new State(0, 0, null, "Start");
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            State current = queue.poll();

            // Goal: target amount in either jug
            if (current.a == target || current.b == target) {
                printPath(current);
                return;
            }

            int a = current.a;
            int b = current.b;

            // 1. Fill Jug A
            addState(queue, visited, new State(m, b, current, "Fill Jug A"));

            // 2. Fill Jug B
            addState(queue, visited, new State(a, n, current, "Fill Jug B"));

            // 3. Empty Jug A
            addState(queue, visited, new State(0, b, current, "Empty Jug A"));

            // 4. Empty Jug B
            addState(queue, visited, new State(a, 0, current, "Empty Jug B"));

            // 5. Pour A into B
            int pourAB = Math.min(a, n - b);
            addState(queue, visited, new State(a - pourAB, b + pourAB, current, "Pour A into B"));

            // 6. Pour B into A
            int pourBA = Math.min(b, m - a);
            addState(queue, visited, new State(a + pourBA, b - pourBA, current, "Pour B into A"));
        }

        System.out.println("No solution exists.");
    }

    // Add a new state only if it has not been visited
    static void addState(Queue<State> q, Set<State> visited, State newState) {
        if (visited.contains(newState))
            return;
        visited.add(newState);
        q.add(newState);
    }

    // Print the solution from start to goal
    static void printPath(State goal) {
        List<State> path = new ArrayList<>();

        while (goal != null) {
            path.add(goal);
            goal = goal.parent;
        }

        Collections.reverse(path);

        for (int i = 0; i < path.size(); i++) {
            State s = path.get(i);
            System.out.println("Step " + i + ": " + s.action
                    + " -> Jug A = " + s.a
                    + " L, Jug B = " + s.b + " L");
        }

        System.out.println("Target achieved!");
    }

    static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Enter capacity of Jug A: ");
            int m = sc.nextInt();

            System.out.print("Enter capacity of Jug B: ");
            int n = sc.nextInt();

            System.out.print("Enter target amount: ");
            int target = sc.nextInt();

            if (m <= 0 || n <= 0 || target < 0) {
                System.out.println("Enter valid positive capacities "
                        + "and a non-negative target.");
            } else {
                solve(m, n, target);
            }
        }
    }
}
