import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.imageio.ImageIO;
// import javax.swing.*;

public class PuzzelGame extends GameFrame implements ButtonActionHandler{

    private JPanel puzzlePanel;
    private JPanel sizePanel;
    // private JButton[] buttons;
    private ArrayList<String> buttonLabels;
    private JComboBox<String> lvlComboBox;
    private JLabel timeLabel, timeTitleLabel;
    private JLabel moveCountLabel, shuffleCountLabel;
    private int shuffleCount = 0;
    private int gridSize;
    private boolean timerStarted = false;
    private TimerThread timerThread;

    private BufferedImage fullImage;
    private BufferedImage[] imagePieces;
    private JLabel[] labels;

    public PuzzelGame() {
        super("Pictzzle");
        puzzlePanel = new JPanel();
        puzzlePanel.setBackground(new Color(135, 206, 250));
        puzzlePanel.setLayout(new BorderLayout());
        setContentPane(puzzlePanel);
        showLevelSelection();
    }

    private void showLevelSelection() {
        sizePanel = new JPanel();
        sizePanel.setLayout(new BoxLayout(sizePanel, BoxLayout.Y_AXIS));
        sizePanel.setBorder(BorderFactory.createEmptyBorder(20, 70, 20, 70));
        sizePanel.setBackground(new Color(173, 216, 230));

        JLabel selectLabel = new JLabel("SELECT LEVEL");
        selectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectLabel.setFont(new Font("Arial", Font.BOLD, 18));

        String[] levels = { "Level 1", "Level 2", "Level 3", "Level 4", "Level 5" };
        lvlComboBox = new JComboBox<>(levels);
        lvlComboBox.setMaximumSize(new Dimension(200, 30));
        lvlComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        lvlComboBox.setBackground(new Color(176, 196, 222));
        lvlComboBox.setForeground(Color.WHITE);
        lvlComboBox.setFont(new Font("Arial", Font.PLAIN, 15));

        JButton startButton = new JButton("Play Game");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setFocusPainted(false);
        startButton.setBackground(new Color(100, 149, 237));
        startButton.setForeground(Color.WHITE);
        startButton.addActionListener(e -> StartGame());

        JButton exitButton = new JButton("Exit Game");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setFont(new Font("Arial", Font.BOLD, 16));
        exitButton.setFocusPainted(false);
        exitButton.setBackground(new Color(250, 128, 114));
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(e -> exit());

        sizePanel.add(Box.createVerticalStrut(10));
        sizePanel.add(selectLabel);
        sizePanel.add(Box.createVerticalStrut(30));
        sizePanel.add(lvlComboBox);
        sizePanel.add(Box.createVerticalStrut(45));
        sizePanel.add(startButton);
        sizePanel.add(Box.createVerticalStrut(33));
        sizePanel.add(exitButton);

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
        infoPanel.setBackground(new Color(135, 206, 250));
        infoPanel.setPreferredSize(new Dimension(250, 0));

        JLabel timeTitleLabel = new JLabel("Time Taken");
        timeTitleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timeTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        timeLabel = new JLabel("00 m : 00 s");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        shuffleCountLabel = new JLabel("Number of Shuffles: " + shuffleCount);
        shuffleCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        moveCountLabel = new JLabel("Move : 0");
        moveCountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        moveCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension buttonSize = new Dimension(120, 40);

        JButton resetButton = createInfoButton("Reset", buttonSize, e -> reset());
        JButton pauseButton = createInfoButton("Pause", buttonSize, e -> pause());
        JButton resumeButton = createInfoButton("Resume", buttonSize, e -> resume());
        JButton backButton = createInfoButton("Back", buttonSize, e -> back());

        infoPanel.add(timeTitleLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(timeLabel);
        infoPanel.add(Box.createVerticalStrut(30));
        infoPanel.add(moveCountLabel);
        infoPanel.add(Box.createVerticalStrut(60));

        infoPanel.add(resetButton);
        infoPanel.add(Box.createVerticalStrut(35));
        infoPanel.add(pauseButton);
        infoPanel.add(Box.createVerticalStrut(35));
        infoPanel.add(resumeButton);
        infoPanel.add(Box.createVerticalStrut(35));
        infoPanel.add(backButton);

        puzzlePanel.add(infoPanel, BorderLayout.EAST);
        initializeButtons(gridPanel);
        revalidate();
        repaint();

        timerStarted = false;
        if (timerThread != null) {
            timerThread.stopTimer();
        }
        timerThread = new TimerThread(timeLabel,moveCountLabel);
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
        splitImage();
        labels = new JLabel[gridSize * gridSize];
        // buttons = new JButton[gridSize * gridSize];
        buttonLabels = new ArrayList<>();

        // for (int i = 0; i < gridSize * gridSize - 1; i++) {
        //     buttonLabels.add(String.valueOf(i));
        // }
        // buttonLabels.add("");

        // Collections.shuffle(buttonLabels);
        // for (int i = 0; i < buttons.length; i++) {
        //     buttons[i] = new JButton(buttonLabels.get(i));
        //     buttons[i].setFont(new Font("Arial", Font.BOLD, Math.max(30, 60 / gridSize)));
        //     buttons[i].addActionListener(new ButtonListener());
        //     gridPanel.add(buttons[i]);
        // }

        for (int i = 0; i < gridSize * gridSize - 1; i++) {
            buttonLabels.add(String.valueOf(i));
        }
        buttonLabels.add("");

        Collections.shuffle(buttonLabels);
        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel();
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
            label.setOpaque(true);
            if (!buttonLabels.get(i).equals("")) {
                int pieceIndex = Integer.parseInt(buttonLabels.get(i));
                label.setIcon(new ImageIcon(imagePieces[pieceIndex]));
            } else {
                label.setBackground(Color.WHITE);
            }
            label.addMouseListener(new LabelClickListener(i));
            gridPanel.add(label);
            labels[i] = label;
        }
    }

