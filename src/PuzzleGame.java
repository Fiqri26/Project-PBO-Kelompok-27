import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.zone.ZoneOffsetTransitionRule.TimeDefinition;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class PuzzleGame extends JFrame {

    private JPanel puzzlePanel;
    private JButton[] buttons;
    private ArrayList<String> buttonLabels;
    private JLabel timeLabel, memoryLabel, shuffleCountLabel;
    private int shuffleCount = 0;
    private long startTime;
    private int gridSize;
    private TimerThread timerThread;

    public PuzzleGame() {
        setTitle("Game Puzzle");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Size Selection Panel
        JPanel sizePanel = new JPanel();
        String[] sizes = { "2", "3", "4" };
        JComboBox<String> sizeComboBox = new JComboBox<>(sizes);
        JButton startButton = new JButton("Start Game");

        startButton.addActionListener(e -> {
            gridSize = Integer.parseInt((String) sizeComboBox.getSelectedItem());
            initializeGame();
            sizePanel.setVisible(false);
        });

        sizePanel.add(new JLabel("Select Puzzle Size:"));
        sizePanel.add(sizeComboBox);
        sizePanel.add(startButton);
        add(sizePanel, BorderLayout.NORTH);

        setVisible(true);
    }

    private void initializeGame() {
        // Game Panel
        puzzlePanel = new JPanel();
        puzzlePanel.setLayout(new GridLayout(gridSize, gridSize));
        add(puzzlePanel, BorderLayout.CENTER);

        // Information Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(6, 1));

        // Labels to display information
        timeLabel = new JLabel("Time Elapsed: 0 seconds");
        memoryLabel = new JLabel("Current Memory: " + getMemoryUsage() + " MB");
        shuffleCountLabel = new JLabel("Number of Shuffles: " + shuffleCount);

        // Add labels to info panel
        infoPanel.add(new JLabel("Puzzle Size: " + gridSize));
        infoPanel.add(shuffleCountLabel);
        infoPanel.add(timeLabel);
        infoPanel.add(memoryLabel);

        JButton showTimingButton = new JButton("Show Timing");
        showTimingButton.addActionListener(e -> startTiming());

        JButton resetButton = new JButton("Reset/Resshuffle");
        resetButton.addActionListener(e -> resetPuzzle());

        JButton pauseButton = new JButton("Pause");
        JButton resumeButton = new JButton("Resume");
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> {
            if (timerThread != null) {
                timerThread.stopTimer();
            }
            System.exit(0);
        });

        // Add buttons to info panel
        infoPanel.add(showTimingButton);
        infoPanel.add(resetButton);
        infoPanel.add(pauseButton);
        infoPanel.add(resumeButton);
        infoPanel.add(exitButton);

        add(infoPanel, BorderLayout.EAST);

        // Initialize buttons
        initializeButtons();

        setVisible(true);

        timerThread = new TimerThread(timeLabel);
        timerThread.start();
    }

    private void initializeButtons() {
        buttons = new JButton[gridSize * gridSize];
        buttonLabels = new ArrayList<>();
        for (int i = 0; i < gridSize * gridSize - 1; i++) {
            buttonLabels.add(String.valueOf(i));
        }
        buttonLabels.add(""); // Empty button

        // Shuffle and add buttons
        Collections.shuffle(buttonLabels);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(buttonLabels.get(i));
            buttons[i].setFont(new Font("Arial", Font.BOLD, 60));
            buttons[i].addActionListener(new ButtonListener());
            puzzlePanel.add(buttons[i]);
        }
    }

    private void resetPuzzle() {
        Collections.shuffle(buttonLabels);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setText(buttonLabels.get(i));
        }
        shuffleCount++;
        shuffleCountLabel.setText("Number of Shuffles: " + shuffleCount);
        // startTime = System.currentTimeMillis();
        if(timerThread != null){
            timerThread.resetTimer();
        }
    }

    public class TimerThread extends Thread {
        private int elapsedTimeInSeconds;
        private boolean running = true;
        private JLabel timeLabel;

        public TimerThread(JLabel timeLabel) {
            this.timeLabel = timeLabel;
            // this.elapsedTimeInSeconds = 0;
            // this.running = false;
        }

        @Override
        public void run() {
            running = true;
            while (running) {
                try {
                    Thread.sleep(1000);
                    elapsedTimeInSeconds++;
                    SwingUtilities.invokeLater(() -> 
                        timeLabel.setText("Time Elapsed: " + elapsedTimeInSeconds + " Seconds")
                    );
                    
                } catch (InterruptedException e) {
                    // System.out.println("Timer interupted.");
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        public void stopTimer() {
            running = false;
        }

        public void resetTimer() {
            elapsedTimeInSeconds = 0;
            SwingUtilities.invokeLater(() ->
                timeLabel.setText("Time Elapsed: 0 seconds")
            );
        }

        public int getElapsedTimeInSeconds() {
            return elapsedTimeInSeconds;
        }
    }

    private void startTiming() {
        // Logic for timing (could be implemented with a Timer)
        new Timer(1000, e -> {
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        timeLabel.setText("Time Elapsed: " + elapsedTime + " seconds");
        }).start();

    }

    private String getMemoryUsage() {
        long memory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        return String.format("%.2f", (memory / 1024.0 / 1024.0));
    }

    private void swapButtons(int emptyIndex, int clickedIndex) {
        String temp = buttons[emptyIndex].getText();
        buttons[emptyIndex].setText(buttons[clickedIndex].getText());
        buttons[clickedIndex].setText(temp);
    }

    private boolean isSolved() {
        for (int i = 0; i < buttonLabels.size() - 1; i++) {
            if (!buttons[i].getText().equals(String.valueOf(i))) {
                return false;
            }
        }
        // return buttons[buttons.length - 1].getText().equals("");
        if (!buttons[buttons.length - 1].getText().equals("")){
            return false;
        }

        if (timerThread != null) {
            timerThread.stopTimer();
        }

        JOptionPane.showMessageDialog(this, "Selamat, anda menyelesaikan puzzle dalam waktu " + timerThread.getElapsedTimeInSeconds() + " detik!");
        timerThread.resetTimer();

        return true;
    }

    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            int clickedIndex = -1, emptyIndex = -1;

            for (int i = 0; i < buttons.length; i++) {
                if (buttons[i] == clickedButton) {
                    clickedIndex = i;
                }
                if (buttons[i].getText().equals("")) {
                    emptyIndex = i;
                }
            }

            // Check if the clicked button can be swapped
            if ((clickedIndex == emptyIndex - 1 && clickedIndex % gridSize != gridSize - 1)
                    || (clickedIndex == emptyIndex + 1 && clickedIndex % gridSize != 0)
                    || clickedIndex == emptyIndex - gridSize || clickedIndex == emptyIndex + gridSize) {
                swapButtons(emptyIndex, clickedIndex);
            }

            // if (isSolved()) {
            //     JOptionPane.showMessageDialog(null, "Selamat! Anda menyelesaikan puzzle!");
            // }

            isSolved();
        }
    }

    public static void main(String[] args) {
        new PuzzleGame();
    }
}
