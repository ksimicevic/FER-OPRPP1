package hr.fer.zemris.java.gui.prim;

import javax.swing.*;
import java.awt.*;

/**
 * PrimDemo is a class representing a frame. All GUI related things happen here.
 */
public class PrimDemo extends JFrame {
    private PrimListModel listModel;

    public PrimDemo() {
        listModel = new PrimListModel();
        initGUI();
    }

    /**
     * Method to initialize GUI.
     */
    private void initGUI() {
        setSize(500, 500);
        setTitle("PrimDemo");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        JPanel listPanel = new JPanel(new GridLayout(1, 2));

        JList<Integer> listLeft = new JList<>(listModel);
        JList<Integer> listRight = new JList<>(listModel);

        JScrollPane scrollLeft = new JScrollPane(listLeft);
        JScrollPane scrollRight = new JScrollPane(listRight);

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> listModel.next());

        JPanel btnPanel = new JPanel();
        btnPanel.add(nextButton);

        listPanel.add(scrollLeft);
        listPanel.add(scrollRight);

        container.add(listPanel, BorderLayout.CENTER);
        container.add(btnPanel, BorderLayout.SOUTH);

    }

    public static void main(String... args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new PrimDemo();
            frame.setVisible(true);
        });
    }
}
