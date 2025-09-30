package com.loglab.livlog.user.entity;

import com.loglab.livlog.auth.oauth.dto.OAuth2UserInfo;
import com.loglab.livlog.auth.oauth.entity.OAuthProvider;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    private String profile;
    private String username;
    private String nickname;
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OAuthProvider provider;
    private String providerId;

    @Builder.Default
    @Column(nullable = false)
    private boolean emailVerified = true;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public User update(OAuth2UserInfo oAuth2UserInfo) {
        this.providerId = oAuth2UserInfo.getId();
        this.email = oAuth2UserInfo.getEmail();
        this.username = oAuth2UserInfo.getName();
        this.profile = oAuth2UserInfo.getImageUrl();
        return this;
    }
}
