package hr.fer.oprpp1.ispit;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class ExamZad01_2 extends JDialog {
    public ExamZad01_2() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        initGUI();
        pack();
    }

    private void initGUI() {
        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        Container cp = new Container();
        ExamLayoutManager exlm = new ExamLayoutManager(20);
        cp.setLayout(exlm);
        cp.add(
                makeLabel("Ovo je tekst za područje 1.", Color.RED),
                ExamLayoutManagerConstraint.AREA1);
        cp.add(
                makeLabel("Područje 2.", Color.GREEN),
                ExamLayoutManagerConstraint.AREA2);
        cp.add(
                makeLabel("Područje 3.", Color.YELLOW),
                ExamLayoutManagerConstraint.AREA3);
        container.add(cp, BorderLayout.CENTER);

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 10, 90, 50);
        slider.setSize(slider.getMinimumSize());
        slider.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            exlm.setPercentage(source.getValue());
        });
        container.add(slider, BorderLayout.NORTH);
    }

    private Component makeLabel(String txt, Color col) {
        JLabel lab = new JLabel(txt);
        lab.setOpaque(true);
        lab.setBackground(col);
        return lab;
    }
}
