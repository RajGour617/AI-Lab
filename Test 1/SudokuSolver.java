import java.util.*;

public class SudokuSolver {

    static final int SIZE = 9;

    // Check whether placing num at grid[row][col] is safe
    static boolean isSafe(int[][] grid, int row, int col, int num) {

        // Check the entire row
        for (int j = 0; j < SIZE; j++) {
            if (grid[row][j] == num)
                return false;
        }

        // Check the entire column
        for (int i = 0; i < SIZE; i++) {
            if (grid[i][col] == num)
                return false;
        }

        // Find the top-left cell of the 3x3 subgrid
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;

        // Check all cells in the 3x3 subgrid
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (grid[i][j] == num)
                    return false;
            }
        }

        return true;
    }

    // Backtracking algorithm to solve the Sudoku
    static boolean solve(int[][] grid) {

        // Find the first empty cell
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {

                if (grid[row][col] == 0) {

                    // Try every digit from 1 to 9
                    for (int num = 1; num <= 9; num++) {

                        if (isSafe(grid, row, col, num)) {
                            grid[row][col] = num;

                            // Recursively solve the remaining grid
                            if (solve(grid)) {
                                return true;
                            }

                            // Undo the choice if it leads to failure
                            grid[row][col] = 0;
                        }
                    }

                    // No digit works in this empty cell
                    return false;
                }
            }
        }

        // No empty cells remain
        return true;
    }

    // Verify that the initial puzzle contains no duplicate clues
    static boolean isValidInitial(int[][] grid) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {

                int num = grid[row][col];

                if (num < 0 || num > 9)
                    return false;

                if (num != 0) {
                    // Temporarily remove the current clue
                    grid[row][col] = 0;
                    boolean safe = isSafe(grid, row, col, num);
                    grid[row][col] = num;

                    if (!safe)
                        return false;
                }
            }
        }
        return true;
    }

    // Display the Sudoku grid
    static void printGrid(int[][] grid) {
        for (int i = 0; i < SIZE; i++) {
            if (i % 3 == 0 && i != 0) {
                System.out.println("------+-------+------");
            }

            for (int j = 0; j < SIZE; j++) {
                if (j % 3 == 0 && j != 0) {
                    System.out.print("| ");
                }

                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            int[][] grid = new int[SIZE][SIZE];

            System.out.println("Enter the Sudoku grid (9 rows, 9 digits each).");
            System.out.println("Use 0 for empty cells:");

            // Read all 81 cells from the user
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    grid[i][j] = sc.nextInt();
                }
            }

            System.out.println("\nOriginal Sudoku:");
            printGrid(grid);

            if (!isValidInitial(grid)) {
                System.out.println("Invalid Sudoku: duplicate or invalid clues.");
            } else if (solve(grid)) {
                System.out.println("\nSolved Sudoku:");
                printGrid(grid);
            } else {
                System.out.println("No solution exists.");
            }
        }

    }
}
