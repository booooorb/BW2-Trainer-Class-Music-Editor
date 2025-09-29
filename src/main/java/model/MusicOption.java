package model;

import model.MusicNames;

public class MusicOption {
    private final int code; // e.g., 0x0452
    private final String label; // UI display

    public MusicOption(int code, String label) {
        this.code = code;
        this.label = label + " - " + MusicNames.musicNameForCode(code);
    }

    public int getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof MusicOption) && ((MusicOption) o).code == code;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(code);
    }
}
