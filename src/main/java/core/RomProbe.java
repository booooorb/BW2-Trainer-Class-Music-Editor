// core/RomProbe.java
package core;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public final class RomProbe {
    private RomProbe() {
    }

    // checks known sequences
    private static final byte[] B2_ORIG = hex(
            "6A F6 10 FB 07 1C 01 98 6A F6 FA FA 0E 2F 08 D2 18 49 7A 00 89 5A 03 B0 21 80");
    private static final byte[] B2_EDIT = hex(
            "1C 49 82 00 89 5C 21 70 6A F6 0C FB 07 1C 01 98 6A F6 F6 FA 0E 2F 04 D2 03 B0");

    private static final byte[] W2_ORIG = hex(
            "6A F6 0A FB 07 1C 01 98 6A F6 F4 FA 0E 2F 08 D2 18 49 7A 00 89 5A 03 B0 21 80");
    private static final byte[] W2_EDIT = hex(
            "1C 49 82 00 89 5C 21 70 6A F6 06 FB 07 1C 01 98 6A F6 F0 FA 0E 2F 04 D2 03 B0");

    public static GameVersion extractAndDetect(Path rom, Path repoRoot, Path workDir) throws Exception {
        Files.createDirectories(workDir);
        try (var s = Files.list(workDir)) {
            s.forEach(p -> {
                try {
                    deleteRec(p);
                } catch (Exception ignore) {
                }
            });
        }

        // Run ndstool extract
        Path arm9 = workDir.resolve("arm9.bin");
        Path arm7 = workDir.resolve("arm7.bin");
        Path y9 = workDir.resolve("y9.bin");
        Path y7 = workDir.resolve("y7.bin");
        Path data = workDir.resolve("data");
        Path overlay = workDir.resolve("overlay");
        Path banner = workDir.resolve("banner.bin");
        Path header = workDir.resolve("header.bin");

        exec(repoRoot.resolve("tools/ndstool.exe").toString(),
                "-x", rom.toString(),
                "-9", arm9.toString(),
                "-7", arm7.toString(),
                "-y9", y9.toString(),
                "-y7", y7.toString(),
                "-d", data.toString(),
                "-y", overlay.toString(),
                "-t", banner.toString(),
                "-h", header.toString());

        // Decompress overlay_0036
        Path ov36 = overlay.resolve("overlay_0036.bin");
        if (!Files.isRegularFile(ov36))
            throw new IllegalArgumentException("overlay_0036.bin not found ... this does not look like BW2.");

        Path ov36dec = workDir.resolve("overlay_0036decomp.bin");
        Files.copy(ov36, ov36dec, StandardCopyOption.REPLACE_EXISTING);
        exec(repoRoot.resolve("tools/blz.exe").toString(), "-d", ov36dec.toString()); // decompress in place

        // Identify version by matching known offsets
        byte[] buf = Files.readAllBytes(ov36dec);
        boolean isB2 = matchesAt(buf, 0x46BA0, B2_ORIG) || matchesAt(buf, 0x46BA0, B2_EDIT);
        boolean isW2 = matchesAt(buf, 0x46B98, W2_ORIG) || matchesAt(buf, 0x46B98, W2_EDIT);

        if (isB2 && !isW2)
            return GameVersion.BLACK2;
        if (isW2 && !isB2)
            return GameVersion.WHITE2;

        throw new IllegalArgumentException("Could not recognize BW2 version from overlay_0036. Wrong ROM?");
    }

    private static boolean matchesAt(byte[] buf, int offset, byte[] pat) {
        if (offset < 0 || offset + pat.length > buf.length)
            return false;
        for (int i = 0; i < pat.length; i++)
            if (buf[offset + i] != pat[i])
                return false;
        return true;
    }

    private static byte[] hex(String s) {
        s = s.replaceAll("\\s+", "");
        byte[] out = new byte[s.length() / 2];
        for (int i = 0; i < out.length; i++)
            out[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
        return out;
    }

    private static void exec(String... cmd) throws Exception {
        Process p = new ProcessBuilder(cmd).redirectErrorStream(true).start();
        try (var r = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            while (r.readLine() != null) {
            }
        }
        int rc = p.waitFor();
        if (rc != 0)
            throw new IOException("Command failed: " + String.join(" ", cmd) + " (rc=" + rc + ")");
    }

    private static void deleteRec(Path p) throws IOException {
        if (Files.isDirectory(p))
            try (var s = Files.list(p)) {
                s.forEach(x -> {
                    try {
                        deleteRec(x);
                    } catch (Exception ignore) {
                    }
                });
            }
        Files.deleteIfExists(p);
    }
}
