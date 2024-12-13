import javax.swing.JLabel;
import javax.swing.SwingUtilities;

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

                int minutes = elapsedTimeInSeconds / 60;
                int seconds = elapsedTimeInSeconds % 60;

                // Update label waktu secara aman di thread UI
                SwingUtilities.invokeLater(() ->
                    timeLabel.setText(String.format("%02d m : %02d s", minutes, seconds))
                );
            } catch (InterruptedException e) {
                if (!running) {
                    break;
                }
                Thread.currentThread().interrupt();
            }
        }
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


