import javax.swing.JFrame;

abstract class GameFrame extends JFrame{
    public GameFrame(String namaFrame){
        super(namaFrame);
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    abstract void StartGame();
}
