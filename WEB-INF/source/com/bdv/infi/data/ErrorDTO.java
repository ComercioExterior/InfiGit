package com.bdv.infi.data;

public class ErrorDTO {

	String errorMessageValue;
	String errorMessageKey;

//	ErrorDTO(String errorMessageValue, String errorMessageKey) {
//		this.errorMessageValue = errorMessageValue;
//		this.errorMessageKey = errorMessageKey;
//	}

	public String getErrorMessageValue() {
		return errorMessageValue;
	}

	public void setErrorMessageValue(String errorMessageValue) {
		this.errorMessageValue = errorMessageValue;
	}

	public String getErrorMessageKey() {
		return errorMessageKey;
	}

	public void setErrorMessageKey(String errorMessageKey) {
		this.errorMessageKey = errorMessageKey;
	}

}
