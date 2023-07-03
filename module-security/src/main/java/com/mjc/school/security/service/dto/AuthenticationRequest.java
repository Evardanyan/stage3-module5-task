package com.mjc.school.security.service.dto;

import lombok.Data;

public record AuthenticationRequest(String email, String password) {
}
