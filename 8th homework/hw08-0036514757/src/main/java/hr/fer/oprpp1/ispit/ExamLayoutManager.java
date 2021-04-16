package hr.fer.oprpp1.ispit;

import java.awt.*;
import java.util.ArrayList;

public class ExamLayoutManager implements LayoutManager2 {
    private double percentage;
    private java.util.List<Component> components;

    public ExamLayoutManager(int percentage) {
        if (percentage < 10 || percentage > 90) {
            throw new IllegalArgumentException("Percentage needs to be in interval [10, 90]");
        }
        this.percentage = percentage / 100.0;
        components = new ArrayList<>(3);
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage / 100.0;
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        ExamLayoutManagerConstraint constraint = (ExamLayoutManagerConstraint) constraints;

        switch (constraint) {
            case AREA1 -> components.add(0, comp);
            case AREA2 -> components.add(1, comp);
            case AREA3 -> components.add(2, comp);
            default -> throw new IllegalArgumentException("Illegal constraint.");
        }

    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return null;
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    @Override
    public void invalidateLayout(Container target) {

    }

    @Override
    public void addLayoutComponent(String name, Component comp) {

    }

    @Override
    public void removeLayoutComponent(Component comp) {

    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return new Dimension(700, 250);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(parent.getWidth(), parent.getHeight());
    }

    @Override
    public void layoutContainer(Container parent) {
        double parentHeight = parent.getHeight();
        double parentWidth = parent.getWidth();

        for (int i = 0; i < components.size(); i++) {
            switch (i) {
                case 0 -> {
                    if (components.get(i) != null) {
                        components.get(i).setBounds(0, 0,
                                (int) Math.ceil(parentWidth), (int) Math.ceil(parentHeight * percentage));
                    }
                }
                case 1 -> {
                    if (components.get(i) != null) {
                        components.get(i).setBounds(0, (int) Math.ceil(parentHeight * percentage),
                                (int) Math.ceil(parentWidth * (percentage)), (int) Math.ceil(parentHeight * (1 - percentage)));
                    }
                }
                case 2 -> {
                    if (components.get(i) != null)
                        components.get(i).setBounds((int) Math.ceil(parentWidth * percentage), (int) Math.ceil(parentHeight * percentage),
                                (int) Math.ceil(parentWidth * (1 - percentage)), (int) Math.ceil(parentHeight * (1 - percentage)));
                }
            }
        }
    }
}
