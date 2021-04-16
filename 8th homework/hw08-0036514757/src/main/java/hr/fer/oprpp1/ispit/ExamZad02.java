package hr.fer.oprpp1.ispit;

import javax.swing.*;
import java.awt.*;

public class ExamZad02 extends JDialog {
    private java.util.List<Integer> list;

    public ExamZad02(java.util.List<Integer> list) {
        this.list = list;
        initGUI();
    }

    private void initGUI() {
        Container cp = getContentPane();
        cp.add(new BarComponent());
        setSize(1280, 720);
    }

    public class BarComponent extends JComponent {
        private boolean green = true;

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setBackground(Color.white);

            int sum = list.stream().mapToInt(i -> i).sum();

            Dimension dimension = getSize();

            int columnWidth = dimension.width / list.size();

            int index = 0;
            for (Integer i : list) {
                g2d.setColor(green ? Color.GREEN : Color.RED);
                green = !green;
                g2d.fillRect(index * columnWidth, 0, columnWidth, (int) (((double) i / sum) * dimension.height));
                index++;
            }
        }
    }

}