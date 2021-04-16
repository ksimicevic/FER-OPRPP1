package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Class responsible for drawing the bar chart.
 */
public class BarChartComponent extends JComponent {
    private final BarChart chart;

    public BarChartComponent(BarChart chart) {
        this.chart = chart;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        Dimension dimension = getSize();
        Insets insets = getInsets();
        FontMetrics fontMetrics = g2d.getFontMetrics();

        java.util.List<XYValue> values = chart.getList();

        int maxY = chart.getMaxY();
        int minY = chart.getMinY();
        int differenceY = chart.getDifferenceY();

        int yNumberWidth = fontMetrics.stringWidth(Integer.toString(chart.getMaxY())); //width of each Y
        int emptySpace = 20; //empty space
        int distanceFromDescription = 15; //distance from Y description to Y values or X description to X values
        int distanceBetweenValueAndAxis = 10; //distance between each value and axis
        int dashLength = 3;

        int originXCoordinate = emptySpace + fontMetrics.getHeight() + distanceFromDescription + yNumberWidth + distanceBetweenValueAndAxis + dashLength;
        int originYCoordinate = dimension.height - insets.bottom - emptySpace - fontMetrics.getHeight() - distanceBetweenValueAndAxis - dashLength - fontMetrics.getHeight() / 2;
        int endXaxis = dimension.width - insets.right - emptySpace;
        int endYaxis = insets.top + emptySpace;

        int yAxisLength = originYCoordinate - endYaxis;
        int xAxisLength = endXaxis - originXCoordinate;

        int rowCount = (maxY - minY) / differenceY;
        int columnWidth = xAxisLength / values.size();
        if ((maxY - minY) % differenceY != 0) {
            rowCount++;
        }

        int rowHeight = yAxisLength / rowCount;
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.setColor(Color.gray);
        g2d.drawLine(originXCoordinate, originYCoordinate, endXaxis, originYCoordinate);

        g2d.setStroke(new BasicStroke(1.5f));
        g2d.setColor(Color.gray);
        g2d.drawLine(originXCoordinate, originYCoordinate, originXCoordinate, endYaxis);


        for (int i = minY; i <= minY + rowCount; i++) {
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.setColor(Color.gray);
            g2d.drawLine(originXCoordinate - dashLength, originYCoordinate - (i * rowHeight), originXCoordinate, originYCoordinate - (i * rowHeight));

            g2d.setColor(Color.black);
            g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));
            g2d.drawString(String.valueOf(i * differenceY + minY), originXCoordinate - dashLength - distanceBetweenValueAndAxis - fontMetrics.stringWidth(String.valueOf(i * differenceY + minY)), originYCoordinate - (i * rowHeight) + fontMetrics.getHeight() / 4);

            if (i != minY) {
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.setColor(Color.pink);
                g2d.drawLine(originXCoordinate + 2, originYCoordinate - i * rowHeight, endXaxis, originYCoordinate - i * rowHeight);
            }
        }

        for (int i = 0; i < values.size(); i++) {
            int yValue = values.get(i).getY();

            if (yValue > maxY) {
                yValue = maxY;
            }

            int yHeight = (yValue - minY) * rowHeight / differenceY;

            g2d.setColor(new Color(235, 100, 52));
            g2d.setBackground(Color.orange);
            g2d.fillRect(originXCoordinate + i * columnWidth, originYCoordinate - yHeight, columnWidth, yHeight + 1);

            g2d.setStroke(new BasicStroke(1.5f));
            g2d.setColor(Color.gray);
            g2d.drawLine(originXCoordinate + i * columnWidth, originYCoordinate, originXCoordinate + i * columnWidth, originYCoordinate + dashLength);

            g2d.setColor(Color.black);
            g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));
            g2d.drawString(String.valueOf(values.get(i).getX()), originXCoordinate + (i * columnWidth) + columnWidth / 2, originYCoordinate + distanceBetweenValueAndAxis + emptySpace / 2);

            if (i != 0) {
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.setColor(Color.pink);
                g2d.drawLine(originXCoordinate + i * columnWidth, originYCoordinate - 2, originXCoordinate + i * columnWidth, endYaxis);
            }
        }

        g2d.setColor(Color.gray);
        int triangleLength = 5;
        int[] triangleX1 = {endXaxis, endXaxis - triangleLength, endXaxis - triangleLength};
        int[] triangleY1 = {originYCoordinate, originYCoordinate - triangleLength, originYCoordinate + triangleLength};
        int number = 3;
        g2d.fillPolygon(triangleX1, triangleY1, number);

        int[] triangleX2 = {originXCoordinate, originXCoordinate - triangleLength, originXCoordinate + triangleLength};
        int[] triangleY2 = {endYaxis, endYaxis + triangleLength, endYaxis + triangleLength};
        g2d.fillPolygon(triangleX2, triangleY2, number);

        g2d.setColor(Color.black);
        g2d.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
        g2d.drawString(chart.getxDescription(), xAxisLength / 2 + originXCoordinate - fontMetrics.stringWidth(chart.getxDescription()) / 2, originYCoordinate + distanceBetweenValueAndAxis + distanceFromDescription + dashLength + fontMetrics.getHeight() / 2);

        AffineTransform at = AffineTransform.getQuadrantRotateInstance(3);
        g2d.setTransform(at);
        g2d.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
        g2d.drawString(chart.getyDescription(), -(originYCoordinate - yAxisLength / 2 + fontMetrics.stringWidth(chart.getyDescription()) * 2), originXCoordinate - dashLength - fontMetrics.getHeight() - emptySpace / 2);
        g2d.setTransform(AffineTransform.getQuadrantRotateInstance(0));

    }
}
