package hr.fer.oprpp1.hw08.jnotepad;

import hr.fer.oprpp1.hw08.jnotepad.documents.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepad.documents.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepad.documents.SingleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepad.local.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepad.local.LJMenu;
import hr.fer.oprpp1.hw08.jnotepad.local.LocalizableAction;
import hr.fer.oprpp1.hw08.jnotepad.local.LocalizationProvider;
import hr.fer.oprpp1.ispit.ExamZad01_1;
import hr.fer.oprpp1.ispit.ExamZad01_2;
import hr.fer.oprpp1.ispit.ExamZad02;
import hr.fer.oprpp1.ispit.ExamZad03;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * JNotepadPP is a little notepad mimic app with some additional features.
 */
public class JNotepadPP extends JFrame {
    private FormLocalizationProvider flp;
    private LocalizationProvider provider = LocalizationProvider.getInstance();
    private DefaultMultipleDocumentModel documents;

    public JNotepadPP() {
        this.documents = new DefaultMultipleDocumentModel();
        this.flp = new FormLocalizationProvider(provider, this);
        initGUI();
    }


    /**
     * GUI initializer method.
     */
    private void initGUI() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("(Unnamed)  -  JNotepad++");
        setLocationRelativeTo(null);
        setSize(850, 600);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        initMenuBar();
        initToolBar();
        createActions();
        initStatusBar();
        initTime();

