package editor;

import javax.swing.*;

public class SearchWorker extends SwingWorker<Object, Object> {
    @Override
    protected Object doInBackground() throws Exception {
        Application.textEditor.startSearch();
        return null;
    }
}
