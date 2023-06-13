package qmmt.model;

public enum UmlEnum {
	QUANTUM_GATE("uml:CallOperationAction"), QUBIT("uml:ActivityPartition");

	private String type;

	private UmlEnum(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
