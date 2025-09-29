package core;

public class BlackRomBytes extends AbstractRomBytes {
    @Override
    public String name() {
        return "Black 2";
    }

    // overlay_0036 @ 0x46BA0 block (re-index by trainer class)
    @Override
    protected long ov36_block_offset() {
        return 0x46BA0L;
    }

    @Override
    protected byte[] ov36_block_replacement() {
        return new byte[] {
                (byte) 0x1C, (byte) 0x49, (byte) 0x82, (byte) 0x00, (byte) 0x89, (byte) 0x5C, (byte) 0x21, (byte) 0x70,
                (byte) 0x6A, (byte) 0xF6, (byte) 0x0C, (byte) 0xFB, (byte) 0x07, (byte) 0x1C, (byte) 0x01, (byte) 0x98,
                (byte) 0x6A, (byte) 0xF6, (byte) 0xF6, (byte) 0xFA, (byte) 0x0E, (byte) 0x2F, (byte) 0x04, (byte) 0xD2,
                (byte) 0x03, (byte) 0xB0
        };
    }

    // 0x46C0C: 20 80 -> 04 48 (NOP trainers-without-intro)
    @Override
    protected long ov36_nop_offset() {
        return 0x46C0CL;
    }

    @Override
    protected byte[] ov36_nop_bytes() {
        return new byte[] { (byte) 0x04, (byte) 0x48 };
    }

    // 0x46C14: E4 28 1D 02 -> 95 23 09 02 (repoint table)
    @Override
    protected long ov36_repoint_offset() {
        return 0x46C14L;
    }

    @Override
    protected byte[] ov36_repoint_bytes() {
        return new byte[] { (byte) 0x95, (byte) 0x23, (byte) 0x09, (byte) 0x02 };
    }

    // y9.bin: at 0x49C, 58 E9 03 -> 5C E9 03
    @Override
    protected long y9_sizefix_offset() {
        return 0x49CL;
    }

    @Override
    protected byte[] y9_sizefix_bytes() {
        return new byte[] { (byte) 0x5C, (byte) 0xE9, (byte) 0x03 };
    }

    // New ARM9 table base (decompressed arm9) 
    @Override
    protected long arm9_table_base() {
        return 0x008E394L;
    } // AA GG EE EE * 236
}