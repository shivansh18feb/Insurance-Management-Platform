package com.insurance.backend.constant;

public final class ValidationConstants {

    private ValidationConstants() {
    }

    public static final int NAME_MAX_LENGTH = 50;
    public static final int EMAIL_MAX_LENGTH = 100;
    public static final int PHONE_LENGTH = 10;
    public static final int ADDRESS_MAX_LENGTH = 255;
    public static final int CITY_MAX_LENGTH = 100;
    public static final int STATE_MAX_LENGTH = 100;
    public static final int POSTAL_CODE_MAX_LENGTH = 6;

    public static final String NAME_REGEX = "^[A-Za-z ]+$";
    public static final String PHONE_REGEX = "^[6-9]\\d{9}$";
    public static final String POSTAL_CODE_REGEX = "^\\d{6}$";

    public static final String REQUIRED_FIELD = "This field is required.";

    public static final String INVALID_EMAIL = "Please enter a valid email address.";

    public static final String INVALID_PHONE =
            "Phone number must contain exactly 10 digits.";

    public static final String INVALID_NAME =
            "Name must contain only alphabets and spaces.";

    public static final String INVALID_POSTAL_CODE =
            "Postal code must contain exactly 6 digits.";

    public static final String INVALID_DATE =
            "Date cannot be in the future.";
}