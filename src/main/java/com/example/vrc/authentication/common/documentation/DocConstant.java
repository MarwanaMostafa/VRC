package com.example.vrc.authentication.common.documentation;

public interface DocConstant {
    interface AuthenticationConstants {
        String API_NAME = "Authentication";
        String API_CONTACTUS_NAME = "Contact Us.";
        String API_CONTACTUS_DESCRIPTION = "Create Complaint.";
        String API_DESCRIPTION = "Manage Authentication flow for user ";
        String API_POST_SIGN_UP_VALUES = "Register to VRC.";
        String API_POST_SIGN_UP_DESCRIPTION = "Register users to enable them to log in to VRC.";
        String API_POST_LOG_IN_VALUES = "Log in to VRC.";
        String API_POST_LOG_IN_DESCRIPTION = "Log in for VRC using Email and Password.";

        String API_GET_AUTO_LOG_IN_VALUES = "Auto Log in to VRC.";
        String API_GET_AUTO_LOG_IN_DESCRIPTION = "Auto Log in to VRC.";

        String API_PUT_FORGET_PASSWORD_VALUES = "Forget Password.";
        String API_PUT_FORGET_PASSWORD_DESCRIPTION = "Forget Password Should provide email to send verification email.";

        String API_PUT_SET_PASSWORD_VALUES = "Set New Password.";
        String API_PUT_SET_PASSWORD_DESCRIPTION = "Set new password after open verification email.";

    }
}
