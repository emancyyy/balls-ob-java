package game;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements MouseMotionListener {
    // Размеры и начальная позиция шарика игрока
    private int ballDiameter = 25; // Начальный размер игрока
    private int playerX = 375; // Центр окна
    private int playerY = 275;

    // Список врагов
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private Random random = new Random();

    // Максимальное количество врагов
    private int maxEnemies = 50;

    

    // Состояние игры
    boolean gameOver = false;

    public GamePanel() {
        setBackground(new Color(40, 40, 40)); // Темно-серый фон
        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Включаем антиалиасинг для сглаживания
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Рисуем игрока с обводкой
        drawBall(g2d, playerX, playerY, ballDiameter, Color.WHITE);

        // Рисуем всех врагов
        for (Enemy enemy : enemies) {
            drawBall(g2d, enemy.getX(), enemy.getY(), enemy.getSize(), enemy.getColor());
        }

        // Если игра окончена, выводим экран смерти
        if (gameOver) {
            g2d.setColor(Color.RED);
            g2d.drawString("Game Over!", getWidth() / 2 - 50, getHeight() / 2 - 50);
            g2d.setColor(Color.WHITE);
            g2d.drawString("Press 'R' to Restart or 'Q' to Quit", getWidth() / 2 - 100, getHeight() / 2);
        }
    }

    // Метод для рисования шара с обводкой
    private void drawBall(Graphics2D g, int x, int y, int diameter, Color color) {
        // Рисуем обводку чуть светлее основного цвета
        Color borderColor = color.brighter();
        g.setColor(borderColor);
        g.fillOval(x - 2, y - 2, diameter + 4, diameter + 4); // Обводка

        // Рисуем сам шар
        g.setColor(color);
        g.fillOval(x, y, diameter, diameter); // Основной шар
    }

    // Обновление логики игры
    public void updateGame() {
        if (gameOver) return; // Если игра окончена, не обновляем

        // Генерация нового врага только если их меньше максимального числа
        if (enemies.size() < maxEnemies) {
            spawnEnemy();
        }

        // Обновляем размеры врагов
        for (Enemy enemy : enemies) {
            enemy.grow();
        }

        // Проверка столкновений
        checkCollisions();

        // Перерисовываем экран
        repaint();
    }

    private void spawnEnemy() {
        int initialSize = random.nextInt(25) + 5;
        int growthRate = 1;
        Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));

        int x, y;
        boolean overlapping;

        do {
            x = random.nextInt(getWidth());
            y = random.nextInt(getHeight());
            overlapping = false;

            for (Enemy enemy : enemies) {
                double distance = Math.sqrt(Math.pow(x - enemy.getX(), 2) + Math.pow(y - enemy.getY(), 2));
                if (distance < enemy.getSize() / 2 + initialSize / 2) {
                    overlapping = true;
                    break;
                }
            }
        } while (overlapping);

        enemies.add(new Enemy(x, y, initialSize, color, growthRate));
    }

    // Проверка столкновений
    private void checkCollisions() {
        // Размер игрока
        int playerRadius = ballDiameter / 2;

        // Проходим по всем врагам и проверяем столкновения
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            int enemyRadius = enemy.getSize() / 2;

            // Вычисляем расстояние между центрами игрока и врага
            int dx = playerX + playerRadius - (enemy.getX() + enemyRadius);
            int dy = playerY + playerRadius - (enemy.getY() + enemyRadius);
            double distance = Math.sqrt(dx * dx + dy * dy);

            // Если игрок и враг столкнулись
            if (distance < playerRadius + enemyRadius) {
                if (playerRadius > enemyRadius) {
                    // Игрок съедает врага, увеличиваем размер игрока
                    ballDiameter += 3;
                    enemies.remove(i); // Удаляем врага из списка
                    i--; // Сдвигаем индекс, чтобы не пропустить следующий элемент
                } else {
                    // Если враг больше игрока, игра заканчивается
                    gameOver = true;
                    break;
                }
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        playerX = e.getX() - ballDiameter / 2;
        playerY = e.getY() - ballDiameter / 2;
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    // Метод для перезапуска игры
    public void restartGame() {
        gameOver = false;
        enemies.clear();
        ballDiameter = 25;
        playerX = 375;
        playerY = 275;
        repaint();
    }
}
