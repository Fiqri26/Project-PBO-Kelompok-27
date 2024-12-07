import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Beranda extends JFrame {

    public Beranda() {
        TampilanBeranda();
    }

    private void TampilanBeranda() {
        setTitle("Pitczzle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setResizable(true);
        setLocationRelativeTo(null);

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
        buttonPanel.setOpaque(false); // buat transparant
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));

        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 18));
        startButton.setBackground(new Color(219, 112, 147));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBounds(600, 600, 120, 40);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PuzzleGame puzzlegame = new PuzzleGame();
                puzzlegame.setVisible(true);
                dispose();

            }
        });
        // backgroundPanel.add(startButton);
        buttonPanel.add(startButton);

        JButton guideButton = new JButton("How to Play");
        guideButton.setFont(new Font("Arial", Font.BOLD, 18));
        guideButton.setBackground(new Color(147, 112, 219));
        guideButton.setForeground(Color.WHITE);
        guideButton.setFocusPainted(false);
        guideButton.setBounds(800, 600, 150, 40);
        guideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                    "How to Play :\n" +
                        "1. Klik tombol 'Start' untuk memulai permainan.\n" +
                        "2. Permainan dimulai dengan puzzle gambar pertama.\n" +
                        "3. Susun potongan puzzle hingga membentuk gambar yang sempurna.\n" +
                        "4. Setelah menyelesaikan satu puzzle, permainan akan otomatis berpindah ke gambar berikutnya.\n" +
                        "5. Selesaikan semua 5 puzzle gambar untuk menang!\n\n" +
                        "Kontrol tambahan :\n" +
                        "- Reset: Memulai permainan dari awal.\n" +
                        "- Exit: Keluar dari permainan.\n" +
                        "- Resume: Melanjutkan permainan yang dijeda.\n" +
                        "- Pause: Menjeda permainan sementara.\n\n" +
                        "Selamat bermain dan bersenang-senang !",
                    "How to Play",
                JOptionPane.INFORMATION_MESSAGE);
            }
        });
        // backgroundPanel.add(guideButton);
        buttonPanel.add(guideButton);

        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        new Beranda().setVisible(true);
    }
}
