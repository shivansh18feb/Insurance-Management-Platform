package com.insurance.backend.constant;

public final class ApiConstants {

    private ApiConstants() {
    }

    public static final String API_BASE_PATH = "/api";

    public static final String CUSTOMERS = API_BASE_PATH + "/customers";

    public static final String POLICIES = API_BASE_PATH + "/policies";
    public static final String AGENTS = API_BASE_PATH + "/agents";
    public static final String CLAIMS = API_BASE_PATH + "/claims";
    public static final String PAYMENTS = API_BASE_PATH + "/payments";
    public static final String PREMIUMS = API_BASE_PATH + "/premiums";
}