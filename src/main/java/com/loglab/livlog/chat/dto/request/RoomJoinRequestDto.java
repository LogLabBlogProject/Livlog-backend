package com.loglab.livlog.chat.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class RoomJoinRequestDto {
    @NotNull
    private Long memberId;   // 운영/초대 API용. 일반 사용자는 토큰 기반이면 불필요
}