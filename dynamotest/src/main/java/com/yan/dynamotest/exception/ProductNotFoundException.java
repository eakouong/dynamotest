package com.yan.dynamotest.exception;

public class ProductNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public ProductNotFoundException(String message) {
		super("error :" + message);
	}

}
