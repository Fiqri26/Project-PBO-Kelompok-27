import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class PuzzleGame extends GameFrame implements ButtonActionHandler{

    private JPanel puzzlePanel;
    private JPanel sizePanel;
    private JButton[] buttons;
    private ArrayList<String> buttonLabels;
    private JComboBox<String> lvlComboBox;
    private JLabel timeLabel, shuffleCountLabel;
    private int shuffleCount = 0;
    private int gridSize;
    private boolean timerStarted = false;
    private TimerThread timerThread;

    public PuzzleGame() {
        super("Pictzzle");
        puzzlePanel = new JPanel();
        puzzlePanel.setBackground(new Color(100, 149, 237));
        puzzlePanel.setLayout(new BorderLayout());
        setContentPane(puzzlePanel);
        showLevelSelection();
    }

    private void showLevelSelection() {
        sizePanel = new JPanel();
        sizePanel.setLayout(new BoxLayout(sizePanel, BoxLayout.Y_AXIS));
        sizePanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 50, 60));
        sizePanel.setBackground(new Color(255, 255, 255));

        JLabel selectLabel = new JLabel("Select Level:");
        selectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectLabel.setFont(new Font("Arial", Font.BOLD, 18));

        String[] levels = { "Level 1", "Level 2", "Level 3", "Level 4", "Level 5" };
        lvlComboBox = new JComboBox<>(levels);
        lvlComboBox.setMaximumSize(new Dimension(200, 30));
        lvlComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        lvlComboBox.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton startButton = new JButton("Start Game");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setFocusPainted(false);
        startButton.setBackground(new Color(100, 149, 237));
        startButton.setForeground(Color.WHITE);
        startButton.addActionListener(e -> StartGame());

        sizePanel.add(Box.createVerticalStrut(20));
        sizePanel.add(selectLabel);
        sizePanel.add(Box.createVerticalStrut(20));
        sizePanel.add(lvlComboBox);
        sizePanel.add(Box.createVerticalStrut(40));
        sizePanel.add(startButton);
        sizePanel.add(Box.createVerticalStrut(0));

        puzzlePanel.setLayout(new GridBagLayout());
        puzzlePanel.add(sizePanel);
        revalidate();
        repaint();
    }


    private void initializeGame() {
        puzzlePanel.removeAll();
        puzzlePanel.setLayout(new BorderLayout());

        JPanel gridContainer = new JPanel(new GridBagLayout());
        JPanel gridPanel = new JPanel(new GridLayout(gridSize, gridSize));
        gridPanel.setPreferredSize(new Dimension(400, 400));
        gridPanel.setBackground(Color.WHITE);

        gridContainer.add(gridPanel);
        puzzlePanel.add(gridPanel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
        infoPanel.setBackground(new Color(255, 255, 255));
        infoPanel.setPreferredSize(new Dimension(250, 0));

        timeLabel = new JLabel("Time Elapsed : 0 seconds");
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        shuffleCountLabel = new JLabel("Number of Shuffles: " + shuffleCount);
        shuffleCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension buttonSize = new Dimension(120, 40);

        JButton resetButton = createInfoButton("Reset", buttonSize, e -> reset());
        JButton pauseButton = createInfoButton("Pause", buttonSize, e -> pause());
        JButton resumeButton = createInfoButton("Resume", buttonSize, e -> resume());
        JButton exitButton = createInfoButton("Exit", buttonSize, e -> exit());

        infoPanel.add(timeLabel);
        infoPanel.add(Box.createHorizontalStrut(20));
        infoPanel.add(resetButton);
        infoPanel.add(Box.createHorizontalStrut(10));
        infoPanel.add(pauseButton);
        infoPanel.add(Box.createHorizontalStrut(10));
        infoPanel.add(resumeButton);
        infoPanel.add(Box.createHorizontalStrut(10));
        infoPanel.add(exitButton);

        puzzlePanel.add(infoPanel, BorderLayout.EAST);

        initializeButtons(gridPanel);

        revalidate();
        repaint();

        timerStarted = false;
        if (timerThread != null) {
            timerThread.stopTimer();
        }
        timerThread = new TimerThread(timeLabel);
    }

    private JButton createInfoButton(String text, Dimension size, ActionListener action) {
        JButton button = new JButton(text);
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(100, 149, 237));
        button.addActionListener(action);
        return button;
    }

    private void initializeButtons(JPanel gridPanel) {
        buttons = new JButton[gridSize * gridSize];
        buttonLabels = new ArrayList<>();

        for (int i = 0; i < gridSize * gridSize - 1; i++) {
            buttonLabels.add(String.valueOf(i));
        }
        buttonLabels.add("");

        Collections.shuffle(buttonLabels);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(buttonLabels.get(i));
            buttons[i].setFont(new Font("Arial", Font.BOLD, Math.max(30, 60 / gridSize)));
            buttons[i].addActionListener(new ButtonListener());
            gridPanel.add(buttons[i]);
        }
    }

    @Override
    public void StartGame(){
        int selectedLevel = lvlComboBox.getSelectedIndex() + 1;
        gridSize = selectedLevel + 1;
        initializeGame();
        sizePanel.setVisible(false);
    }

    @Override
    public void reset() {
        if (timerThread != null) {
            timerThread.stopTimer();
            try {
                timerThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        timerStarted = false;
        timeLabel.setText("Time Elapsed: 0 seconds");
        timerThread = new TimerThread(timeLabel);
        resetPuzzle();
    }

    @Override
    public void pause() {
        if (timerThread != null) {
            timerThread.pauseTimer();
        }
    }

    @Override
    public void resume() {
        if (timerThread != null) {
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
        if (timerThread != null) {
            timerThread.resetTimer();
        }
    }

    public class TimerThread extends Thread {
        private int elapsedTimeInSeconds = 0;
        private boolean running = true;
        private boolean paused = false;
        private JLabel timeLabel;

        public TimerThread(JLabel timeLabel) {
            this.timeLabel = timeLabel;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    synchronized (this) {
                        while (paused) {
                            wait();
                        }
                    }
                    Thread.sleep(1000);
                    elapsedTimeInSeconds++;
                    SwingUtilities
                            .invokeLater(() -> timeLabel.setText("Time Elapsed: " + elapsedTimeInSeconds + " Seconds"));
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
            SwingUtilities.invokeLater(() -> timeLabel.setText("Time Elapsed: 0 seconds"));
        }

        public void pauseTimer() {
            paused = true;
        }

        public synchronized void resumeTimer() {
            paused = false;
            notify();
        }
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

        if (!buttons[buttons.length - 1].getText().equals("")) {
            return false;
        }

        if (timerThread != null) {
            timerThread.stopTimer();
        }

        JPanel completeDialogPanel = new JPanel();
        completeDialogPanel.setLayout(new BoxLayout(completeDialogPanel, BoxLayout.Y_AXIS));
        completeDialogPanel.setBackground(new Color(255, 255, 255));
        completeDialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel messageLabel = new JLabel("Selamat!");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 20));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel intructionLabel = new JLabel("Apa anda ingin melanjutkan ke level berikutnya?");
        intructionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        intructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        completeDialogPanel.add(messageLabel);
        completeDialogPanel.add(Box.createHorizontalStrut(15));
        completeDialogPanel.add(intructionLabel);

        Object[] options = { "Next", "Cancel" };
        int choice = JOptionPane.showOptionDialog(
                this,
                completeDialogPanel,
                "Level Completed",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null,
                options,
                options[0]);

        if (choice == JOptionPane.YES_OPTION) {
            if (gridSize < 5) {
                gridSize++;
            }
            initializeGame();
        } else {
            initializeGame();
        }

        return true;
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!timerStarted) {
                timerThread.start();
                timerStarted = true;
            }

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

            if ((clickedIndex == emptyIndex - 1 && clickedIndex % gridSize != gridSize - 1)
                    || (clickedIndex == emptyIndex + 1 && clickedIndex % gridSize != 0)
                    || clickedIndex == emptyIndex - gridSize
                    || clickedIndex == emptyIndex + gridSize) {
                swapButtons(emptyIndex, clickedIndex);
            }

            isSolved();
        }
    }

    public static void main(String[] args) {
        PuzzleGame game = new PuzzleGame();
        game.setVisible(true);

    }
}
