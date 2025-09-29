package core;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import ui.EditorUI;

public interface RomPatcher {
    // Run full extract -> patch -> rebuild flow and return output .nds path.
    Path patch(Path rom, Path repoRoot, Path workDir, int[] musicByIndex) throws Exception;


    String name();
}
