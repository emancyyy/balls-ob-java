package game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MenuPanel extends JPanel {
    private JButton startButton;
    private JButton quitButton;
    private GamePanel gamePanel;

    public MenuPanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        // Устанавливаем layout для центрирования кнопок
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Отступы между кнопками
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Кнопка "Начать игру"
        startButton = createStyledButton("Start Game");
        startButton.addActionListener(e -> {
            // Переключаем на игровой экран
            gamePanel.setVisible(true);
            MenuPanel.this.setVisible(false);
            gamePanel.requestFocusInWindow(); // Запрос фокуса для игры
        });

        // Кнопка "Выход"
        quitButton = createStyledButton("Quit");
        quitButton.addActionListener(e -> System.exit(0));

        // Добавляем кнопки на панель
        add(startButton, gbc);
        gbc.gridy = 1; // Переходим ко второй строке
        add(quitButton, gbc);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }

            @Override
            public void setContentAreaFilled(boolean b) {
                // Отключаем стандартную заливку
            }
        };

        button.setFont(new Font("SansSerif", Font.BOLD, 22));
        button.setBackground(new Color(120, 120, 120));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        button.setOpaque(true);

        // Добавляем эффект затемнения при наведении
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(button.getBackground().darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(120, 120, 120));
            }
        });

        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Включаем антиалиасинг для сглаживания
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Рисуем фоновый рисунок - цветные круги
        drawBackground(g2d);
    }

    private void drawBackground(Graphics2D g) {
        g.setColor(new Color(40, 40, 40)); // Темно-серый фон
        g.fillRect(0, 0, getWidth(), getHeight());

        // Рисуем цветные кружки на фоне с учетом расстояния между ними
        List<Point> centers = new ArrayList<>();
        int minDistance = 80; // Минимальное расстояние между центрами шаров
        int maxAttempts = 100; // Максимальное количество попыток для поиска позиции

        Random rand = new Random();
        for (int i = 0; i < 30; i++) {
            int attempts = 0;
            Point newCenter;
            boolean validPosition;
            int ballDiameter = rand.nextInt(50) + 30; // Размер шаров от 30 до 80 пикселей

            do {
                int x = rand.nextInt(getWidth() - ballDiameter);
                int y = rand.nextInt(getHeight() - ballDiameter);
                newCenter = new Point(x + ballDiameter / 2, y + ballDiameter / 2);
                validPosition = true;

                // Проверяем расстояние до уже существующих центров
                for (Point center : centers) {
                    if (newCenter.distance(center) < minDistance) {
                        validPosition = false;
                        break;
                    }
                }

                attempts++;
            } while (!validPosition && attempts < maxAttempts);

            if (validPosition) {
                centers.add(newCenter);
                Color ballColor = new Color(
                        rand.nextInt(256),
                        rand.nextInt(256),
                        rand.nextInt(256)
                );
                drawBall(g, newCenter.x - ballDiameter / 2, newCenter.y - ballDiameter / 2, ballDiameter, ballColor);
            }
        }
    }

    private void drawBall(Graphics2D g, int x, int y, int diameter, Color color) {
        // Рисуем обводку чуть светлее основного цвета
        Color borderColor = color.brighter();
        g.setColor(borderColor);
        g.fillOval(x - 2, y - 2, diameter + 4, diameter + 4); // Обводка

        // Рисуем сам шар
        g.setColor(color);
        g.fillOval(x, y, diameter, diameter); // Основной шар
    }
}
