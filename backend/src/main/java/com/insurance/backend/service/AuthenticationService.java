package com.insurance.backend.service;

import com.insurance.backend.dto.request.*;
import com.insurance.backend.dto.response.AuthResponseDto;

public interface AuthenticationService {

    AuthResponseDto register(RegisterRequestDto request);

    AuthResponseDto login(LoginRequestDto request);

    AuthResponseDto refreshToken(RefreshTokenRequestDto request);

    void logout(String refreshToken);

    void changePassword(String email, ChangePasswordRequestDto request);

    String forgotPassword(ForgotPasswordRequestDto request);

    void resetPassword(ResetPasswordRequestDto request);
}