    @Override
    public void StartGame(){
        int selectedLevel = lvlComboBox.getSelectedIndex() + 1;
        gridSize = selectedLevel + 1;
        loadLevelImage(selectedLevel);
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
            timerStarted = false;
            if (timeTitleLabel != null) {
            timeTitleLabel.setText("Time Taken");
           }
            timerStarted = false;
            timeLabel.setText("00 m : 00 s");
            moveCountLabel.setText("Move : 0");
            timerThread = new TimerThread(timeLabel, moveCountLabel);
            resetPuzzle();
        }
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
    public void back() {
        if (timerThread != null) {
            timerThread.stopTimer();
        }
        timerStarted = false;
        shuffleCount = 0;

        puzzlePanel.removeAll();
        showLevelSelection();
        revalidate();
        repaint();
    }

    @Override
    public void exit() {
        Object[] options = {"Ya", "Tidak"};
        int confirm = JOptionPane.showOptionDialog(
            this,
            "Apakah Anda Ingin Keluar Dari Permainan?",
            "Exit Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );

        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }

    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizeImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizeImage.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();

        return resizeImage;
    }

    private void loadLevelImage(int level) {
        try {
            BufferedImage originalImage = ImageIO.read(new File("src/images/level" + level + ".jpg"));
            // BufferedImage croppedImage = cropToSquere(originalImage);
            // int targetSize = 400;
            fullImage = resizeImage(originalImage, 730, 558);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load image for level " + level);
            e.printStackTrace();
        }
    }

