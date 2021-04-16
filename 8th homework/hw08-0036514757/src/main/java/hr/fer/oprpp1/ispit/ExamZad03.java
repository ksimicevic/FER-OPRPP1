package hr.fer.oprpp1.ispit;

import hr.fer.oprpp1.hw08.jnotepad.documents.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepad.documents.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepad.documents.SingleDocumentModel;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class ExamZad03 extends JDialog {
    private MultipleDocumentModel model;
    private DocumentList listModel = new DocumentList();

    public ExamZad03(MultipleDocumentModel model) {
        this.model = model;
        model.addMultipleDocumentListener(new MultipleDocumentListener() {
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                for (var l : listModel.listeners) {
                    l.contentsChanged(new ListDataEvent(listModel, ListDataEvent.CONTENTS_CHANGED, listModel.getSize(), listModel.getSize()));
                }
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {
                for (var l : listModel.listeners) {
                    l.intervalAdded(new ListDataEvent(listModel, ListDataEvent.INTERVAL_ADDED, listModel.getSize(), listModel.getSize()));
                }
            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {
                for (var l : listModel.listeners) {
                    l.intervalRemoved(new ListDataEvent(listModel, ListDataEvent.INTERVAL_REMOVED, listModel.getSize(), listModel.getSize()));
                }
            }
        });
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(false);
        initGUI();
        pack();
    }

    private void initGUI() {
        Container cp = getContentPane();

        listModel.updateList();
        JList<String> list = new JList<>(listModel);
        list.setSelectedIndex(listModel.docs.indexOf(model.getCurrentDocument()));

        JScrollPane scrollPane = new JScrollPane(list);
        listModel.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                listModel.updateList();
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                listModel.updateList();
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                listModel.updateList();
            }
        });
        listModel.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                list.setSelectedIndex(listModel.docs.indexOf(model.getCurrentDocument()));
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                list.setSelectedIndex(listModel.docs.indexOf(model.getCurrentDocument()));
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                list.setSelectedIndex(listModel.docs.indexOf(model.getCurrentDocument()));

            }
        });
        cp.add(scrollPane);
    }


    private class DocumentList implements ListModel<String> {
        private List<ListDataListener> listeners = new LinkedList<>();
        private List<SingleDocumentModel> docs = new LinkedList<>();

        @Override
        public int getSize() {
            return docs.size();
        }

        @Override
        public String getElementAt(int index) {
            Path p = docs.get(index).getFilePath();
            return p == null ? "(Unnamed)" : p.toString();
        }

        public void updateList() {
            docs.clear();
            for (SingleDocumentModel singleDocumentModel : model) {
                docs.add(singleDocumentModel);
            }
        }

        @Override
        public void addListDataListener(ListDataListener l) {
            listeners.add(l);
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
            listeners.remove(l);
        }
    }
}
