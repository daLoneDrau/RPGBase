package com.dalonedrow.rpg.base.flyweights;

/**
 * @author drau
 */
public enum ErrorMessage {
	/** bad parameters. */
	BAD_PARAMETERS("Message1"),
	/** illegal access. */
	ILLEGAL_ACCESS("Illegal Acess"),
	/** illegal object type. */
	ILLEGAL_OBJECT_TYPE("Illegal Object Type"),
	/** illegal operation. */
	ILLEGAL_OPERATION("Illegal Operation"),
	/** invalid argument. */
	INTERNAL_BAD_ARGUMENT("Invalid Argument was passed to the method"),
	/** internal error. */
	INTERNAL_ERROR("Internal Error has occured"),
	/** invalid data format. */
	INVALID_DATA_FORMAT("Unable to parse the given data value"),
	/** invalid data type. */
	INVALID_DATA_TYPE("Unable to process the give data type"),
	/** invalid field. */
	INVALID_FIELD("Invalid field"),
	/** invalid operand. */
	INVALID_OPERAND("Invalid Operant was passed as parameter"),
	/** invalid operation. */
	INVALID_OPERATION("Invalid Operation"),
	/** invalid parameter. */
	INVALID_PARAM("Invalid parameter was passed"),
	/** invalid path parameters. */
	INVALID_PATH_PARAM("Invalid path parameters"),
	/** invalid resource type. */
	INVALID_RESOURCE_TYPE("Invalid Resource Type"),
	/** missing id. */
	MISSING_ID("ID is missing"),
	/** missing operand. */
	MISSING_OPERAND("Missing operand that was passed as parameter"),
	/** multiple records found. */
	MULTIPLE_RECORD_FOUND("Multiple Records found"),
	/** no record found. */
	NO_RECORD_FOUND("No Record was found"),
	/** can create revision of deleted record. */
	POST_DELETED_REC("Can create a revision of deleted record"),
	/** no data for POST operation. */
	POST_INDATA("POST Operations Requires Data");
	/** the user message. */
	private final String userMessage;
	/**
	 * Creates a new instance of {@link ErrorMessage}.
	 * @param usrMsg the user message
	 */
	ErrorMessage(final String usrMsg) {
		userMessage = usrMsg;
	}
	/**
	 * Gets the user message.
	 * @return {@link String}
	 */
	public String getUserMessage() {
		return userMessage;
	}
}