    private void splitImage() {
        int pieceWidht = fullImage.getWidth() / gridSize;
        int pieceHeight = fullImage.getHeight() / gridSize;
        imagePieces = new BufferedImage[gridSize * gridSize];

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                int index = row * gridSize + col;
                if (index == gridSize * gridSize - 1) {
                    imagePieces[index] = null;
                } else {
                    imagePieces[index] = fullImage.getSubimage(
                        col * pieceWidht, row * pieceHeight, pieceWidht, pieceHeight);
                }
            }
        }
    }

    private void resetPuzzle() {
        Collections.shuffle(buttonLabels);
        // for (int i = 0; i < buttons.length; i++) {
        //     buttons[i].setText(buttonLabels.get(i));
        // }
        // shuffleCount++;
        // shuffleCountLabel.setText("Number of Shuffles: " + shuffleCount);
        // if (timerThread != null) {
        //     timerThread.resetTimer();
        // }
        for (int i = 0; i < labels.length; i++) {
            JLabel label = labels[i];

            if (!buttonLabels.get(i).equals("")) {
                int pieceIndex = Integer.parseInt(buttonLabels.get(i));
                label.setIcon(new ImageIcon(imagePieces[pieceIndex]));
                label.setBackground(null);
            } else {
                label.setIcon(null);
                label.setBackground(Color.WHITE);
            }
        }
        shuffleCount++;
        shuffleCountLabel.setText("Number of shuffle : " + shuffleCount);

        if (timerThread != null) {
            timerThread.resetTimer();
        }
    }

    // private void swapButtons(int emptyIndex, int clickedIndex) {
    //     String temp = buttons[emptyIndex].getText();
    //     buttons[emptyIndex].setText(buttons[clickedIndex].getText());
    //     buttons[clickedIndex].setText(temp);
    // }

    private void swapLabels(int emptyIndex, int clickedIndex) {
        ImageIcon tempIcon = (ImageIcon) labels[emptyIndex].getIcon();
        labels[emptyIndex].setIcon(labels[clickedIndex].getIcon());
        labels[clickedIndex].setIcon(tempIcon);

        labels[emptyIndex].setBackground(Color.LIGHT_GRAY);
        labels[clickedIndex].setBackground(null);
    }

    private boolean isSolved() {
        // for (int i = 0; i < buttonLabels.size() - 1; i++) {
        //     if (!buttons[i].getText().equals(String.valueOf(i))) {
        //         return false;
        //     }
        // }

        // if (!buttons[buttons.length - 1].getText().equals("")) {
        //     return false;
        // }
        for (int i = 0; i < gridSize * gridSize - 1; i++) {
            if (labels[i].getIcon() == null || ((ImageIcon) labels[i].getIcon()).getImage() != imagePieces[i]) {
                return false;
            }
        }

        if (labels[labels.length - 1].getIcon() != null) {
            return false;
        }

        if (timerThread != null) {
            timerThread.stopTimer();
        }

        if (gridSize >= 5) {
            JPanel finalDialogPanel = new JPanel();
            finalDialogPanel.setLayout(new BoxLayout(finalDialogPanel, BoxLayout.Y_AXIS));
            finalDialogPanel.setBackground(new Color(255, 255, 255));
            finalDialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel messageLabel = new JLabel("Selamat Anda Telah Menyelesaikan Puzzel Level Terakhir !!!");
            messageLabel.setFont(new Font("Arial", Font.BOLD, 20));
            messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel instructionLabel = new JLabel("Apa Yang Ingin Anda Lakukan Selanjutnya ?");
            instructionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            finalDialogPanel.add(messageLabel);
            finalDialogPanel.add(Box.createVerticalStrut(15));
            finalDialogPanel.add(instructionLabel);

            Object[] options = {"Restart", "Exit"};
            int choice = JOptionPane.showOptionDialog(
                this,
                finalDialogPanel,
                "Game Completed",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

            if (choice == JOptionPane.YES_OPTION) {
                gridSize = 2;
                initializeGame();
            } else {
                System.exit(0);
            }
        } else {
            JPanel completeDialogPanel = new JPanel();
            completeDialogPanel.setLayout(new BoxLayout(completeDialogPanel, BoxLayout.Y_AXIS));
            completeDialogPanel.setBackground(new Color(255, 255, 255));
            completeDialogPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

            JLabel messageLabel = new JLabel("Selamat Anda telah menyelesaikan Puzzel Level " + (gridSize - 1) + "!");
            messageLabel.setFont(new Font("Arial", Font.BOLD, 20));
            messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel intructionLabel = new JLabel("Apakah Anda Ingin Melanjutkan ke Level Berikutnya ?");
            intructionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            intructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            completeDialogPanel.add(messageLabel);
            completeDialogPanel.add(Box.createVerticalStrut(35));
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
                loadLevelImage(gridSize - 1);
                initializeGame();
            } else {
                loadLevelImage(gridSize - 1);
                initializeGame();
            }
        }

        return true;
    }

    private class LabelClickListener extends MouseAdapter {
        private final int index;

        LabelClickListener(int index) {
            this.index = index;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int emptyIndex = -1;

            if (!timerStarted) {
                timerThread.start();
                timerStarted = true;
            }
            for (int i = 0; i < labels.length; i++) {
                if (labels[i].getIcon() == null) {
                    emptyIndex = i;
                    break;
                }
            }

            if ((index == emptyIndex - 1 && index % gridSize != gridSize - 1)
                    || (index == emptyIndex + 1 && index % gridSize != 0)
                    || index == emptyIndex - gridSize
                    || index == emptyIndex + gridSize) {
                swapLabels(emptyIndex, index);
                timerThread.incrementMoveCount();
                // if  (isSolved()) {
                //     JOptionPane.showMessageDialog(PuzzleGame.this, "Puzzle Solved!");
                // }
            }

            isSolved();
        }
        
    }

    // private class ButtonListener implements ActionListener {
    //     @Override
    //     public void actionPerformed(ActionEvent e) {
    //         if (!timerStarted) {
    //             timerThread.start();
    //             timerStarted = true;
    //         }

    //         JButton clickedButton = (JButton) e.getSource();
    //         int clickedIndex = -1, emptyIndex = -1;

    //         for (int i = 0; i < buttons.length; i++) {
    //             if (buttons[i] == clickedButton) {
    //                 clickedIndex = i;
    //             }
    //             if (buttons[i].getText().equals("")) {
    //                 emptyIndex = i;
    //             }
    //         }

    //         if ((clickedIndex == emptyIndex - 1 && clickedIndex % gridSize != gridSize - 1)
    //                 || (clickedIndex == emptyIndex + 1 && clickedIndex % gridSize != 0)
    //                 || clickedIndex == emptyIndex - gridSize
    //                 || clickedIndex == emptyIndex + gridSize) {
    //             swapButtons(emptyIndex, clickedIndex);
    //             timerThread.incrementMoveCount();
    //         }

    //         isSolved();
    //     }
    // }

    public static void main(String[] args) {
        PuzzelGame game = new PuzzelGame();
        game.setVisible(true);

    }
}
