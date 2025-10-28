package com.loglab.livlog.chat.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class GroupRoomCreateRequestDto {
    
    @NotBlank(message = "채팅방 제목은 필수입니다.")
    @Size(min = 1, max = 100, message = "채팅방 제목은 1-100자 사이여야 합니다.")
    private String title;

    @NotEmpty(message = "참여자 목록은 필수입니다.")
    @Size(min = 2, max = 50, message = "참여자는 2-50명 사이여야 합니다.")
    private List<@NotNull(message = "유효하지 않은 사용자 ID입니다.") Long> memberIds;
}