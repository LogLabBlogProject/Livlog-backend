package com.loglab.livlog.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatSendRequestDto {
    @NotBlank
    private String text;
}
