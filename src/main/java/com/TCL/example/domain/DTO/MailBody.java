package com.TCL.example.domain.DTO;

import lombok.Builder;

@Builder
public record MailBody(String to, String subject, String content) {
}
