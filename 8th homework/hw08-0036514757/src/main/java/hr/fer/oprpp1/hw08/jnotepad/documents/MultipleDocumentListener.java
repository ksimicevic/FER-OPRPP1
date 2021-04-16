package hr.fer.oprpp1.hw08.jnotepad.documents;

public interface MultipleDocumentListener {
    void currentDocumentChanged(SingleDocumentModel previousModel,
                                SingleDocumentModel currentModel);
    void documentAdded(SingleDocumentModel model);
    void documentRemoved(SingleDocumentModel model);
}
