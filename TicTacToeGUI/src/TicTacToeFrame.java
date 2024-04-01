import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToeFrame extends JFrame implements ActionListener {
    private TicTacToeButton[][] buttons;
    private boolean xTurn;
    private int moves;

    public TicTacToeFrame() {
        setTitle("Tic Tac Toe");
        setSize(300, 350); // Adjusted height to accommodate the quit button
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 3, 5, 5)); // 4 rows, 3 columns, with 5 pixels of spacing between buttons

        buttons = new TicTacToeButton[3][3];
        xTurn = true;
        moves = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new TicTacToeButton();
                buttons[i][j].addActionListener(this);
                add(buttons[i][j]);
            }
        }

        // Add an empty label for layout purposes
        add(new JLabel(""));

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Thanks for playing!");
                System.exit(0);
            }
        });
        add(quitButton);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        TicTacToeButton button = (TicTacToeButton) e.getSource();
        if (button.getState() == TicTacToeButton.State.EMPTY) {
            int row = -1, col = -1;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (buttons[i][j] == button) {
                        row = i;
                        col = j;
                        break;
                    }
                }
            }

            if (row != -1 && col != -1) {
                if (xTurn) {
                    button.setState(TicTacToeButton.State.X);
                    button.setText("X");
                } else {
                    button.setState(TicTacToeButton.State.O);
                    button.setText("O");
                }
                moves++;

                if (moves >= 7 && isTie()) { // Check for tie condition after 7 moves
                    JOptionPane.showMessageDialog(this, "It's a tie!");
                    if (playAgain()) {
                        resetBoard();
                    } else {
                        JOptionPane.showMessageDialog(this, "Thanks for playing!");
                        System.exit(0);
                    }
                    return;
                }

                if (moves >= 5 && checkForWin(row, col)) {
                    JOptionPane.showMessageDialog(this, "Player " + (xTurn ? "X" : "O") + " wins!");
                    if (playAgain()) {
                        resetBoard();
                    } else {
                        JOptionPane.showMessageDialog(this, "Thanks for playing!");
                        System.exit(0);
                    }
                    return;
                }

                if (moves == 9) { // If no winner is found after 9 moves, it's a tie
                    JOptionPane.showMessageDialog(this, "It's a tie!");
                    if (playAgain()) {
                        resetBoard();
                    } else {
                        JOptionPane.showMessageDialog(this, "Thanks for playing!");
                        System.exit(0);
                    }
                    return;
                }

                xTurn = !xTurn; // Move this line here to switch turns only if the move is valid
            }
        } else {
            JOptionPane.showMessageDialog(this, "Illegal move! Please choose an empty square.");
        }
    }

    private boolean checkForWin(int row, int col) {
        TicTacToeButton.State currentPlayer = buttons[row][col].getState();

        // Check row
        if (buttons[row][0].getState() == currentPlayer &&
                buttons[row][1].getState() == currentPlayer &&
                buttons[row][2].getState() == currentPlayer) {
            return true;
        }
        // Check column
        if (buttons[0][col].getState() == currentPlayer &&
                buttons[1][col].getState() == currentPlayer &&
                buttons[2][col].getState() == currentPlayer) {
            return true;
        }
        // Check diagonals
        if ((row == col || row + col == 2) &&
                ((buttons[0][0].getState() == currentPlayer &&
                        buttons[1][1].getState() == currentPlayer &&
                        buttons[2][2].getState() == currentPlayer) ||
                        (buttons[0][2].getState() == currentPlayer &&
                                buttons[1][1].getState() == currentPlayer &&
                                buttons[2][0].getState() == currentPlayer))) {
            return true;
        }
        return false;
    }

    private boolean isTie() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getState() == TicTacToeButton.State.EMPTY) {
                    return false; // If any empty square is found, the game is not a tie yet
                }
            }
        }
        return true; // If no empty square is found, the game is a tie
    }

    private void resetBoard() {
        xTurn = true;
        moves = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setState(TicTacToeButton.State.EMPTY);
            }
        }
    }

    private boolean playAgain() {
        int choice = JOptionPane.showConfirmDialog(this, "Do you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION);
        return choice == JOptionPane.YES_OPTION;
    }

    public static void main(String[] args) {
        new TicTacToeFrame();
    }
}

class TicTacToeButton extends JButton {
    public enum State {
        EMPTY, X, O;
    }

    private State state;

    public TicTacToeButton() {
        state = State.EMPTY;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
