package ui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;

import core.RomPatcher;
import core.RomProbe;
import core.BlackRomBytes;
import core.GameVersion;
import core.WhiteRomBytes;
import java.nio.file.*;

import model.MusicOption;
import model.TrainerNames;
import model.MusicCodes;

import java.awt.*;
import java.io.File;

public class EditorUI extends JFrame {
    private final JTextField romField = new JTextField();
    private final JButton browseBtn = new JButton("Browse…");

    private final JRadioButton b2Radio = new JRadioButton("Black 2");
    private final JRadioButton w2Radio = new JRadioButton("White 2");

    private final JButton applyBtn = new JButton("Apply and Download");

    private final JTable table = new JTable();
    private final JLabel status = new JLabel("Ready.");
    private File selectedRom = null;
    private GameVersion detectedVersion = null;

    public EditorUI() {
        super("B2/W2 Trainer Class Music Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(620, 600));
        setLocationByPlatform(true);
        getContentPane().setLayout(new BorderLayout(8, 8));

        // TOP GUI: ROM picker + version
        insFileChooser();

        // MIDDLE GUI: table (236 trainer classes)
        insMusicTable();

        // BOTTOM GUI: status bar
        JPanel bottom = new JPanel(new BorderLayout());
        status.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        bottom.add(status, BorderLayout.CENTER);
        getContentPane().add(bottom, BorderLayout.SOUTH);

        // Wire buttons
        browseBtn.addActionListener(e -> chooseRom());
        clickApplyButton();
    }

    // Top GUI: ROM picker + version
    private void insFileChooser() {
        JPanel top = new JPanel(new BorderLayout(8, 8));
        romField.setEditable(false);
        JPanel leftTop = new JPanel(new BorderLayout(6, 6));
        leftTop.add(new JLabel("ROM:"), BorderLayout.WEST);
        leftTop.add(romField, BorderLayout.CENTER);
        leftTop.add(browseBtn, BorderLayout.EAST);
        top.add(leftTop, BorderLayout.CENTER);

        b2Radio.setEnabled(false);
        w2Radio.setEnabled(false);
        b2Radio.setFocusable(false);
        w2Radio.setFocusable(false);

        JPanel vers = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        ButtonGroup g = new ButtonGroup();
        g.add(b2Radio);
        g.add(w2Radio);
        b2Radio.setSelected(true);
        vers.add(new JLabel("Version:"));
        vers.add(b2Radio);
        vers.add(w2Radio);
        vers.add(applyBtn);
        top.add(vers, BorderLayout.SOUTH);

        getContentPane().add(top, BorderLayout.NORTH);
    }

    // Table (236 trainer classes)
    private void insMusicTable() {
        String[] cols = { "Index", "Trainer Name", "Battle Music (0xXx + Name)" };
        Object[][] rows = new Object[236][3];
        for (int i = 0; i < 236; i++) {
            rows[i][0] = Integer.valueOf(i);
            rows[i][1] = TrainerNames.convertIndex(i);
            int code = MusicCodes.musicCodeForIndex(i);
            rows[i][2] = new MusicOption(code, MusicCodes.hexLabel(code));
        }

        // Model of Music Table
        DefaultTableModel model = new DefaultTableModel(rows, cols) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 2;
            }

            @Override
            public Class<?> getColumnClass(int c) {
                return (c == 0) ? Integer.class : (c == 1) ? String.class : MusicOption.class;
            }
        };

        table.setModel(model);

        // Left Text Alignment
        DefaultTableCellRenderer left = new DefaultTableCellRenderer();
        left.setHorizontalAlignment(SwingConstants.LEFT);
        table.getColumnModel().getColumn(0).setCellRenderer(left);

        table.setRowHeight(22);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        table.getColumnModel().getColumn(0).setPreferredWidth(40); // Index
        table.getColumnModel().getColumn(1).setPreferredWidth(280); // Trainer Name
        table.getColumnModel().getColumn(2).setPreferredWidth(330); // Music

        // Combo editor for music column
        insTableDropdown();

        getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void insTableDropdown() {
        JComboBox<MusicOption> combo = new JComboBox<>();
        for (MusicOption opt : MusicCodes.options())
            combo.addItem(opt);

        // (optional) ensure the dropdown list shows the label
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof MusicOption)
                    lbl.setText(((MusicOption) value).getLabel());
                return lbl;
            }
        });

        table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(combo));
        table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                if (v instanceof MusicOption)
                    setText(((MusicOption) v).getLabel()); // "0x6A"
                return this;
            }
        });
    }


    private void clickApplyButton() {
        applyBtn.addActionListener(e -> {
            if (selectedRom == null) {
                JOptionPane.showMessageDialog(this, "Pick a ROM first.", "No ROM", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // prefer auto-detected version; fall back to radio
            GameVersion v = (detectedVersion != null)
                    ? detectedVersion
                    : (b2Radio.isSelected() ? GameVersion.BLACK2 : GameVersion.WHITE2);

            core.RomPatcher patcher = (v == GameVersion.BLACK2) ? new core.BlackRomBytes()
                    : new core.WhiteRomBytes();

            try {
                int[] music = readMusicFromTable();
                Path target = chooseSavePath(); // your existing Save As… from earlier
                if (target == null)
                    return;

                Path built = patcher.patch(selectedRom.toPath(), repoRoot(), workDir(), music);
                Files.createDirectories(target.getParent());
                Files.copy(built, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                JOptionPane.showMessageDialog(this, "Saved: " + target, "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                status.setText("Built " + target.getFileName());
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, patcher.name() + " patch failed: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // Non-GUI Helpers

    private void chooseRom() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Select B2/W2 ROM (.nds)");
        if (selectedRom != null)
            fc.setCurrentDirectory(selectedRom.getParentFile());
        int res = fc.showOpenDialog(this);
        if (res != JFileChooser.APPROVE_OPTION)
            return;

        selectedRom = fc.getSelectedFile();
        romField.setText(selectedRom.getAbsolutePath());
        status.setText("Selected ROM: " + selectedRom.getName());

        // Auto-extract + version detect in background
        applyBtn.setEnabled(false);
        b2Radio.setEnabled(false);
        w2Radio.setEnabled(false);
        status.setText("Extracting and detecting version…");

        new SwingWorker<Void, Void>() {
            private GameVersion result;
            private Exception error;

            @Override
            protected Void doInBackground() {
                try {
                    result = RomProbe.extractAndDetect(selectedRom.toPath(), repoRoot(), workDir());
                } catch (Exception ex) {
                    error = ex;
                }
                return null;
            }

            @Override
            protected void done() {
                if (error != null) {
                    JOptionPane.showMessageDialog(EditorUI.this,
                            "Not a valid BW2 ROM (or tools failed):\n" + error.getMessage(),
                            "Invalid ROM", JOptionPane.ERROR_MESSAGE);
                    selectedRom = null;
                    romField.setText("");
                    status.setText("Ready.");
                    b2Radio.setEnabled(true);
                    w2Radio.setEnabled(true);
                    applyBtn.setEnabled(true);
                    return;
                }
                detectedVersion = result;
                if (detectedVersion == GameVersion.BLACK2)
                    b2Radio.setSelected(true);
                if (detectedVersion == GameVersion.WHITE2)
                    w2Radio.setSelected(true);
                status.setText("Extracted. Detected: " + detectedVersion);
                // keep radios locked so the user doesn’t mis-pick
                applyBtn.setEnabled(true);
            }
        }.execute();

        
    }

    private Path chooseSavePath() {
        File dir = (selectedRom != null) ? selectedRom.getParentFile() : new File(".");
        JFileChooser fc = new JFileChooser(dir);
        fc.setDialogTitle("Save patched ROM as…");
        fc.setFileFilter(new FileNameExtensionFilter("Nintendo DS ROM (*.nds)", "nds"));

        String base = stripExt(selectedRom != null ? selectedRom.getName() : "patched");
        fc.setSelectedFile(new File(dir, base + "_patched.nds"));

        int res = fc.showSaveDialog(this);
        if (res != JFileChooser.APPROVE_OPTION)
            return null;

        File f = fc.getSelectedFile();
        // ensure .nds extension
        if (!f.getName().toLowerCase().endsWith(".nds")) {
            f = new File(f.getParentFile(), f.getName() + ".nds");
        }

        if (f.exists()) {
            int ow = JOptionPane.showConfirmDialog(
                    this,
                    "Overwrite existing file?\n" + f.getAbsolutePath(),
                    "Confirm overwrite",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (ow != JOptionPane.YES_OPTION)
                return null;
        }
        return f.toPath();
    }

    private static String stripExt(String name) {
        int i = name.lastIndexOf('.');
        return (i > 0) ? name.substring(0, i) : name;
    }

    private Path repoRoot() {
        return Paths.get("").toAbsolutePath();
    }

    private Path workDir() {
        return Paths.get(System.getenv("LOCALAPPDATA"), "bw2_editor", "work");
    }

    private int[] readMusicFromTable() {
        int[] out = new int[236];
        for (int i = 0; i < 236; i++) {
            MusicOption m = (MusicOption) table.getValueAt(i, 2);
            out[i] = (m != null ? m.getCode() : 0x6A);
        }
        return out;
    }

    public String getRomName() {
        return selectedRom.getName();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new EditorUI().setVisible(true);
            }
        });
    }
}