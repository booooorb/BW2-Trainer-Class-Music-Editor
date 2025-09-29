package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import model.MusicNames;

public class MusicCodes {
    private static final int[] MUSIC = new int[236];

    static {
        MUSIC[0] = 0x6A;
        MUSIC[1] = 0x6A;
        MUSIC[2] = 0x6A;
        MUSIC[3] = 0x6A;
        MUSIC[4] = 0x6A;
        MUSIC[5] = 0x6A;
        MUSIC[6] = 0x6A;
        MUSIC[7] = 0x6A;
        MUSIC[8] = 0x6A;
        MUSIC[9] = 0x6A;
        MUSIC[10] = 0x6A;
        MUSIC[11] = 0x6A;
        MUSIC[12] = 0x6A;
        MUSIC[13] = 0x6A;
        MUSIC[14] = 0x6A;
        MUSIC[15] = 0x6A;
        MUSIC[16] = 0x6A;
        MUSIC[17] = 0x6A;
        MUSIC[18] = 0x6A;
        MUSIC[19] = 0x6A;
        MUSIC[20] = 0x6A;
        MUSIC[21] = 0x6A;
        MUSIC[22] = 0x6A;
        MUSIC[23] = 0x6A;
        MUSIC[24] = 0x6A;
        MUSIC[25] = 0x6D;
        MUSIC[26] = 0x6A;
        MUSIC[27] = 0x71;
        MUSIC[28] = 0x6A;
        MUSIC[29] = 0x6A;
        MUSIC[30] = 0x6A;
        MUSIC[31] = 0x6A;
        MUSIC[32] = 0x6A;
        MUSIC[33] = 0x6A;
        MUSIC[34] = 0x6A;
        MUSIC[35] = 0x6A;
        MUSIC[36] = 0x6A;
        MUSIC[37] = 0x6A;
        MUSIC[38] = 0x6D;
        MUSIC[39] = 0x6A;
        MUSIC[40] = 0x71;
        MUSIC[41] = 0x6A;
        MUSIC[42] = 0x6A;
        MUSIC[43] = 0x6A;
        MUSIC[44] = 0x6A;
        MUSIC[45] = 0x6A;
        MUSIC[46] = 0x6A;
        MUSIC[47] = 0x6A;
        MUSIC[48] = 0x6A;
        MUSIC[49] = 0x6A;
        MUSIC[50] = 0x6A;
        MUSIC[51] = 0x6A;
        MUSIC[52] = 0x6A;
        MUSIC[53] = 0x6A;
        MUSIC[54] = 0x6A;
        MUSIC[55] = 0x6A;
        MUSIC[56] = 0x6A;
        MUSIC[57] = 0x6A;
        MUSIC[58] = 0x6A;
        MUSIC[59] = 0x6A;
        MUSIC[60] = 0x6A;
        MUSIC[61] = 0x6A;
        MUSIC[62] = 0x6A;
        MUSIC[63] = 0x6A;
        MUSIC[64] = 0x6A;
        MUSIC[65] = 0x6A;
        MUSIC[66] = 0x6A;
        MUSIC[67] = 0x6A;
        MUSIC[68] = 0x6A;
        MUSIC[69] = 0x6A;
        MUSIC[70] = 0x6A;
        MUSIC[71] = 0x6A;
        MUSIC[72] = 0x6A;
        MUSIC[73] = 0x6A;
        MUSIC[74] = 0x6A;
        MUSIC[75] = 0x6A;
        MUSIC[76] = 0x6A;
        MUSIC[77] = 0x6A;
        MUSIC[78] = 0x6F;
        MUSIC[79] = 0x6F;
        MUSIC[80] = 0x6F;
        MUSIC[81] = 0x6F;
        MUSIC[82] = 0x6A;
        MUSIC[83] = 0x6A;
        MUSIC[84] = 0x6A;
        MUSIC[85] = 0x6A;
        MUSIC[86] = 0x6A;
        MUSIC[87] = 0x6A;
        MUSIC[88] = 0x6A;
        MUSIC[89] = 0x6A;
        MUSIC[90] = 0x6A;
        MUSIC[91] = 0x6A;
        MUSIC[92] = 0x6A;
        MUSIC[93] = 0x6A;
        MUSIC[94] = 0x6A;
        MUSIC[95] = 0x6A;
        MUSIC[96] = 0x6A;
        MUSIC[97] = 0x6A;
        MUSIC[98] = 0x6A;
        MUSIC[99] = 0x6A;
        MUSIC[100] = 0x79;
        MUSIC[101] = 0x6A;
        MUSIC[102] = 0x6A;
        MUSIC[103] = 0x6A;
        MUSIC[104] = 0x6A;
        MUSIC[105] = 0x6A;
        MUSIC[106] = 0x6A;
        MUSIC[107] = 0x6A;
        MUSIC[108] = 0x6C;
        MUSIC[109] = 0x6C;
        MUSIC[110] = 0x6C;
        MUSIC[111] = 0x6C;
        MUSIC[112] = 0x6C;
        MUSIC[113] = 0x6C;
        MUSIC[114] = 0x6C;
        MUSIC[115] = 0x6C;
        MUSIC[116] = 0x6A;
        MUSIC[117] = 0x6A;
        MUSIC[118] = 0x6A;
        MUSIC[119] = 0x6A;
        MUSIC[120] = 0x6A;
        MUSIC[121] = 0x6A;
        MUSIC[122] = 0x6A;
        MUSIC[123] = 0x6A;
        MUSIC[124] = 0x6A;
        MUSIC[125] = 0x6A;
        MUSIC[126] = 0x6A;
        MUSIC[127] = 0x6A;
        MUSIC[128] = 0x6A;
        MUSIC[129] = 0x6A;
        MUSIC[130] = 0x6A;
        MUSIC[131] = 0x6A;
        MUSIC[132] = 0x6A;
        MUSIC[133] = 0x6A;
        MUSIC[134] = 0x6A;
        MUSIC[135] = 0x6A;
        MUSIC[136] = 0x6A;
        MUSIC[137] = 0x6A;
        MUSIC[138] = 0x6A;
        MUSIC[139] = 0x6A;
        MUSIC[140] = 0x6A;
        MUSIC[141] = 0x6A;
        MUSIC[142] = 0x6A;
        MUSIC[143] = 0x6A;
        MUSIC[144] = 0x6A;
        MUSIC[145] = 0xEE;
        MUSIC[146] = 0x6A;
        MUSIC[147] = 0x6A;
        MUSIC[148] = 0x6A;
        MUSIC[149] = 0x6A;
        MUSIC[150] = 0x6A;
        MUSIC[151] = 0x6A;
        MUSIC[152] = 0x6A;
        MUSIC[153] = 0x6A;
        MUSIC[154] = 0x6A;
        MUSIC[155] = 0x6A;
        MUSIC[156] = 0x6A;
        MUSIC[157] = 0x6A;
        MUSIC[158] = 0x6A;
        MUSIC[159] = 0x6A;
        MUSIC[160] = 0x6A;
        MUSIC[161] = 0x6A;
        MUSIC[162] = 0x6A;
        MUSIC[163] = 0x6A;
        MUSIC[164] = 0x6A;
        MUSIC[165] = 0x6A;
        MUSIC[166] = 0x6A;
        MUSIC[167] = 0x6A;
        MUSIC[168] = 0x6A;
        MUSIC[169] = 0x6A;
        MUSIC[170] = 0x6A;
        MUSIC[171] = 0x6A;
        MUSIC[172] = 0x6A;
        MUSIC[173] = 0x6A;
        MUSIC[174] = 0x6A;
        MUSIC[175] = 0x6A;
        MUSIC[176] = 0x6A;
        MUSIC[177] = 0x6A;
        MUSIC[178] = 0x6A;
        MUSIC[179] = 0x6A;
        MUSIC[180] = 0x6A;
        MUSIC[181] = 0x6A;
        MUSIC[182] = 0x6A;
        MUSIC[183] = 0x6A;
        MUSIC[184] = 0x6A;
        MUSIC[185] = 0x6A;
        MUSIC[186] = 0xEA;
        MUSIC[187] = 0xE9;
        MUSIC[188] = 0xE9;
        MUSIC[189] = 0xEB;
        MUSIC[190] = 0x6A;
        MUSIC[191] = 0xF3;
        MUSIC[192] = 0xE9;
        MUSIC[193] = 0xEC;
        MUSIC[194] = 0x6A;
        MUSIC[195] = 0x6A;
        MUSIC[196] = 0x6A;
        MUSIC[197] = 0x6D;
        MUSIC[198] = 0x6A;
        MUSIC[199] = 0x6A;
        MUSIC[200] = 0x6A;
        MUSIC[201] = 0x6A;
        MUSIC[202] = 0x6A;
        MUSIC[203] = 0x6A;
        MUSIC[204] = 0x6A;
        MUSIC[205] = 0x6A;
        MUSIC[206] = 0x6A;
        MUSIC[207] = 0x6A;
        MUSIC[208] = 0x6A;
        MUSIC[209] = 0x6A;
        MUSIC[210] = 0x6A;
        MUSIC[211] = 0x6A;
        MUSIC[212] = 0x6A;
        MUSIC[213] = 0x6A;
        MUSIC[214] = 0x6A;
        MUSIC[215] = 0x6A;
        MUSIC[216] = 0x6A;
        MUSIC[217] = 0x6A;
        MUSIC[218] = 0x6A;
        MUSIC[219] = 0x6A;
        MUSIC[220] = 0x6A;
        MUSIC[221] = 0x6A;
        MUSIC[222] = 0x6A;
        MUSIC[223] = 0x6A;
        MUSIC[224] = 0x6A;
        MUSIC[225] = 0x6A;
        MUSIC[226] = 0x6A;
        MUSIC[227] = 0xED;
        MUSIC[228] = 0x6A;
        MUSIC[229] = 0x6A;
        MUSIC[230] = 0x6A;
        MUSIC[231] = 0x6A;
        MUSIC[232] = 0x6A;
        MUSIC[233] = 0x6A;
        MUSIC[234] = 0x6A;
        MUSIC[235] = 0xEA;
    }

    public static int musicCodeForIndex(int index) {
        if (index < 0 || index >= MUSIC.length)
            return 0x6A;
        int n = MUSIC[index];
        return n & 0xFF;
    }

    // Hex label conversions
    public static String hexLabel(int code) {
        return String.format("0x%02X", code & 0xFF);
    }

    public static List<MusicOption> options() {
        List<MusicOption> out = new ArrayList<>(0xF8 + 1);
        for (int code = 0x00; code <= 0xF8; code++) {
            out.add(new MusicOption(code, hexLabel(code)));
        }
        // VS* first, then by hex code ascending
        out.sort((a, b) -> {
            boolean avs = isVs(a.getCode());
            boolean bvs = isVs(b.getCode());
            if (avs != bvs)
                return avs ? -1 : 1; // VS group first
            return Integer.compare(a.getCode(), b.getCode());
        });
        return out;
    }

    private static boolean isVs(int code) {
        String name = MusicNames.musicNameForCode(code);
        if (name == null)
            return false;
        String s = name.toUpperCase(Locale.ROOT);
        // Catch VS: "SEQ_BGM_VS_*", "_VS_", or " VS "
        return s.startsWith("SEQ_BGM_VS_") || s.contains("_VS_") || s.contains(" VS ");
    }

}
