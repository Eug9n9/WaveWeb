package ca.wave.web.form;

import java.io.Serializable;

public class Analysis implements Serializable {
	private static final long serialVersionUID = 1L;
	private String analysisId;
	private String circuitId;
	private String reportPath;
	private Long launchTime;
	private Long timeLeft;
	private boolean success;

	public String getAnalysisId() {
		return analysisId;
	}

	public void setAnalysisId(String analysisId) {
		this.analysisId = analysisId;
	}

	public String getCircuitId() {
		return circuitId;
	}

	public void setCircuitId(String circuitId) {
		this.circuitId = circuitId;
	}

	public String getReportPath() {
		return reportPath;
	}

	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
	}

	public Long getLaunchTime() {
		return launchTime;
	}

	public void setLaunchTime(Long launchTime) {
		this.launchTime = launchTime;
	}

	public Long getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(Long timeLeft) {
		this.timeLeft = timeLeft;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
