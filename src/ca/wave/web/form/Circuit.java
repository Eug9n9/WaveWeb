package ca.wave.web.form;

import java.io.Serializable;

public class Circuit implements Serializable {
	private static final long serialVersionUID = 1L;
	private String circuitId;

	public String getCircuitId() {
		return circuitId;
	}

	public void setCircuitId(String cid) {
		this.circuitId = cid;
	}
}
