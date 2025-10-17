package com.loglab.livlog.chat.dto;

import lombok.Data;

@Data
public class ChatSendRequestDto {
    @NotBlank
    private String text;
}
