package hr.fer.oprpp1.hw08.jnotepad.documents;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DefaultSingleDocumentModel implements SingleDocumentModel {
    private Path filePath;
    private JTextArea text;
    private boolean modified;
    private List<SingleDocumentListener> listeners;


    public DefaultSingleDocumentModel(Path filePath, String text) {
        this.filePath = filePath;
        this.text = new JTextArea(text);
        this.listeners = new ArrayList<>();
        this.modified = false;
        this.text.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                modified = true;
                notifyListeners();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                modified = true;
                notifyListeners();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                modified = true;
                notifyListeners();
            }
        });

    }

    @Override
    public JTextArea getTextComponent() {
        return text;
    }

    @Override
    public Path getFilePath() {
        return filePath;
    }

    @Override
    public void setFilePath(Path path) {
        if (path == null) {
            throw new NullPointerException("Path cannot be null");
        }
        this.filePath = path;
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean modified) {
        this.modified = modified;
        notifyListeners();
    }

    @Override
    public void addSingleDocumentListener(SingleDocumentListener l) {
        listeners.add(l);
    }

    @Override
    public void removeSingleDocumentListener(SingleDocumentListener l) {
        listeners.remove(l);
    }

    private void notifyListeners() {
        listeners.forEach(l -> l.documentModifyStatusUpdated(this));
    }
}
