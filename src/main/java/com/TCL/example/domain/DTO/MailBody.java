package com.TCL.example.domain.DTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MailBody {
    private String to;
    private String subject;
    private String content;
}
