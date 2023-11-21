package qmmt.model;

public enum QuantumGatesEnum {
        CCX("CCX",
                        new String[] { "ccx", "cp", "crx", "cry", "crz", "cswap", "rxx", "ryy", "rzx",
                                        "rzz", "u2" }, "c-c-x"),
        CH("CH", new String[] { "ch", "csx", "cx", "cy", "cz", "dcx", "iswap", "ms", "rx", "ry", "rz", "swap",
                        "u1" }, "c-h"),

        CRX("CRX",
                        new String[] { "ccx", "cp", "crx", "cry", "crz", "cswap", "rxx", "ryy", "rzx",
                                        "rzz", "u2" }, "c-r-x"),
        CRY("CRY",
                        new String[] { "ccx", "cp", "crx", "cry", "crz", "cswap", "rxx", "ryy", "rzx",
                                        "rzz", "u2" }, "c-r-y"),
        CRZ("CRZ",
                        new String[] { "ccx", "cp", "crx", "cry", "crz", "cswap", "rxx", "ryy", "rzx",
                                        "rzz", "u2" }, "c-r-z"),
        CSWAP("CSWAP",
                        new String[] { "ccx", "cp", "crx", "cry", "crz", "cswap", "rxx", "ryy", "rzx",
                                        "rzz", "u2" }, "c-swap-swap"),
        CSX("CSX",
                        new String[] { "ch", "csx", "cx", "cy", "cz", "dcx", "iswap", "ms", "rx", "ry", "rz",
                                        "swap", "u1" }, "c-sx"),
        CU("CU", new String[] { "u3" }, "cu"),
        CU3("CU3", new String[] { "u" }, "cu3"),
        CX("CX", new String[] { "ch", "csx", "cx", "cy", "cz", "dcx", "iswap", "ms", "rx", "ry", "rz", "swap",
                        "u1" }, "c-x"),
        CY("CY", new String[] { "ch", "csx", "cx", "cy", "cz", "dcx", "iswap", "ms", "rx", "ry", "rz", "swap",
                        "u1" }, "c-y"),
        CZ("CZ", new String[] { "ch", "csx", "cx", "cy", "cz", "dcx", "iswap", "ms", "rx", "ry", "rz", "swap",
                        "u1" }, "c-z"),
        DCX("DCX",
                        new String[] { "ch", "csx", "cx", "cy", "cz", "dcx", "iswap", "ms", "rx", "ry", "rz",
                                        "swap", "u1" }, "d-cx"),
        H("H", new String[] { "id", "t", "tdg", "x", "h", "i", "sdg", "s", "y", "z", "sx" }, "h"),
        I("I", new String[] { "t", "tdg", "x", "h", "i", "sx", "sdg", "s", "y", "z", "id" }, "i"),
        ID("ID", new String[] { "t", "tdg", "x", "h", "i", "sx", "sdg", "s", "y", "z", "id" }, "id"),
        ISWAP("ISWAP",
                        new String[] { "ch", "csx", "cx", "cy", "cz", "dcx", "iswap", "ms", "rx", "ry", "rz",
                                        "swap", "u1" }, "i-swap"),
        MS("MS", new String[] { "ch", "csx", "cx", "cy", "cz", "dcx", "iswap", "ms", "rx", "ry", "rz", "swap",
                        "u1" }, "m-s"),
        RX("RX", new String[] { "ch", "csx", "cx", "cy", "cz", "dcx", "iswap", "ms", "rx", "ry", "rz", "swap",
                        "u1" }, "r-x"),
        RXX("RXX",
                        new String[] { "ccx", "cp", "crx", "cry", "crz", "cswap", "rxx", "ryy", "rzx",
                                        "rzz", "u2" },"r-x-x"),
        RY("RY", new String[] { "ch", "csx", "cx", "cy", "cz", "dcx", "iswap", "ms", "rx", "ry", "rz", "swap",
                        "u1" },"r-y"),
        RYY("RYY",
                        new String[] { "ccx", "cp", "crx", "cry", "crz", "cswap", "rxx", "ryy", "rzx",
                                        "rzz", "u2" },"r-y-y"),
        RZ("RZ", new String[] { "ch", "csx", "cx", "cy", "cz", "dcx", "iswap", "ms", "rx", "ry", "rz", "swap",
                        "u1" }, "r-z"),
        RZX("RZX",
                        new String[] { "ccx", "cp", "crx", "cry", "crz", "cswap", "rxx", "ryy", "rzx",
                                        "rzz", "u2" },"r-z-x"),
        RZZ("RZZ",
                        new String[] { "ccx", "cp", "crx", "cry", "crz", "cswap", "rxx", "ryy", "rzx",
                                        "rzz", "u2" }, "r-z-z"),
        S("S", new String[] { "t", "tdg", "x", "h", "i", "sx", "sdg", "s", "y", "z", "id" }, "s"),
        SDG("SDG", new String[] { "t", "tdg", "x", "h", "i", "sx", "sdg", "s", "y", "z", "id" }, "sdg"),
        SWAP("SWAP",
                        new String[] { "ch", "csx", "cx", "cy", "cz", "dcx", "iswap", "ms", "rx", "ry", "rz",
                                        "swap", "u1" }, "swap-swap"),
        SX("SX", new String[] { "id", "t", "tdg", "x", "h", "i", "s", "sdg", "y", "z", "sx" }, "sx"),
        T("T", new String[] { "t", "tdg", "x", "h", "i", "sx", "s", "sdg", "y", "z", "id" }, "t"),
        TDG("TDG", new String[] { "t", "tdg", "x", "h", "i", "sx", "s", "sdg", "y", "z", "id" }, "tdg"),
        U("U", new String[] { "u", "u3" }, "u"),
        U1("U1", new String[] { "ch", "csx", "cx", "cy", "cz", "dcx", "iswap", "ms", "rx", "ry", "rz", "swap",
                        "u1" }, "u1-u1"),
        U2("U2", new String[] { "ccx", "cp", "crx", "cry", "crz", "cswap", "rxx", "ryy", "rzx", "rzz",
                        "u2" }, "u2-u2-u2"),
        U3("U3", new String[] {"u", "u3" }, "u3"),
        X("X", new String[] { "x", "t", "tdg", "h", "i", "sx", "s", "sdg", "y", "z", "id" }, "x"),
        Y("Y", new String[] { "t", "tdg", "x", "h", "i", "sx", "s", "sdg", "y", "z", "id" }, "y"),
        Z("Z", new String[] { "t", "tdg", "x", "h", "i", "sx", "s", "sdg", "y", "z", "id" }, "z");

