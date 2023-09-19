package qmmt.model;

public enum UmlEnum {
	QUANTUM_GATE(new String[] {"uml:CallOperationAction", "uml:AcceptEventAction", "uml:SendSignalAction"}), QUBIT(new String[] {"uml:ActivityPartition"});

	private String [] type;

	private UmlEnum(String[] type) {
		this.type = type;
	}

	public String[] getTypes() {
		return type;
	}
}
