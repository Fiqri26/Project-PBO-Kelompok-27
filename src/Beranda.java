import javax.swing.*;
import java.awt.*;

public class Beranda extends GameFrame {

    public Beranda() {
        super("Pictzzle");
        TampilanBeranda();
    }

    private void TampilanBeranda() {
        JPanel backgroundPanel = new JPanel() {
            private Image backgroundImage = new ImageIcon(getClass().getResource("images/background.jpg")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));

        JButton startButton = new JButton("Play");
        startButton.setFont(new Font("Arial", Font.BOLD, 18));
        startButton.setBackground(new Color(219, 112, 147));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBounds(600, 600, 120, 55);
        startButton.addActionListener(e -> StartGame());
        buttonPanel.add(startButton);

        JButton guideButton = new JButton("How to Play");
        guideButton.setFont(new Font("Arial", Font.BOLD, 18));
        guideButton.setBackground(new Color(147, 112, 219));
        guideButton.setForeground(Color.WHITE);
        guideButton.setFocusPainted(false);
        guideButton.setBounds(800, 600, 150, 55);
        guideButton.addActionListener(e -> JOptionPane.showMessageDialog(
                null,
                "How to Play :\n" +
                        "1. Klik tombol 'Start' untuk memulai permainan.\n" +
                        "2. Pemain dapat bebas memilih level puzzle.\n" +
                        "3. Susun potongan puzzle hingga membentuk gambar yang sempurna.\n" +
                        "4. Setelah menyelesaikan satu puzzle, permainan akan otomatis berpindah ke level berikutnya.\n" +
                        "5. Selesaikan semua 5 puzzle gambar untuk menang!\n\n" +
                        "Tombol Kontrol Tambahan Permainan :\n" +
                        "- Reset : Memulai permainan dari awal.\n" +
                        "- Exit : Keluar dari permainan.\n" +
                        "- Resume : Melanjutkan permainan yang dijeda.\n" +
                        "- Pause : Menjeda permainan sementara.\n\n" +
                        "Selamat bermain dan bersenang-senang !",
                "How to Play",
                JOptionPane.INFORMATION_MESSAGE
        ));

        buttonPanel.add(guideButton);

        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void StartGame() {
        new PuzzleGame().setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        Beranda beranda = new Beranda();
        beranda.setVisible(true);
    }
}