        /*
         * 
         * "ch": {"ch", "csx", "cx", "cy", "cz", "dcx", "iswap", "ms", "rx", "ry",
         * "rz", "swap", "u1"},
         * "cp": {"ccx", "cp", "crx", "cry", "crz", "cswap", "rxx", "ryy",
         * "rzx", "rzz", "u2"},
         * "crx": {"ccx", "cp", "crx", "cry", "crz", "cswap", "rxx", "ryy",
         * "rzx", "rzz", "u2"},
         * "cry": {"ccx", "cp", "crx", "cry", "crz", "cswap", "rxx", "ryy",
         * "rzx", "rzz", "u2"},
         * "crz": {"ccx", "cp", "crx", "cry", "crz", "cswap", "rxx", "ryy",
         * "rzx", "rzz", "u2"},
         * "cswap": {"ccx", "cp", "crx", "cry", "crz", "cswap", "rxx",
         * "ryy", "rzx", "rzz", "u2"},
         * "csx": {"ch", "csx", "cx", "cy", "cz", "dcx", "iswap", "ms", "rx", "ry",
         * "rz", "swap", "u1"},
         * "cu": set(),
         * "cu1": {"ccx", "cp", "crx", "cry", "crz", "cswap", "rxx", "ryy",
         * "rzx", "rzz", "u2"},
         * "cu3": set(),
         * "cx": {"ch", "csx", "cx", "cy", "cz", "dcx", "iswap", "ms", "rx", "ry",
         * "rz", "swap", "u1"},
         * "cy": {"ch", "csx", "cx", "cy", "cz", "dcx", "iswap", "ms", "rx", "ry",
         * "rz", "swap", "u1"},
         * "cz": {"ch", "csx", "cx", "cy", "cz", "dcx", "iswap", "ms", "rx", "ry",
         * "rz", "swap", "u1"},
         * "dcx": {"ch", "csx", "cx", "cy", "cz", "dcx", "iswap", "ms", "rx", "ry",
         * "rz", "swap", "u1"},
         * "h": {"id", "t", "tdg", "x", "h", "i", "sdg", "s", "y", "z", "sx"},
         * "i": {"t", "tdg", "x", "h", "i", "sx", "sdg", "s", "y", "z", "id"},
         * "id": {"t", "tdg", "x", "h", "i", "sx", "sdg", "s", "y", "z", "id"},
         * "iden": set(),
         * "iswap": {"ch", "csx", "cx", "cy", "cz", "dcx", "iswap", "ms", "rx",
         * "ry", "rz", "swap", "u1"},
         * "ms": {"ch", "csx", "cx", "cy", "cz", "dcx", "iswap", "ms", "rx", "ry",
         * "rz", "swap", "u1"},
         * "p": {"ch", "csx", "cx", "cy", "cz", "dcx", "iswap", "ms", "rx", "ry",
         * "rz", "swap", "u1"},
         * "r": {"ccx", "cp", "crx", "cry", "crz", "cswap", "rxx", "ryy",
         * "rzx", "rzz", "u2"},
         * "rx": {"ch", "csx", "cx", "cy", "cz", "dcx", "iswap", "ms", "rx", "ry",
         * "rz", "swap", "u1"},
         * "rxx": {"ccx", "cp", "crx", "cry", "crz", "cswap", "rxx", "ryy",
         * "rzx", "rzz", "u2"},
         * "ry": {"ch", "csx", "cx", "cy", "cz", "dcx", "iswap", "ms", "rx", "ry",
         * "rz", "swap", "u1"},
         * "ryy": {"ccx", "cp", "crx", "cry", "crz", "cswap", "rxx", "ryy",
         * "rzx", "rzz", "u2"},
         * "rz": {"ch", "csx", "cx", "cy", "cz", "dcx", "iswap", "ms", "rx", "ry",
         * "rz", "swap", "u1"},
         * "rzx": {"ccx", "cp", "crx", "cry", "crz", "cswap", "rxx", "ryy",
         * "rzx", "rzz", "u2"},
         * "rzz": {"ccx", "cp", "crx", "cry", "crz", "cswap", "rxx", "ryy",
         * "rzx", "rzz", "u2"},
         * "s": {"t", "tdg", "x", "h", "i", "sx", "sdg", "s", "y", "z", "id"},
         * "sdg": {"t", "tdg", "x", "h", "i", "sx", "sdg", "s", "y", "z", "id"},
         * "swap": {"ch", "csx", "cx", "cy", "cz", "dcx", "iswap", "ms", "rx",
         * "ry", "rz", "swap", "u1"},
         * "sx": {"id", "t", "tdg", "x", "h", "i", "s", "sdg", "y", "z", "sx"},
         * "t": {"t", "tdg", "x", "h", "i", "sx", "s", "sdg", "y", "z", "id"},
         * "tdg": {"t", "tdg", "x", "h", "i", "sx", "s", "sdg", "y", "z", "id"},
         * "u": {"u", "u3"},
         * "u1": {"ch", "csx", "cx", "cy", "cz", "dcx", "iswap", "ms", "rx", "ry",
         * "rz", "swap", "u1"},
         * "u2": {"ccx", "cp", "crx", "cry", "crz", "cswap", "rxx", "ryy",
         * "rzx", "rzz", "u2"},
         * "u3": {"u", "u3"},
         * "x": {"x", "t", "tdg", "h", "i", "sx", "s", "sdg", "y", "z", "id"},
         * "y": {"t", "tdg", "x", "h", "i", "sx", "s", "sdg", "y", "z", "id"},
         * "z": {"t", "tdg", "x", "h", "i", "sx", "s", "sdg", "y", "z", "id"}}
         */
        String quantumGate;
        String[] equivalences;
        String division;

        QuantumGatesEnum(String quantumGate, String[] equivalences, String division) {
                this.quantumGate = quantumGate;
                this.equivalences = equivalences;
                this.division = division;       
        }

        public String[] getEquivalences() {
                return equivalences;
        }

        public void setEquivalences(String[] equivalences) {
                this.equivalences = equivalences;
        }

        public String getQuantumGate() {
                return quantumGate;
        }

        public void setQuantumGate(String quantumGate) {
                this.quantumGate = quantumGate;
        }

        public String getDivision() {
                return division;
        }

        public void setDivision(String division) {
                this.division = division;
        }
}
