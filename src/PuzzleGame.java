import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
// import javax.swing.Timer;

public class PuzzleGame extends JFrame implements ButtonActionHandler{

    private JPanel puzzlePanel;
    private JButton[] buttons;
    private ArrayList<String> buttonLabels;
    private JLabel timeLabel, shuffleCountLabel;
    private int shuffleCount = 0;
    // private long startTime;
    private int gridSize;
    private TimerThread timerThread;

    public PuzzleGame() {
        setTitle("Pitczzle");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        // setLayout(new BorderLayout());

        // Size Selection Panel
        // JPanel sizePanel = new JPanel();
        // String[] sizes = { "2", "3", "4" };
        // JComboBox<String> sizeComboBox = new JComboBox<>(sizes);
        // JButton startButton = new JButton("Start Game");

        puzzlePanel = new JPanel();
        puzzlePanel.setBackground(new Color(100, 149, 237));
        puzzlePanel.setLayout(new BorderLayout());
        setContentPane(puzzlePanel);

        showLevelSelection();

        // startButton.addActionListener(e -> {
        //     gridSize = Integer.parseInt((String) sizeComboBox.getSelectedItem());
        //     initializeGame();
        //     sizePanel.setVisible(false);
        // });

        // sizePanel.add(new JLabel("Select Puzzle Size:"));
        // sizePanel.add(sizeComboBox);
        // sizePanel.add(startButton);
        // add(sizePanel, BorderLayout.NORTH);

        // setVisible(true);
    }

    private void showLevelSelection() {
        JPanel sizePanel = new JPanel();
        sizePanel.setLayout(new BoxLayout(sizePanel, BoxLayout.Y_AXIS));
        sizePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        sizePanel.setBackground(new Color(255, 223, 186)); // Warna krem muda
        // sizePanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 3));
        sizePanel.setPreferredSize(new Dimension(300, 200));

        JLabel selectLabel = new JLabel("Select Level:");
        selectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectLabel.setFont(new Font("Arial", Font.BOLD, 18));

        String[] levels = {"Level 1", "Level 2", "Level 3", "Level 4", "Level 5"};
        JComboBox<String> lvlComboBox = new JComboBox<>(levels);
        lvlComboBox.setMaximumSize(new Dimension(200, 30));
        lvlComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        lvlComboBox.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton startButton = new JButton("Start Game");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setFocusPainted(false);
        startButton.setBackground(new Color(100, 149, 237));
        startButton.setForeground(Color.WHITE);
        startButton.addActionListener(e -> {
            int selectedLevel = lvlComboBox.getSelectedIndex() + 1;
            gridSize = selectedLevel + 1;
            initializeGame();
            sizePanel.setVisible(false);
        });

        sizePanel.add(Box.createVerticalStrut(20));
        sizePanel.add(selectLabel);
        sizePanel.add(Box.createVerticalStrut(20));
        sizePanel.add(lvlComboBox);
        sizePanel.add(Box.createVerticalStrut(20));
        sizePanel.add(startButton);
        sizePanel.add(Box.createVerticalStrut(20));

        puzzlePanel.setLayout(new GridBagLayout());
        puzzlePanel.add(sizePanel);

        revalidate();
        repaint();
    }

    private void initializeGame() {
        // Game Panel
        // puzzlePanel = new JPanel();
        // puzzlePanel.setLayout(new GridLayout(gridSize, gridSize));
        // add(puzzlePanel, BorderLayout.CENTER);

        puzzlePanel.removeAll();
        puzzlePanel.setLayout(new BorderLayout());

        JPanel gridContainer = new JPanel(new GridBagLayout());
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(gridSize, gridSize));
        gridPanel.setPreferredSize(new Dimension(400, 400));
        gridPanel.setBackground(Color.WHITE);

        gridContainer.add(gridPanel);
        puzzlePanel.add(gridPanel, BorderLayout.CENTER);

        // Information Panel
        JPanel infoPanel = new JPanel();
        // infoPanel.setLayout(new GridLayout(6, 1));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
        infoPanel.setBackground(Color.LIGHT_GRAY);
        infoPanel.setPreferredSize(new Dimension(250, 0));

        // Labels to display information
        timeLabel = new JLabel("Time Elapsed: 0 seconds");
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // memoryLabel = new JLabel("Current Memory: " + getMemoryUsage() + " MB");
        // shuffleCountLabel = new JLabel("Number of Shuffles: " + shuffleCount);

