package com.notification.template;

public enum HtmlTemplateType {
    // Auth
    GENERATED_OTP, // using
    LOGIN_ALERT, // using
    RESET_PASSWORD, // using
    REGISTRATION_SUCCESS,
    WELCOME_EMAIL, // using

    // User
    USER_UPDATED,
    USER_DELETED,
    USER_ROLE_UPDATED,
    USER_PASSWORD_UPDATED,

    // Booking
    BOOKING_CONFIRMATION,
    BOOKING_CANCELLED,
    BOOKING_REMINDER,

}
