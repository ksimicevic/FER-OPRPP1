package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class BarChartDemo extends JFrame {
    private BarChart chart;

    public BarChartDemo(BarChart chart) {
        super();
        this.chart = chart;
        initGUI();
    }

    private void initGUI() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Bar chart");
        setSize(750, 750);

        Container container = getContentPane();
        container.add(new BarChartComponent(chart));
    }


    public static void main(String... args) throws FileNotFoundException {
        if (args.length != 1) {
            throw new IllegalStateException("Only one argument is required.");
        }

        String path = args[0];

        Scanner sc = new Scanner(new File(path));
        String xDescription = sc.nextLine();
        String yDescription = sc.nextLine();

        LinkedList<XYValue> valueList = new LinkedList<>();

        String[] values = sc.nextLine().split("\\s+");

        for (String value : values) {
            String[] valueSplit = value.split(",");
            try {
                valueList.add(new XYValue(Integer.parseInt(valueSplit[0].trim()), Integer.parseInt(valueSplit[1].trim())));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Invalid value");
            }
        }

        int minY, maxY, differenceY;

        try {
            minY = Integer.parseInt(sc.nextLine().trim());
            maxY = Integer.parseInt(sc.nextLine().trim());
            differenceY = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Unable to parse to number");

        }

        SwingUtilities.invokeLater(() -> {
            BarChartDemo demo = new BarChartDemo(new BarChart(valueList, xDescription, yDescription, minY, maxY, differenceY));
            demo.setVisible(true);
        });

    }
}
