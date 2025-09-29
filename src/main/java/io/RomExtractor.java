package io;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;


public final class RomExtractor {
    private RomExtractor() {
    }

    public static Result extract(Path rom, Path repoRoot, Path workDir) throws Exception {
        Files.createDirectories(workDir);

        Tools tools = resolveTools(repoRoot);
        requireFile(rom, "ROM (.nds) not found: ");
        requireFile(tools.ndstool, "ndstool missing: ");
        requireFile(tools.blz, "blz missing: ");

        // ndstool extract
        int x = run(List.of(
                tools.ndstool.toString(), "-x", rom.toString(),
                "-9", "arm9.bin",
                "-7", "arm7.bin",
                "-y9", "y9.bin",
                "-y7", "y7.bin",
                "-d", "data",
                "-y", "overlay",
                "-t", "banner.bin",
                "-h", "header.bin"), workDir);
        ensureZero(x, "ndstool extract failed, exit=" + x);

        // Paths produced by ndstool:
        Path arm9 = workDir.resolve("arm9.bin");
        Path arm7 = workDir.resolve("arm7.bin");
        Path y9 = workDir.resolve("y9.bin");
        Path y7 = workDir.resolve("y7.bin");
        Path dataDir = workDir.resolve("data");
        Path overlayDir = workDir.resolve("overlay");
        Path banner = workDir.resolve("banner.bin");
        Path header = workDir.resolve("header.bin");

        // Decompress ARM9 to arm9.decomp.bin
        Path arm9Decomp = workDir.resolve("arm9.decomp.bin");
        Files.copy(arm9, arm9Decomp, StandardCopyOption.REPLACE_EXISTING);
        int d1 = run(List.of(tools.blz.toString(), "-d", arm9Decomp.toString()), workDir);
        ensureZero(d1, "blz -d failed on arm9.decomp.bin, exit=" + d1);

        // Decompress overlay_0036 to overlay_0036.decomp.bin
        Path overlay36 = findOverlay36(overlayDir); // overlay/overlay_0036.bin
        Path overlay36Decomp = workDir.resolve("overlay_0036.decomp.bin");
        Files.copy(overlay36, overlay36Decomp, StandardCopyOption.REPLACE_EXISTING);
        int d2 = run(List.of(tools.blz.toString(), "-d", overlay36Decomp.toString()), workDir);
        ensureZero(d2, "blz -d failed on overlay_0036.decomp.bin, exit=" + d2);

        return new Result(workDir, tools.ndstool, tools.blz,
                arm9, arm9Decomp, arm7, y9, y7, dataDir, overlayDir, overlay36, overlay36Decomp, banner, header);
    }

    // Recompress overlay_0036: copies the decomp back onto overlay/overlay_0036.bin, then blz -en on it.
    public static void recompressOverlay36(Result r) throws Exception {
        Files.copy(r.overlay36Decomp, r.overlay36, StandardCopyOption.REPLACE_EXISTING);
        int c = run(List.of(r.blz.toString(), "-en", r.overlay36.toString()), r.workDir);
        ensureZero(c, "blz -en failed on overlay_0036.bin, exit=" + c);
    }

    // Recompress ARM9 with BLZ (-en9 or -eo9). Returns the recompressed file path (arm9.recomp.bin).
    public static Path recompressArm9(Result r, boolean optimal) throws Exception {
        Path out = r.workDir.resolve("arm9.recomp.bin");
        Files.copy(r.arm9Decomp, out, StandardCopyOption.REPLACE_EXISTING);
        String flag = optimal ? "-eo9" : "-en9";
        int c = run(List.of(r.blz.toString(), flag, out.toString()), r.workDir);
        ensureZero(c, "blz " + flag + " failed on arm9.recomp.bin, exit=" + c);
        return out;
    }

    // Rebuild using decompressed ARM9 (emulator-friendly; avoids size drift issues).
    public static Path rebuildRom(Result r, String outName) throws Exception {
        return rebuildRom(r, outName, r.arm9Decomp);
    }

    // Rebuild using a specific ARM9 (e.g., a recompressed one from recompressArm9).
    public static Path rebuildRom(Result r, String outName, Path arm9ForBuild) throws Exception {
        int b = run(List.of(
                r.ndstool.toString(), "-c", outName,
                "-9", arm9ForBuild.toString(),
                "-7", r.arm7.toString(),
                "-y9", r.y9.toString(),
                "-y7", r.y7.toString(),
                "-d", r.dataDir.toString(),
                "-y", r.overlayDir.toString(),
                "-t", r.banner.toString(),
                "-h", r.header.toString()), r.workDir);
        ensureZero(b, "ndstool build failed, exit=" + b);
        return r.workDir.resolve(outName);
    }


    public static final class Result {
        public final Path workDir;
        public final Path ndstool, blz;
        public final Path arm9, arm9Decomp, arm7, y9, y7;
        public final Path dataDir, overlayDir;
        public final Path overlay36, overlay36Decomp;
        public final Path banner, header;

        private Result(Path workDir, Path ndstool, Path blz,
                Path arm9, Path arm9Decomp, Path arm7, Path y9, Path y7,
                Path dataDir, Path overlayDir, Path overlay36, Path overlay36Decomp,
                Path banner, Path header) {
            this.workDir = workDir;
            this.ndstool = ndstool;
            this.blz = blz;
            this.arm9 = arm9;
            this.arm9Decomp = arm9Decomp;
            this.arm7 = arm7;
            this.y9 = y9;
            this.y7 = y7;
            this.dataDir = dataDir;
            this.overlayDir = overlayDir;
            this.overlay36 = overlay36;
            this.overlay36Decomp = overlay36Decomp;
            this.banner = banner;
            this.header = header;
        }
    }

    private static final class Tools {
        final Path ndstool, blz;

        Tools(Path n, Path b) {
            ndstool = n;
            blz = b;
        }
    }

    private static Tools resolveTools(Path repoRoot) {
        // Allow override via env var (e.g., BW2_TOOL_DIR=C:\path\to\tools)
        String override = System.getenv("BW2_TOOL_DIR");
        Path base = (override != null && !override.isBlank())
                ? Paths.get(override)
                : repoRoot.resolve("tools");

        boolean win = System.getProperty("os.name").toLowerCase().contains("win");
        Path nds = base.resolve(win ? "ndstool.exe" : "ndstool");
        Path blz = base.resolve(win ? "blz.exe" : "blz");
        return new Tools(nds, blz);
    }

    private static void requireFile(Path p, String msg) throws FileNotFoundException {
        if (p == null || !Files.isRegularFile(p))
            throw new FileNotFoundException(msg + p);
    }

    private static void ensureZero(int code, String err) throws IOException {
        if (code != 0)
            throw new IOException(err);
    }

    private static int run(List<String> cmd, Path cwd) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(cmd);
        if (cwd != null)
            pb.directory(cwd.toFile());
        pb.redirectErrorStream(true);
        Process p = pb.start();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))) {
            for (String line; (line = r.readLine()) != null;)
                System.out.println(line);
        }
        return p.waitFor();
    }

    private static Path findOverlay36(Path overlayDir) throws IOException {
        Path expected = overlayDir.resolve("overlay_0036.bin");
        if (Files.isRegularFile(expected))
            return expected;
        // Fallback: try to find by pattern if the name varies
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(overlayDir, "overlay_0036*.bin")) {
            for (Path p : ds)
                return p;
        }
        throw new FileNotFoundException("overlay_0036.bin not found under " + overlayDir);
    }
}
