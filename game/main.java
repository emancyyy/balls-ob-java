package game;
import javax.swing.JFrame;
import java.awt.CardLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Growing Balls Game");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        GamePanel gamePanel = new GamePanel();
        MenuPanel menuPanel = new MenuPanel(gamePanel);

        frame.add(gamePanel);
        frame.add(menuPanel);
        gamePanel.setVisible(false); // Изначально показываем меню
        frame.setLayout(new CardLayout());
        frame.setVisible(true);

        // Обработчик нажатий клавиш для перезапуска игры
        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_R && gamePanel.gameOver) {
                    gamePanel.restartGame();
                }
                if (e.getKeyCode() == KeyEvent.VK_Q && gamePanel.gameOver) {
                    System.exit(0);
                }
            }
        });
        gamePanel.setFocusable(true); // Для того, чтобы обработка клавиш работала

        // Игровой цикл
        while (true) {
            gamePanel.updateGame(); // Обновляем состояние игры
            try {
                Thread.sleep(60); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}