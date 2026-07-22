package com.insurance.backend.constant;

public final class MessageConstants {

    private MessageConstants() {
    }

    public static final String CUSTOMER_NOT_FOUND = "Customer not found with id: ";

    public static final String EMAIL_ALREADY_EXISTS = "Email already exists.";

    public static final String PHONE_ALREADY_EXISTS = "Phone number already exists.";

    public static final String POLICY_NOT_FOUND = "Policy not found with id: ";

    public static final String INVALID_POLICY_DATE =
            "Policy start date cannot be after end date.";
}