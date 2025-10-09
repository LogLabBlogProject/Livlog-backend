package com.loglab.livlog.user.dto;

import com.loglab.livlog.auth.oauth.entity.OAuthProvider;
import com.loglab.livlog.user.entity.Role;
import com.loglab.livlog.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String email;
    private String profile;
    private String nickname;
    private Role role;
    private OAuthProvider provider;
    private boolean emailVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** Custom Constructor, Entity → ResponseDTO 변환 */
    public UserResponseDto(User entity) {
        this.id = entity.getId();
        this.email = entity.getEmail();
        this.profile = entity.getProfile();
        this.nickname = entity.getNickname();
        this.role = entity.getRole();
        this.provider = entity.getProvider();
        this.emailVerified = entity.isEmailVerified();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
