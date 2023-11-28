package qmmt.model;

public enum XMIElementsEnum {
    ID("xmi:id"), QUML_PROFILE_CONTROLLED_QUBIT("QuantumUMLProfile:ControlledQubit"), QUML_PROFILE_QUANTUM_GATE("QuantumUMLProfile:QuantumGate");

    private String value;

	private XMIElementsEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
