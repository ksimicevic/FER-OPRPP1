package hr.fer.zemris.java.gui.layouts;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Class representing a layout for calculator.
 */
public class CalcLayout implements LayoutManager2 {

    private HashMap<RCPosition, Component> elements;

    private int spaceBetweenElements;

    public CalcLayout(int spaceBetweenElements) {
        this.elements = new HashMap<>();
        this.spaceBetweenElements = spaceBetweenElements;
    }

    public CalcLayout() {
        this(0);
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        RCPosition constraint;

        if (comp == null || constraints == null) {
            throw new NullPointerException("Cannot work with null values here.");
        }

        if (constraints instanceof String) {
            constraint = RCPosition.parse((String) constraints);
        } else if (constraints instanceof RCPosition) {
            constraint = (RCPosition) constraints;
        } else {
            throw new IllegalArgumentException("Invalid constraint. Valid constraint is either of class String or RCPosition.");
        }

        int row = constraint.getRow();
        int column = constraint.getColumn();


        if (row < 1 || row > 5) {
            throw new CalcLayoutException("Row " + row + " is out of bounds.");
        }

        if (column < 1 || column > 7) {
            throw new CalcLayoutException("Column " + column + " is out of bounds.");
        }

        if (row == 1) {
            if (column >= 2 && column <= 5) {
                throw new CalcLayoutException("Component cannot be placed in row 1 between columns 2 and 5");
            }
        }

        if (elements.get(constraint) != null) {
            throw new IllegalArgumentException("There is an element already on given position.");
        }

        elements.put(constraint, comp);
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        Insets targetInsets = target.getInsets();
        int height = 0, width = 0;

        for (var elementEntry : elements.entrySet()) {
            int elementHeight = elementEntry.getValue().getMaximumSize().height;
            int elementWidth = elementEntry.getValue().getMaximumSize().width;

            if (elementEntry.getKey().getRow() == 1 && elementEntry.getKey().getColumn() == 1) {
                elementHeight -= 4 * this.spaceBetweenElements;
                elementWidth /= 5;
            }

            if (elementHeight > height) {
                height = elementHeight;
            }

            if (elementWidth > width) {
                width = elementWidth;
            }
        }

        return new Dimension(width * 7 + targetInsets.left + targetInsets.right + 6 * spaceBetweenElements,
                height * 5 + targetInsets.top + targetInsets.bottom + 4 * spaceBetweenElements);
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
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        for (var entry : elements.entrySet()) {
            if (entry.getValue().equals(comp)) {
                elements.remove(entry.getKey());
                break;
            }
        }
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        Insets parentInsets = parent.getInsets();
        int height = 0, width = 0;

        for (var elementEntry : elements.entrySet()) {
            int elementHeight = elementEntry.getValue().getPreferredSize().height;
            int elementWidth = elementEntry.getValue().getPreferredSize().width;

            if (elementEntry.getKey().getRow() == 1 && elementEntry.getKey().getColumn() == 1) {
                elementHeight -= -4 * this.spaceBetweenElements;
                elementWidth /= 5;
            }

            if (elementHeight > height) {
                height = elementHeight;
            }

            if (elementWidth > width) {
                width = elementWidth;
            }
        }

        return new Dimension(width * 7 + parentInsets.left + parentInsets.right + 6 * spaceBetweenElements,
                height * 5 + parentInsets.top + parentInsets.bottom + 4 * spaceBetweenElements);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        Insets parentInsets = parent.getInsets();
        int height = 0, width = 0;

        for (var elementEntry : elements.entrySet()) {
            int elementHeight = elementEntry.getValue().getMinimumSize().height;
            int elementWidth = elementEntry.getValue().getMinimumSize().width;

            if (elementEntry.getKey().getRow() == 1 && elementEntry.getKey().getColumn() == 1) {
                elementHeight -= -4 * this.spaceBetweenElements;
                elementWidth /= 5;
            }

            if (elementHeight > height) {
                height = elementHeight;
            }

            if (elementWidth > width) {
                width = elementWidth;
            }
        }

        return new Dimension(width * 7 + parentInsets.left + parentInsets.right + 6 * spaceBetweenElements,
                height * 5 + parentInsets.top + parentInsets.bottom + 4 * spaceBetweenElements);
    }

