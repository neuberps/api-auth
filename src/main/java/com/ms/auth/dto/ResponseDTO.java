package com.ms.auth.dto;

import com.ms.auth.model.User;

public record ResponseDTO (User user, String token) {
}
