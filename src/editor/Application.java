package editor;

import javax.swing.*;

public class Application {
    static TextEditor textEditor;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> textEditor = new TextEditor());
    }
}
