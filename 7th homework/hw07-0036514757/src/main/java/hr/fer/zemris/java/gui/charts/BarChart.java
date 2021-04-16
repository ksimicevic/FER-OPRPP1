package hr.fer.zemris.java.gui.charts;

import java.util.List;

public class BarChart {
    private List<XYValue> list;
    private String xDescription, yDescription;
    private int minY, maxY, differenceY;

    public BarChart(List<XYValue> list, String xDescription, String yDescription, int minY, int maxY, int differenceY) {

        boolean test = list.stream().map(XYValue::getY).allMatch(x -> x >= minY);

        if (test) {
            this.list = list;
        } else {
            throw new IllegalArgumentException("All Y values in the list must be greater than minY");
        }

        this.xDescription = xDescription;
        this.yDescription = yDescription;

        if (minY >= 0) {
            this.minY = minY;
        } else {
            throw new IllegalArgumentException("Min y cannot be negative");
        }

        if (maxY > minY) {
            this.maxY = maxY;
        } else {
            throw new IllegalArgumentException("Max y isn't larger than min y");

        }

        this.differenceY = differenceY;
    }

    public List<XYValue> getList() {
        return list;
    }

    public String getxDescription() {
        return xDescription;
    }

    public String getyDescription() {
        return yDescription;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getDifferenceY() {
        return differenceY;
    }
}
