import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;

public class PuzzleGame extends JFrame {

    private JPanel puzzlePanel;
    private JButton[] buttons;
    private final int GRID_SIZE = 3; // Ukuran grid 3x3
    private ArrayList<String> buttonLabels;

    public PuzzleGame() {
        setTitle("Game Puzzle");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel atas untuk judul
        JLabel title = new JLabel("Selamat Datang di Game Puzzle", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        // Panel puzzle
        puzzlePanel = new JPanel();
        puzzlePanel.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        add(puzzlePanel, BorderLayout.CENTER);

        // Tombol dan label puzzle
        buttons = new JButton[GRID_SIZE * GRID_SIZE];
        buttonLabels = new ArrayList<>();
        for (int i = 1; i <= GRID_SIZE * GRID_SIZE - 1; i++) {
            buttonLabels.add(String.valueOf(i));
        }
        buttonLabels.add(""); // Tombol kosong

        // Acak urutan tombol
        Collections.shuffle(buttonLabels);

        // Tambahkan tombol ke panel
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(buttonLabels.get(i));
            buttons[i].setFont(new Font("Arial", Font.BOLD, 24));
            buttons[i].addActionListener(new ButtonListener());
            puzzlePanel.add(buttons[i]);
        }

        // Tombol reset
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetPuzzle());
        add(resetButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void resetPuzzle() {
        Collections.shuffle(buttonLabels);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setText(buttonLabels.get(i));
        }
    }

    private void swapButtons(int emptyIndex, int clickedIndex) {
        String temp = buttons[emptyIndex].getText();
        buttons[emptyIndex].setText(buttons[clickedIndex].getText());
        buttons[clickedIndex].setText(temp);
    }

    private boolean isSolved() {
        for (int i = 0; i < buttonLabels.size() - 1; i++) {
            if (!buttons[i].getText().equals(String.valueOf(i + 1))) {
                return false;
            }
        }
        return buttons[buttons.length - 1].getText().equals("");
    }

    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            int clickedIndex = -1, emptyIndex = -1;

            // Cari indeks tombol yang diklik dan tombol kosong
            for (int i = 0; i < buttons.length; i++) {
                if (buttons[i] == clickedButton) {
                    clickedIndex = i;
                }
                if (buttons[i].getText().equals("")) {
                    emptyIndex = i;
                }
            }

            // Cek jika tombol yang diklik bisa ditukar
            if ((clickedIndex == emptyIndex - 1 && clickedIndex % GRID_SIZE != GRID_SIZE - 1)
                    || (clickedIndex == emptyIndex + 1 && clickedIndex % GRID_SIZE != 0)
                    || clickedIndex == emptyIndex - GRID_SIZE || clickedIndex == emptyIndex + GRID_SIZE) {
                swapButtons(emptyIndex, clickedIndex);
            }

            // Periksa apakah puzzle selesai
            if (isSolved()) {
                JOptionPane.showMessageDialog(null, "Selamat! Anda menyelesaikan puzzle!");
            }
        }
    }

    public static void main(String[] args) {
        new PuzzleGame();
    }
}
