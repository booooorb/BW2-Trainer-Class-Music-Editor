package core;

import io.RomExtractor;
import io.BinaryPatcher;

import java.nio.file.Path;

abstract class AbstractRomBytes implements RomPatcher {
    // Version-specific constants:
    protected abstract long ov36_block_offset();

    protected abstract byte[] ov36_block_replacement(); // 26 byte

    protected abstract long ov36_nop_offset();

    protected abstract byte[] ov36_nop_bytes(); // 2 bytes

    protected abstract long ov36_repoint_offset();

    protected abstract byte[] ov36_repoint_bytes(); // 4 bytes (addr)

    protected abstract long y9_sizefix_offset();

    protected abstract byte[] y9_sizefix_bytes(); // 3 bytes

    protected abstract long arm9_table_base(); // where AA GG EE EE[0] starts

    protected int arm9_entry_stride() {
        return 4;
    }

    protected int arm9_music_field_offset() { // overwrite GG with 1-byte music
        return 1;
    }

    @Override
    public Path patch(Path rom, Path repoRoot, Path workDir, int[] musicByIndex) throws Exception {
        // Extract and decompress
        RomExtractor.Result r = RomExtractor.extract(rom, repoRoot, workDir);

        // overlay_36 edits
        BinaryPatcher.write(r.overlay36Decomp, ov36_block_offset(), ov36_block_replacement());
        BinaryPatcher.write(r.overlay36Decomp, ov36_nop_offset(), ov36_nop_bytes());
        BinaryPatcher.write(r.overlay36Decomp, ov36_repoint_offset(), ov36_repoint_bytes());

        // y9 edits (compressed-size table fix)
        BinaryPatcher.write(r.y9, y9_sizefix_offset(), y9_sizefix_bytes());

        // Write music table in ARM9 (decompressed)
        writeMusicTable(r.arm9Decomp, arm9_table_base(), arm9_entry_stride(), arm9_music_field_offset(), musicByIndex);

        // Recompress overlay_0036 and rebuild (using decompressed arm9)
        RomExtractor.recompressOverlay36(r);
        return RomExtractor.rebuildRom(r, outRomName());
    }

    protected String outRomName() {
        return "patched.nds";
    }

    protected void writeMusicTable(Path arm9Decomp, long base, int stride, int field, int[] music) throws Exception {
        for (int i = 0; i < music.length; i++) {
            long off = base + (long) i * stride + field;
            io.BinaryPatcher.write(arm9Decomp, off, (byte) (music[i] & 0xFF));
        }
    }
}
