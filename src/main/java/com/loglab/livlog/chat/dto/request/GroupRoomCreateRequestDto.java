package com.loglab.livlog.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class GroupRoomCreateRequestDto {
    @NotBlank
    private String title;

    @Size(min = 1)
    private List<Long> memberIds; // 생성 시 포함할 사용자들(본인 제외 or 포함, 정책에 맞게)
}