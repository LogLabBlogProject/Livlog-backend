package com.loglab.livlog.chat.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ReadMarkRequestDto {
    @NotNull
    private Long upToMessageId;
}