        // Add labels to info panel
        // infoPanel.add(new JLabel("Puzzle Size: " + gridSize));
        // infoPanel.add(shuffleCountLabel);
        // infoPanel.add(memoryLabel);

        Dimension buttonSize = new Dimension(120, 40);

        // JButton showTimingButton = new JButton("Show Timing");
        // showTimingButton.addActionListener(e -> startTiming());

        JButton resetButton = createInfoButton("Reset", buttonSize, e -> reset());
        JButton pauseButton = createInfoButton("Pause", buttonSize, e -> pause());
        JButton resumeButton = createInfoButton("Resume", buttonSize, e -> resume());
        JButton exitButton = createInfoButton("Exit", buttonSize, e -> exit());

        // JButton resetButton = new JButton("Reset/Resshuffle");
        // resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        // resetButton.addActionListener(e -> resetPuzzle());

        // JButton pauseButton = new JButton("Pause");
        // pauseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        // pauseButton.addActionListener(e -> {
        //     if (timerThread != null) {
        //         timerThread.stopTimer();
        //     }
        // });

        // JButton resumeButton = new JButton("Resume");
        // resumeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        // resumeButton.addActionListener(e -> {
        //     if (timerThread != null && timerThread.isAlive()) {
        //         timerThread.start();
        //     }
        // });

        // JButton exitButton = new JButton("Exit");
        // exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        // exitButton.addActionListener(e -> {
        //     if (timerThread != null) {
        //         timerThread.stopTimer();
        //     }
        //     System.exit(0);
        // });

        // Add buttons to info panel
        // infoPanel.add(showTimingButton);
        infoPanel.add(timeLabel);
        infoPanel.add(Box.createHorizontalStrut(20));
        infoPanel.add(resetButton);
        infoPanel.add(Box.createHorizontalStrut(10));
        infoPanel.add(pauseButton);
        infoPanel.add(Box.createHorizontalStrut(10));
        infoPanel.add(resumeButton);
        infoPanel.add(Box.createHorizontalStrut(10));
        infoPanel.add(exitButton);

        // add(infoPanel, BorderLayout.EAST);
        puzzlePanel.add(infoPanel, BorderLayout.EAST);

        // Initialize buttons
        initializeButtons(gridPanel);

        revalidate();
        repaint();

        // setVisible(true);

        timerThread = new TimerThread(timeLabel);
        timerThread.start();
    }

    private JButton createInfoButton(String text, Dimension size, ActionListener action){
        JButton button = new  JButton(text);
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.addActionListener(action);
        return button;
    }

    private void initializeButtons(JPanel gridPanel) {
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
            buttons[i].setFont(new Font("Arial", Font.BOLD, Math.max(30, 60 / gridSize)));
            buttons[i].addActionListener(new ButtonListener());
            gridPanel.add(buttons[i]);
        }
    }

    @Override
    public void reset() {
        resetPuzzle();
    }

    @Override
    public void pause() {
        if (timerThread != null) {
            timerThread.stopTimer();
        }
    }

    @Override
    public void resume() {
        if (timerThread != null && timerThread.isAlive()) {
            timerThread.resumeTimer();
        }
    }

    @Override
    public void exit() {
        if (timerThread != null) {
            timerThread.stopTimer();
        }
        System.exit(0);
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
        private boolean paused = false;
        private JLabel timeLabel;

        public TimerThread(JLabel timeLabel) {
            this.timeLabel = timeLabel;
        }

        @Override
        public void run() {
            running = true;
            while (running) {
                try {
                    synchronized (this) {
                        while (paused) {
                            wait(); // Tunggu sampai `resumeTimer()` dipanggil
                        }
                    }
                    Thread.sleep(1000);
                    elapsedTimeInSeconds++;
                    SwingUtilities.invokeLater(() ->
                        timeLabel.setText("Time Elapsed: " + elapsedTimeInSeconds + " Seconds")
                    );
                } catch (InterruptedException e) {
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

        public void pauseTimer() {
            paused = true;
        }

        public synchronized void resumeTimer() {
            paused = false;
            notify(); // Melanjutkan thread yang sedang menunggu
        }

        public int getElapsedTimeInSeconds() {
            return elapsedTimeInSeconds;
        }
    }


    // private void startTiming() {
    //     // Logic for timing (could be implemented with a Timer)
    //     new Timer(1000, e -> {
    //     long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
    //     timeLabel.setText("Time Elapsed: " + elapsedTime + " seconds");
    //     }).start();

    // }

    // private String getMemoryUsage() {
    //     long memory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    //     return String.format("%.2f", (memory / 1024.0 / 1024.0));
    // }

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

}
