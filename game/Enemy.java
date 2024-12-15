package game;
import java.awt.Color;
import java.awt.Graphics;

public class Enemy {
    private int x, y; // Позиция врага
    private int size;  // Размер врага
    private Color color; // Цвет врага
    private int growthRate; // Скорость роста

    public Enemy(int x, int y, int size, Color color, int growthRate) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
        this.growthRate = growthRate;
    }

    // Рисуем врага
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, size, size);
    }

    // Увеличиваем размер врага
    public void grow() {
        size += growthRate;
    }

    // Получаем размер врага
    public int getSize() {
        return size;
    }

    // Получаем координаты X и Y врага
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Получаем цвет врага
    public Color getColor() {
        return color;
    }
}
