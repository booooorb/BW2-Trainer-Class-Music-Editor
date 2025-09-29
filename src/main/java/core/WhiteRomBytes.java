package core;

public class WhiteRomBytes extends AbstractRomBytes {
    @Override
    public String name() {
        return "White 2";
    }

    // overlay_0036 @ 0x46B98 block
    @Override
    protected long ov36_block_offset() {
        return 0x46B98L;
    }

    @Override
    protected byte[] ov36_block_replacement() {
        return new byte[] {
                (byte) 0x1C, (byte) 0x49, (byte) 0x82, (byte) 0x00, (byte) 0x89, (byte) 0x5C, (byte) 0x21, (byte) 0x70,
                (byte) 0x6A, (byte) 0xF6, (byte) 0x06, (byte) 0xFB, (byte) 0x07, (byte) 0x1C, (byte) 0x01, (byte) 0x98,
                (byte) 0x6A, (byte) 0xF6, (byte) 0xF0, (byte) 0xFA, (byte) 0x0E, (byte) 0x2F, (byte) 0x04, (byte) 0xD2,
                (byte) 0x03, (byte) 0xB0
        };
    }

    // 0x46C04: 20 80 -> 04 48
    @Override
    protected long ov36_nop_offset() {
        return 0x46C04L;
    }

    @Override
    protected byte[] ov36_nop_bytes() {
        return new byte[] { (byte) 0x04, (byte) 0x48 };
    }

    // 0x46C0C: 18 29 1D 02 -> C1 23 09 02
    @Override
    protected long ov36_repoint_offset() {
        return 0x46C0CL;
    }

    @Override
    protected byte[] ov36_repoint_bytes() {
        return new byte[] { (byte) 0xC1, (byte) 0x23, (byte) 0x09, (byte) 0x02 };
    }

    // y9.bin: at 0x49C, 30 E9 03 -> 34 E9 03
    @Override
    protected long y9_sizefix_offset() {
        return 0x49CL;
    }

    @Override
    protected byte[] y9_sizefix_bytes() {
        return new byte[] { (byte) 0x34, (byte) 0xE9, (byte) 0x03 };
    }

    // Decompressed arm9 table base for W2
    @Override
    protected long arm9_table_base() {
        return 0x008E3C0L;
    }
}
