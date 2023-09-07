package com.mrInstruments.backend.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorInfo {

	   @JsonProperty("status_code")
	   private int statusCode;
	   @JsonProperty("uri")
	   private String uriRequested;
	   @JsonProperty("message")
	   private String message;

//	   public ErrorInfo(ApiException exception, String uriRequested) {
//	       this.message = exception.getMessage();
//	       this.statusCode = exception.getStatusCode().value();
//	       this.uriRequested = uriRequested;
//	   }

	   public ErrorInfo(int statusCode, String uriRequested, String message) {
		   this.statusCode = statusCode;
		   this.uriRequested = uriRequested;
		   this.message = message;
	   }

	   public String getMessage() {
	       return message;
	   }

	   public int getStatusCode() {
	       return statusCode;
	   }

	   public String getUriRequested() {
	       return uriRequested;
	   }

	}
