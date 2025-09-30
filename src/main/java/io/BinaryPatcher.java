package io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

// writer into overlay_36, arm9 and y9
public final class BinaryPatcher {
    private BinaryPatcher() {
    }

    public static void write(Path file, long offset, byte... data) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file.toFile(), "rw")) {
            raf.seek(offset);
            raf.write(data);
        }
    }
}
