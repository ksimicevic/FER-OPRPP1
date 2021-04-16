package hr.fer.oprpp1.hw08.jnotepad.documents;

import hr.fer.oprpp1.hw08.jnotepad.JNotepadPP;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {
    private final ArrayList<SingleDocumentModel> documentList = new ArrayList<>();
    private SingleDocumentModel currentDocument;

    private final ArrayList<MultipleDocumentListener> listeners = new ArrayList<>();

    private final ImageIcon RED_DISKETTE = loadIcon("red_diskette.png");
    private final ImageIcon GREEN_DISKETTE = loadIcon("green_diskette.png");

    @Override
    public SingleDocumentModel createNewDocument() {
        SingleDocumentModel doc = new DefaultSingleDocumentModel(null, "");
        documentList.add(doc);
        currentDocument = doc;
        addNewTab(doc);
        doc.addSingleDocumentListener(new SingleDocumentListener() {
            @Override
            public void documentModifyStatusUpdated(SingleDocumentModel model) {
                setIconAt(documentList.indexOf(doc), doc.isModified() ? RED_DISKETTE : GREEN_DISKETTE);
            }

            @Override
            public void documentFilePathUpdated(SingleDocumentModel model) {
                setTitleAt(documentList.indexOf(doc), model.getFilePath().getFileName().toString());
            }
        });
        setSelectedIndex(documentList.indexOf(doc));
        notifyAllListeners(doc, null, DocumentChange.ADDED);
        return doc;
    }

    @Override
    public SingleDocumentModel getCurrentDocument() {
        return currentDocument;
    }

    @Override
    public SingleDocumentModel loadDocument(Path path) {
        if (path == null) {
            throw new NullPointerException("Path to load document cannot be null!");
        }

        for (SingleDocumentModel docs : documentList) {
            if (Objects.equals(docs.getFilePath(), path)) {
                notifyAllListeners(currentDocument, docs, DocumentChange.CHANGED);
                setSelectedIndex(documentList.indexOf(docs));
                currentDocument = docs;
                return docs;
            }
        }

        java.util.List<String> lines;

        try {
            lines = Files.readAllLines(path);
        } catch (IOException ex) {
            throw new RuntimeException("Error while reading file");
        }

        SingleDocumentModel doc = new DefaultSingleDocumentModel(path, String.join("\n", lines));
        documentList.add(doc);
        currentDocument = doc;
        addNewTab(doc);
        doc.addSingleDocumentListener(new SingleDocumentListener() {
            @Override
            public void documentModifyStatusUpdated(SingleDocumentModel model) {
                setIconAt(documentList.indexOf(doc), doc.isModified() ? RED_DISKETTE : GREEN_DISKETTE);
            }

            @Override
            public void documentFilePathUpdated(SingleDocumentModel model) {
                setTitleAt(documentList.indexOf(doc), model.getFilePath().getFileName().toString());
            }
        });
        setSelectedIndex(documentList.indexOf(doc));
        notifyAllListeners(currentDocument, null, DocumentChange.ADDED);
        return doc;
    }

    @Override
    public void saveDocument(SingleDocumentModel model, Path newPath) {
        String text = model.getTextComponent().getText();

        for (SingleDocumentModel doc : documentList) {
            if (!doc.equals(model) && Objects.equals(newPath, doc.getFilePath())) {
                throw new IllegalArgumentException("There is already a file there!");
            }
        }

        if(model.getFilePath() == null || !newPath.getFileName().equals(model.getFilePath())) {
            model.setFilePath(newPath);
            setTitleAt(getSelectedIndex(), newPath.getFileName().toString());
        }


        try {
            Files.write(newPath, Collections.singletonList(text));
        } catch (IOException ex) {
            throw new RuntimeException("Error while writing into file");
        }

        model.setModified(false);
        notifyAllListeners(currentDocument, model, DocumentChange.CHANGED);
    }

    @Override
    public void closeDocument(SingleDocumentModel model) {
        int indexOfClosingDocument = documentList.indexOf(model);
        removeTabAt(indexOfClosingDocument);
        setSelectedIndex(indexOfClosingDocument - 1);
        currentDocument = documentList.get(indexOfClosingDocument - 1);
        documentList.remove(indexOfClosingDocument);
        notifyAllListeners(model, null, DocumentChange.REMOVED);
    }

    @Override
    public void addMultipleDocumentListener(MultipleDocumentListener l) {
        listeners.add(l);
    }

    @Override
    public void removeMultipleDocumentListener(MultipleDocumentListener l) {
        listeners.remove(l);
    }

    @Override
    public int getNumberOfDocuments() {
        return documentList.size();
    }

    @Override
    public SingleDocumentModel getDocument(int index) {
        return documentList.get(index);
    }

    @Override
    public Iterator<SingleDocumentModel> iterator() {
        return documentList.iterator();
    }

    /**
     * Method helper that will add a new tab when creating or loading a new document.
     *
     * @param doc document for which a new tab will be created
     */
    private void addNewTab(SingleDocumentModel doc) {
        JPanel docPanel = new JPanel();
        docPanel.setLayout(new BorderLayout());
        docPanel.add(new JScrollPane(doc.getTextComponent()), BorderLayout.CENTER);

        String absolutePath = "";
        String fileName;

        Path filePath = doc.getFilePath();

        if (filePath != null) {
            absolutePath = filePath.toAbsolutePath().toString();
            fileName = filePath.getFileName().toString();
        } else {
            fileName = "(Unnamed)";
        }

        addTab(fileName, GREEN_DISKETTE, docPanel, absolutePath);
    }

    /**
     * Method helper that will load all icons to be later used.
     *
     * @param iconName icon name
     * @return image icon representing the icon loaded
     * @throws NullPointerException if unable to create a stream
     * @throws RuntimeException     if there's an error while loading the icon
     */
    private ImageIcon loadIcon(String iconName) {
        InputStream inputStream = JNotepadPP.class.getResourceAsStream("icons/" + iconName);

        if (inputStream == null) {
            throw new NullPointerException("No icon found");
        }

        byte[] bytes;
        try {
            bytes = inputStream.readAllBytes();
            inputStream.close();
        } catch (IOException exception) {
            throw new RuntimeException("Error while reading");
        }

        Image img = new ImageIcon(bytes).getImage().getScaledInstance(10, 10, Image.SCALE_SMOOTH);

        return new ImageIcon(img);
    }

    /**
     * Enum representing types of change that can happen in document.
     */
    private enum DocumentChange {
        ADDED, REMOVED, CHANGED;
    }

    /**
     * Helper method that will notify all listeners according to change that happened.
     *
     * @param doc            document that was affected
     * @param newDoc         another document that was affected or null if there wasn't
     * @param documentChange type of change that happened
     */
    private void notifyAllListeners(SingleDocumentModel doc, SingleDocumentModel newDoc, DocumentChange documentChange) {
        switch (documentChange) {
            case ADDED:
                listeners.forEach(l -> l.documentAdded(doc));
                break;
            case REMOVED:
                listeners.forEach(l -> l.documentRemoved(doc));
                break;
            case CHANGED:
                listeners.forEach(l -> l.currentDocumentChanged(doc, newDoc));
                break;
            default:
                //NOTHING
        }
    }

    /**
     * Method that will called to update the current document when tab is switched.
     */
    public void documentSwitched() {
        currentDocument = documentList.get(getSelectedIndex());
    }
}
