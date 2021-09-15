import java.util.Arrays;

public class DiagonalSudoku {
    // A record that represents the position on the board (records are available since Java 14 https://openjdk.java.net/jeps/359)
    private static record Position(int row, int column) {
    }

    /**
     * The main function solving a X-sudoku board.
     * Works with boards NxN (N - size of the board), assuming that N is a perfect square.
     * Square root of N is the size of the squares.
     * If a board passed has duplicates is treated as unsolvable.
     *
     * @param board the unsolved X-sudoku board
     * @return the solved sudoku board or null if it is not possible to solve the given board
     * @throws IllegalArgumentException if the board is invalid
     */
    public static int[][] solve(int[][] board) {
//        check the board for null
        if (board == null) return null;
//        check if the board is valid
        if (!isValidBoard(board)) throw new IllegalArgumentException();

        var emptyPosition = findEmptyPosition(board);
        if (emptyPosition == null) return board;

        int row = emptyPosition.row, column = emptyPosition.column;

        for (var i = 1; i <= board.length; i++) {
            if (isValidNumberForPosition(board, i, new Position(row, column))) {
                board[row][column] = i;
                if (solve(board) != null) return board;
                // if it was not possible to find a number for the empty position, the cell is set back to 0 (empty)
                board[row][column] = 0;
            }
        }

        return null;
    }

    //    finds an empty position in the board
    //    returns the empty position
    private static Position findEmptyPosition(int[][] board) {
        for (var i = 0; i < board.length; i++) {
            for (var j = 0; j < board.length; j++) {
                if (board[i][j] == 0) {
                    return new Position(i, j);
                }
            }
        }
        return null;
    }

    //    checks if it is valid to place the given number in the given position
    private static boolean isValidNumberForPosition(int[][] board, int number, Position position) {
        // check row and column
        for (var i = 0; i < board.length; i++) {
            if ((board[position.row][i] == number && position.column != i) || (board[i][position.column] == number && position.row != i)) {
                return false;
            }
        }

        var squareSize = (int) Math.sqrt(board.length);

        // check square
        int iSquare = (position.row / squareSize) * squareSize;
        int jSquare = (position.column / squareSize) * squareSize;

        for (var i = iSquare; i < iSquare + squareSize; i++) {
            for (var j = jSquare; j < jSquare + squareSize; j++) {
                if (board[i][j] == number && i != position.row && j != position.column) {
                    return false;
                }
            }
        }

        // check primary diagonal
        if (position.row == position.column) {
            for (var i = 0; i < board.length; i++) {
                if (board[i][i] == number) {
                    return false;
                }
            }
        }

        // check secondary diagonal
        if (position.column + position.row == board.length - 1) {
            for (var i = 0; i < board.length; i++) {
                if (board[i][board.length - i - 1] == number) {
                    return false;
                }
            }
        }

        return true;
    }

    //    checks if the board is valid
    private static boolean isValidBoard(int[][] board) {
//        the size of the board has to be a perfect square
        if (!isPerfectSquare(board.length)) return false;
//        check that the board size is NxN
        if (Arrays.stream(board).anyMatch(v -> v.length != board.length)) return false;

//        checks if the numbers in the given board are in the correct range (0 to board.length inclusive)
        for (int[] row : board) {
            for (int j = 0; j < board.length; j++) {
                if (row[j] < 0 || row[j] > board.length) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isPerfectSquare(int n) {
        var sqrt = Math.sqrt(n);
        return (sqrt - Math.floor(sqrt)) == 0;
    }
}
