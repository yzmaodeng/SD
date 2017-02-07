package com.sd.duib;

public class CreditConsumeResult {

	private boolean success;
	private String errorMessage = "";
	private String bizId = "";
	private Integer credits = 0;// 用户积分余额

	public CreditConsumeResult(boolean success) {
		this.success = success;
	}

	public String toString() {
		if (success) {
			return "{'status':'ok','errorMessage':'','bizId':'" + bizId
					+ "','credits':'" + credits + "'}";
		} else {
			return "{'status':'fail','errorMessage':'" + errorMessage
					+ "','credits':'" + credits + "'}";
		}
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public Integer getCredits() {
		return credits;
	}

	public void setCredits(Integer credits) {
		this.credits = credits;
	}

}
