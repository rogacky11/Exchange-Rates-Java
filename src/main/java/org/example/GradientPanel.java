package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class GradientPanel extends JPanel {
    private Color startColor;
    private Color endColor;
    private boolean reverse;
    private boolean horizontal;

    public GradientPanel(Color startColor, Color endColor, boolean reverse, boolean horizontal) {
        this.startColor = startColor;
        this.endColor = endColor;
        this.reverse = reverse;
        this.horizontal = horizontal;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Definiowanie kierunku gradientu
        Point2D start;
        Point2D end;

        if (horizontal) {
            if (!reverse) {
                start = new Point2D.Float(0, 0); // Początek od lewej
                end = new Point2D.Float(panelWidth, 0); // Koniec na prawo
            } else {
                start = new Point2D.Float(panelWidth, 0); // Początek na prawo
                end = new Point2D.Float(0, 0); // Koniec na lewo
            }
        } else { // pionowy
            if (!reverse) {
                start = new Point2D.Float(0, 0); // Początek od góry
                end = new Point2D.Float(0, panelHeight); // Koniec na dole
            } else {
                start = new Point2D.Float(0, panelHeight); // Początek na dole
                end = new Point2D.Float(0, 0); // Koniec na górze
            }
        }

        // Ustawienie kolorów gradientu
        GradientPaint gradient = new GradientPaint(start, startColor, end, endColor);

        // Rysowanie gradientu na tle panelu
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, panelWidth, panelHeight);

        g2d.dispose();
    }
}