    @Override
    public void layoutContainer(Container parent) {

        //USING UNIFORM ARRAY

        double heightPerElement = (parent.getHeight() - 4 * spaceBetweenElements) / 5.0;
        double widthPerElement = (parent.getWidth() - 6 * spaceBetweenElements) / 7.0;

        int heightPerElementPlus1 = (int) (heightPerElement) + 1;
        int widthPerElementPlus1 = (int) (widthPerElement) + 1;

        int numberOfHeightPlus1 = (int) ((heightPerElement - (int) heightPerElement) * 5);
        int numberOfWidthPlus1 = (int) ((widthPerElement - (int) widthPerElement) * 7);

        int[] heightsArray = new int[5];
        int[] widthsArray = new int[7];

        uniformArray(heightsArray, numberOfHeightPlus1, heightPerElementPlus1);
        uniformArray(widthsArray, numberOfWidthPlus1, widthPerElementPlus1);

        System.out.println(Arrays.toString(heightsArray));
        System.out.println(Arrays.toString(widthsArray));


        int height = (int) (parent.getHeight() / 5.0 - 4 * spaceBetweenElements) + 1;
        int width = (int) (parent.getWidth() / 7.0 - 6 * spaceBetweenElements) + 1;


        for (var element : elements.entrySet()) {
            int row = element.getKey().getRow();
            int column = element.getKey().getColumn();

            if (column == 1 && row == 1) {
                element.getValue().setBounds(0, 0, 5 * widthsArray[column - 1] + 4 * spaceBetweenElements, heightsArray[row - 1]);
            } else {
                element.getValue().setBounds((column - 1) * (widthsArray[column - 1] + spaceBetweenElements), (row - 1) *
                        (heightsArray[row - 1] + spaceBetweenElements), widthsArray[column - 1], heightsArray[row - 1]);
            }
        }

        /*
        int widths = (parent.getWidth() - 4 * spaceBetweenElements) / 5;
        int heights = (parent.getHeight() - 6 * spaceBetweenElements) / 7;

         for (var element : elements.entrySet()) {
             int row = element.getKey().getRow();
             int column = element.getKey().getColumn();

             if (column == 1 && row == 1) {
                 element.getValue().setBounds(0, 0, 5 * widths + 4 * spaceBetweenElements, heights);
             } else {
                 element.getValue().setBounds((column - 1) * (widths + spaceBetweenElements), (row - 1) *
                         (heights + spaceBetweenElements), width, height);
             }
         } */
    }

    /**
     * Method used to create a somewhat uniform distribution of numbers in array.
     *
     * @param array         array to fill with elements
     * @param numberOfPlus1 number of elements that will be of larger value
     * @param valuePlus1    the larger value
     */
    private void uniformArray(int[] array, int numberOfPlus1, int valuePlus1) {
        if (numberOfPlus1 == 0) {
            Arrays.fill(array, valuePlus1 - 1);
            return;
        }

        for (int i = 0; i < array.length; i += 2) {
            if (numberOfPlus1 > 0) {
                array[i] = valuePlus1;
                numberOfPlus1--;
            } else {
                for (int j = 0; j < array.length; j++) {
                    if (array[j] != valuePlus1) {
                        array[j] = valuePlus1 - 1;
                    }
                }
            }
        }

        if (numberOfPlus1 > 0) {
            for (int i = 1; i < array.length; i += 2) {
                array[i] = valuePlus1;
            }

            for (int i = 0; i < array.length; i++) {
                if (array[i] != valuePlus1) {
                    array[i] = valuePlus1 - 1;
                }
            }
        }

        for (int i = 0; i < array.length; i++) {
            if (array[i] == 0) {
                array[i] = valuePlus1 - 1;
            }
        }

    }

    public HashMap<RCPosition, Component> getElements() {
        return elements;
    }
}
