package hr.fer.oprpp1.ispit;

import javax.swing.*;
import java.awt.*;

public class ExamZad01_1 extends JDialog {
    public ExamZad01_1() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        initGUI();
        pack();
    }
    private void initGUI() {
        Container cp = getContentPane();
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
    }
    private Component makeLabel(String txt, Color col) {
        JLabel lab = new JLabel(txt);
        lab.setOpaque(true);
        lab.setBackground(col);
        return lab;
    }
}
