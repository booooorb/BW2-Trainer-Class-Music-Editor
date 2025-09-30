package io;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

// runs external tool
public final class ExternalTools {
    private ExternalTools() {
    }

    public static int run(List<String> cmd, Path workDir) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(cmd);
        if (workDir != null)
            pb.directory(workDir.toFile());
        pb.redirectErrorStream(true);
        Process p = pb.start();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null)
                System.out.println(line);
        }
        return p.waitFor();
    }
}
