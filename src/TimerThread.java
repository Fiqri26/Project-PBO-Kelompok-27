import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class TimerThread extends Thread {
    private int elapsedTimeInSeconds = 0;
    private int moveCount = 0;
    private boolean running = true;
    private boolean paused = false;
    private JLabel timeLabel;
    private JLabel moveLabel;

    public TimerThread(JLabel timeLabel, JLabel moveLabel) {
        this.timeLabel = timeLabel;
        this.moveLabel = moveLabel;
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

                int minutes = elapsedTimeInSeconds / 60;
                int seconds = elapsedTimeInSeconds % 60;

                SwingUtilities.invokeLater(() -> {
                    timeLabel.setText(String.format("%02d m : %02d s", minutes, seconds));
                });
            } catch (InterruptedException e) {
                if (!running) {
                    break;
                }
                Thread.currentThread().interrupt();
            }
        }
    }

    public void incrementMoveCount() {
        moveCount++;
        SwingUtilities.invokeLater(() -> moveLabel.setText("Moves : " + moveCount));
    }

    public void stopTimer() {
        running = false;
        interrupt();
    }

    public void resetTimer() {
        elapsedTimeInSeconds = 0;
        SwingUtilities.invokeLater(() -> timeLabel.setText("00 m : 00 s"));
    }

    public void pauseTimer() {
        synchronized (this) {
            paused = true;
        }
    }

    public synchronized void resumeTimer() {
        paused = false;
        notify();
    }
}
