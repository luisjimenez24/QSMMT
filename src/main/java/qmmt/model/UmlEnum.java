package qmmt.model;

public enum UmlEnum {
	ONE_QUBIT_QUANTUM_GATE(new String[] {"uml:CallOperationAction"}), QUBIT(new String[] {"uml:ActivityPartition"}), TWO_QUBIT_QUANTUM_GATE(new String [] {"uml:AcceptEventAction", "uml:SendSignalAction"});

	private String [] type;

	private UmlEnum(String[] type) {
		this.type = type;
	}

	public String[] getTypes() {
		return type;
	}
}