        documents.createNewDocument();
        container.add(documents, BorderLayout.CENTER);
        updateStatusBar();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitAction.actionPerformed(null);
            }
        });

        documents.getCurrentDocument().getTextComponent().addCaretListener(e -> {
            selectionCheck();
            updateStatusBar();
        });

        documents.addChangeListener(e -> {
            documents.documentSwitched();
            Path filePath = documents.getCurrentDocument().getFilePath();
            String path;

            if (filePath == null) {
                path = "(Unnamed)";
            } else {
                path = filePath.toString();
            }

            JNotepadPP.this.setTitle(path + "  -  " + "JNotepad++");

            updateStatusBar();
        });

        documents.addMultipleDocumentListener(new MultipleDocumentListener() {
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {

                if (currentModel.getFilePath() != null) {
                    JNotepadPP.this.setTitle(currentModel.getFilePath().toString() + "  -  " + "JNotepad++");
                } else {
                    JNotepadPP.this.setTitle("(Unnamed)  -  JNotepad++");
                }

                currentModel.getTextComponent().addCaretListener(e -> {
                    selectionCheck();
                    updateStatusBar();
                });
                updateStatusBar();
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {
                model.getTextComponent().addCaretListener(e -> {
                    selectionCheck();
                    updateStatusBar();
                });
                updateStatusBar();
            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {
                model.getTextComponent().removeCaretListener(e -> updateStatusBar());
            }
        });
    }

    private LJMenu changeCaseMenu;
    private LJMenu sortMenu;
    private JMenuItem unique;

    /**
     * Method that will initialize menu bar and all its components.
     */
    private void initMenuBar() {
        JMenuBar menu = new JMenuBar();

        LJMenu fileMenu = new LJMenu("file", provider);
        fileMenu.add(new JMenuItem(newDocumentAction));
        fileMenu.add(new JMenuItem(openDocumentAction));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(saveAction));
        fileMenu.add(new JMenuItem(saveAsAction));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(statisticsAction));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(closeTabAction));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(exitAction));
        menu.add(fileMenu);


        LJMenu editMenu = new LJMenu("edit", provider);
        editMenu.add(new JMenuItem(copyAction));
        editMenu.add(new JMenuItem(cutAction));
        editMenu.add(new JMenuItem(pasteAction));
        menu.add(editMenu);

        LJMenu toolMenu = new LJMenu("tools", provider);
        changeCaseMenu = new LJMenu("change_case", provider);
        changeCaseMenu.add(toUppercaseAction);
        changeCaseMenu.add(toLowercaseAction);
        changeCaseMenu.add(invertAction);
        toolMenu.add(changeCaseMenu);
        sortMenu = new LJMenu("sort", provider);
        sortMenu.add(ascendingAction);
        sortMenu.add(descendingAction);
        toolMenu.add(sortMenu);
        toolMenu.addSeparator();
        unique = new JMenuItem(uniqueAction);
        toolMenu.add(unique);
        menu.add(toolMenu);

        LJMenu languagesMenu = new LJMenu("languages", provider);
        languagesMenu.add(new JMenuItem(croLanguageAction));
        languagesMenu.add(new JMenuItem(enLanguageAction));
        menu.add(languagesMenu);

        LJMenu examMenu = new LJMenu("exam", provider);
        examMenu.add(new JMenuItem(exercise11Action));
        examMenu.add(new JMenuItem(exercise12Action));
        examMenu.add(new JMenuItem(exercise2Action));
        examMenu.add(new JMenuItem(exercise3Action));
        menu.add(examMenu);

        this.setJMenuBar(menu);
    }

    /**
     * Method that will initialize tool bar and all its components.
     */
    private void initToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(true);

        toolBar.add(newDocumentAction);
        toolBar.add(openDocumentAction);
        toolBar.add(saveAction);
        toolBar.add(saveAsAction);
        toolBar.addSeparator();
        toolBar.add(closeTabAction);
        toolBar.add(exitAction);
        toolBar.addSeparator();
        toolBar.add(copyAction);
        toolBar.add(cutAction);
        toolBar.add(pasteAction);
        toolBar.addSeparator();
        toolBar.add(statisticsAction);
        toolBar.addSeparator();
        toolBar.add(croLanguageAction);
        toolBar.add(enLanguageAction);

        this.getContentPane().add(toolBar, BorderLayout.PAGE_START);
    }

    private JLabel length;
    private JLabel line;
    private JLabel column;
    private JLabel selected;
    private JLabel time = new JLabel();

    /**
     * Method that will initialize status bar and all its components.
     */
    private void initStatusBar() {
        JToolBar statsBar = new JToolBar();
        statsBar.setLayout(new BorderLayout());
        statsBar.setFloatable(true);

        length = new JLabel(flp.getString("length"));
        line = new JLabel(flp.getString("line"));
        column = new JLabel(flp.getString("column"));
        selected = new JLabel(flp.getString("selected"));

        JPanel infoPanel = new JPanel();
        infoPanel.add(line);
        infoPanel.add(column);
        infoPanel.add(selected);

        statsBar.add(length, BorderLayout.WEST);
        statsBar.add(infoPanel, BorderLayout.CENTER);
        statsBar.add(time, BorderLayout.EAST);

        this.getContentPane().add(statsBar, BorderLayout.PAGE_END);
    }

    /**
     * Method that will initialize the clock on the right
     */
    private void initTime() {
        Timer timer = new Timer(1000, e -> {
            Date current = new Date();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            time.setText(formatter.format(current));
        });

        timer.start();
    }

    /**
     * Method called to update status bar to new values. It'll be called on each caret position change.
     */
    private void updateStatusBar() {
        JTextArea textArea = documents.getCurrentDocument().getTextComponent();
        Element root = textArea.getDocument().getDefaultRootElement();

        try {
            length.setText(flp.getString("length") + ":" + textArea.getText().length());
            line.setText(flp.getString("line") + ":" + (textArea.getLineOfOffset(textArea.getCaretPosition()) + 1));
            column.setText(flp.getString("column") + ":" + (textArea.getCaretPosition() - root.getElement(root.getElementIndex(textArea.getCaretPosition())).getStartOffset()));
            selected.setText(flp.getString("selected") + ":" + Math.abs(textArea.getSelectionEnd() - textArea.getSelectionStart()));
        } catch (BadLocationException ignored) {
            //DO NOTHING
        }

    }

    /**
     * Method that will check if any text has been selected, and if it hasn't it'll disable all selected-related features.
     * Otherwise it will enable them.
     * This method, like updateStatusBar will be called on each cared position change.
     */
    private void selectionCheck() {
        JTextArea textArea = documents.getCurrentDocument().getTextComponent();
        int selectedLength = textArea.getSelectionEnd() - textArea.getSelectionStart();

        boolean check = selectedLength != 0;

        changeCaseMenu.setEnabled(check);
        sortMenu.setEnabled(check);
        unique.setEnabled(check);

    }

    /**
     * Performs the new document action.
     */
    private final Action newDocumentAction = new LocalizableAction("new", provider) {
        @Override
        public void actionPerformed(ActionEvent e) {
            documents.createNewDocument();
        }
    };

    /**
     * Performs the open document action.
     */
    private final Action openDocumentAction = new LocalizableAction("open", provider) {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Open file");

            if (fileChooser.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
                return;
            }

            Path filePath = fileChooser.getSelectedFile().toPath();

            try {
                documents.loadDocument(filePath);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(JNotepadPP.this, flp.getString("error_file_load"), flp.getString("error"), JOptionPane.ERROR_MESSAGE);
            }

        }
    };

    /**
     * Performs the save action. If used on file without a path, it will instead call save as action.
     */
    private final Action saveAction = new LocalizableAction("save", provider) {
        @Override
        public void actionPerformed(ActionEvent e) {
            SingleDocumentModel doc = documents.getCurrentDocument();

            if (doc.getFilePath() == null) {
                saveAsAction.actionPerformed(e);
                return;
            }

            try {
                documents.saveDocument(doc, doc.getFilePath());
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(
                        JNotepadPP.this,
                        flp.getString("error_file_save"),
                        flp.getString("error"),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(
                    JNotepadPP.this,
                    flp.getString("save_success"),
                    flp.getString("success"),
                    JOptionPane.INFORMATION_MESSAGE);

            doc.setModified(false);
        }
    };

    /**
     * Performs the save as action.
     */
    private final Action saveAsAction = new LocalizableAction("save_as", provider) {
        @Override
        public void actionPerformed(ActionEvent e) {
            SingleDocumentModel doc = documents.getCurrentDocument();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle(flp.getString("save_as"));

            if (fileChooser.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(
                        JNotepadPP.this,
                        flp.getString("no_changes_saved"),
                        flp.getString("warning"),
                        JOptionPane.WARNING_MESSAGE);
                return;
            }


            Path filePath = fileChooser.getSelectedFile().toPath();
            doc.setFilePath(filePath);

            try {
                documents.saveDocument(doc, filePath);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(
                        JNotepadPP.this,
                        flp.getString("file_exists"),
                        flp.getString("error"),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(
                    JNotepadPP.this,
                    flp.getString("save_success"),
                    flp.getString("success"),
                    JOptionPane.INFORMATION_MESSAGE);
        }
    };

    /**
     * Performs the close tab action. If there is only one tab opened at the time of closing, application will instead exit.
     */
    private final Action closeTabAction = new LocalizableAction("close_tab", provider) {
        @Override
        public void actionPerformed(ActionEvent e) {
            SingleDocumentModel doc = documents.getCurrentDocument();

            if (doc.isModified()) {
                int option = JOptionPane.showConfirmDialog(JNotepadPP.this, flp.getString("closing_modified"),
                        flp.getString("warning"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

                if (option == JOptionPane.YES_OPTION) {
                    saveAction.actionPerformed(e);
                }

                if (option == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }

            if (documents.getNumberOfDocuments() == 1) {
                exitAction.actionPerformed(e);
            }

            documents.closeDocument(doc);
        }
    };

    /**
     * Performs the exit action.
     */
    private final Action exitAction = new LocalizableAction("exit", provider) {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (SingleDocumentModel doc : documents) {
                if (doc.isModified()) {
                    int option = JOptionPane.showConfirmDialog(JNotepadPP.this, flp.getString("closing_modified"),
                            doc.getFilePath() == null ? "(Unnamed)" : doc.getFilePath().getFileName().toString(), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

                    if (option == JOptionPane.YES_OPTION) {
                        if (doc.getFilePath() == null) {
                            JFileChooser fileChooser = new JFileChooser();
                            fileChooser.setDialogTitle(flp.getString("save_as"));

                            if (fileChooser.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
                                JOptionPane.showMessageDialog(
                                        JNotepadPP.this,
                                        flp.getString("no_changes_saved"),
                                        flp.getString("warning"),
                                        JOptionPane.WARNING_MESSAGE);
                                return;
                            }

                            Path filePath = fileChooser.getSelectedFile().toPath();
                            doc.setFilePath(filePath);
                        }

                        documents.saveDocument(doc, doc.getFilePath());
                    }

                    if (option == JOptionPane.CANCEL_OPTION) {
                        return;
                    }
                }
            }

            System.exit(0);
        }
    };

    /**
     * Performs the copy action.
     */
    private final Action copyAction = new LocalizableAction("copy", provider) {
        @Override
        public void actionPerformed(ActionEvent e) {
            Action a = new DefaultEditorKit.CopyAction();
            a.actionPerformed(e);
        }
    };

    /**
     * Performs the cut action.
     */
    private final Action cutAction = new LocalizableAction("cut", provider) {
        @Override
        public void actionPerformed(ActionEvent e) {
            Action a = new DefaultEditorKit.CutAction();
            a.actionPerformed(e);
        }
    };

    /**
     * Performs the paste action.
     */
    private final Action pasteAction = new LocalizableAction("paste", provider) {
        @Override
        public void actionPerformed(ActionEvent e) {
            Action a = new DefaultEditorKit.PasteAction();
            a.actionPerformed(e);
        }
    };

    /**
     * Performs the statistics action, showing number of characters, number of non-blank characters and line number in current file.
     */
    private final Action statisticsAction = new LocalizableAction("statistics", provider) {
        @Override
        public void actionPerformed(ActionEvent e) {
            String text = documents.getCurrentDocument().getTextComponent().getText();

            int charNumber = text.length();
            long nonBlankCharNumber = text.chars().filter(c -> !(c == ' ' || c == '\t' || c == '\n')).count();
            long lineNumber = text.chars().filter(c -> c == '\n').count() + 1;

            String message = flp.getString("char_number") + ": " + charNumber + "\n" +
                    flp.getString("nonblank_char_number") + ": " + nonBlankCharNumber + "\n" +
                    flp.getString("line_number") + ": " + lineNumber;

            JOptionPane.showMessageDialog(JNotepadPP.this, message);
        }
    };

    /**
     * Performs the uppercase action on selected text.
     */
    private final Action toUppercaseAction = new LocalizableAction("to_uppercase", provider) {
        @Override
        public void actionPerformed(ActionEvent e) {
            changeSelectedText("to_uppercase");
        }
    };

    /**
     * Performs the lowercase action on selected text.
     */
    private final Action toLowercaseAction = new LocalizableAction("to_lowercase", provider) {
        @Override
        public void actionPerformed(ActionEvent e) {
            changeSelectedText("to_lowercase");
        }
    };

    /**
     * Performs the invert action on selected text.
     */
    private final Action invertAction = new LocalizableAction("invert", provider) {
        @Override
        public void actionPerformed(ActionEvent e) {
            changeSelectedText("invert");
        }
    };

    /**
     * Helper method for ascending, descending, unique, to lowercase, to uppercase and invert actions.
     *
     * @param key telling which method is in question
     */
    private void changeSelectedText(String key) {
        JTextArea textArea = documents.getCurrentDocument().getTextComponent();
        Document doc = textArea.getDocument();
        int len = Math.abs(textArea.getSelectionEnd() - textArea.getSelectionStart());
        int offset = 0;

        if (len != 0) {
            offset = Math.min(textArea.getCaret().getDot(), textArea.getCaret().getMark());
        }

        if (key.equals("ascending") || key.equals("descending") || key.equals("unique")) {
            try {
                offset = textArea.getLineStartOffset(textArea.getLineOfOffset(offset));
                len = textArea.getLineEndOffset(textArea.getLineOfOffset(len + offset));

                String text = doc.getText(offset, len - offset);
                doc.remove(offset, len - offset);
                doc.insertString(offset, editLines(key, text) + "\n", null);
            } catch (BadLocationException ignored) {
                //DO NOTHING
            }
            return;
        }

        try {
            String text = doc.getText(offset, len);
            switch (key) {
                case "to_uppercase" -> text = text.toUpperCase();
                case "to_lowercase" -> text = text.toLowerCase();
                case "invert" -> text = invert(text);
                default -> System.err.println("Wrong key");
            }
            doc.remove(offset, len);
            doc.insertString(offset, text, null);
        } catch (BadLocationException ignored) {
            //DO NOTHING
        }
    }

    /**
     * Helper method used for invert letters action. All lower case letters will become upper case and vice versa.
     *
     * @param text to be inverted
     * @return inverted text
     */
    private String invert(String text) {
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (Character.isLowerCase(c)) {
                chars[i] = Character.toUpperCase(c);
            } else if (Character.isUpperCase(c)) {
                chars[i] = Character.toLowerCase(c);
            }
        }
        return new String(chars);
    }

    /**
     * Helper method used for ascending, descending and unique actions.
     *
     * @param key  ascending, descending or unique
     * @param text text to perform the action on
     * @return text after performing one of the three actions
     */
    private String editLines(String key, String text) {
        ArrayList<String> lines = new ArrayList<>(Arrays.asList(text.split("\n")));

        if (!key.equals("unique")) {
            Locale locale = new Locale(provider.getCurrentLanguage());
            Collator collator = Collator.getInstance(locale);

            lines.sort(collator::compare);

            if (key.equals("descending")) {
                Collections.reverse(lines);
            }
            return String.join("\n", lines);
        } else {
            HashSet<String> set = new HashSet<>(lines);
            return String.join("\n", set);
        }
    }

    /**
     * Preforms the ascending action on selected text. It will sort all lines in ascending order.
     */
    private final Action ascendingAction = new LocalizableAction("ascending", provider) {
        @Override
        public void actionPerformed(ActionEvent e) {
            changeSelectedText("ascending");
        }
    };

    /**
     * Preforms the descending action on selected text. It will sort all lines in descending order.
     */
    private final Action descendingAction = new LocalizableAction("descending", provider) {
        @Override
        public void actionPerformed(ActionEvent e) {
            changeSelectedText("descending");
        }
    };

    /**
     * Preforms the unique action on selected text. It will remove all duplicate lines in selected text, leaving only one representative.
     */
    private final Action uniqueAction = new LocalizableAction("unique", provider) {
        @Override
        public void actionPerformed(ActionEvent e) {
            changeSelectedText("unique");
        }
    };

    /**
     * Changes GUI language to Croatian.
     */
    private final Action croLanguageAction = new LocalizableAction("croatian", provider) {

        @Override
        public void actionPerformed(ActionEvent e) {
            provider.setLanguage("hr");
        }
    };

    /**
     * Changes GUI language to English.
     */
    private final Action enLanguageAction = new LocalizableAction("english", provider) {

        @Override
        public void actionPerformed(ActionEvent e) {
            provider.setLanguage("en");
        }
    };

    private final Action exercise11Action = new LocalizableAction("exercise11", provider) {

        @Override
        public void actionPerformed(ActionEvent e) {

            SwingUtilities.invokeLater(() -> new ExamZad01_1().setVisible(true));
        }
    };

    private final Action exercise12Action = new LocalizableAction("exercise12", provider) {

        @Override
        public void actionPerformed(ActionEvent e) {

            SwingUtilities.invokeLater(() -> new ExamZad01_2().setVisible(true));
        }
    };

    private final Action exercise3Action = new LocalizableAction("exercise3", provider) {

        @Override
        public void actionPerformed(ActionEvent e) {

            SwingUtilities.invokeLater(() -> new ExamZad03(documents).setVisible(true));
        }
    };

    private final Action exercise2Action = new LocalizableAction("exercise2", provider) {

        @Override
        public void actionPerformed(ActionEvent e) {
            List<Integer> list = new ArrayList<>();
            for (SingleDocumentModel doc : documents) {
                list.add(doc.getTextComponent().getText().length());
            }

            SwingUtilities.invokeLater(() -> new ExamZad02(list).setVisible(true));
        }
    };

    /**
     * This method will assign all key shortcuts to all actions.
     */
    private void createActions() {
        newDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
        newDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);

        openDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
        openDocumentAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);

        saveAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
        saveAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);

        saveAsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift S"));
        saveAsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);

        closeTabAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift C"));
        closeTabAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);

        exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control E"));
        exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);

        copyAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
        copyAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);

        cutAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
        cutAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);

        pasteAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
        pasteAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_V);

        statisticsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control T"));
        statisticsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);

        toUppercaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control U"));
        toUppercaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U);

        toLowercaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control L"));
        toLowercaseAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);

        invertAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control I"));
        invertAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_I);

        ascendingAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift A"));
        ascendingAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);

        descendingAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control D"));
        descendingAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_D);

        uniqueAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q"));
        uniqueAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Q);
    }

    public static void main(String... args) {
        SwingUtilities.invokeLater(() -> new JNotepadPP().setVisible(true));
    }
}
