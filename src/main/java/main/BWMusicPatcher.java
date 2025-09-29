package main;
import javax.swing.SwingUtilities;
import ui.EditorUI;

import javax.swing.SwingUtilities;

// Init
public class BWMusicPatcher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EditorUI().setVisible(true));
    }
}
