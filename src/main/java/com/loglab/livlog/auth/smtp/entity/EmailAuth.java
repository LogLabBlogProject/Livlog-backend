package com.loglab.livlog.auth.smtp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 이메일 인증 Entitiy
 *
 * @author : 000flag
 * @fileName : EmailAuth
 * @since : 2025.10.14
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String receiver;   // 이메일 주소
    private String subject;    // 제목
    private String code;       // 인증 코드

    @Column(columnDefinition = "TEXT")
    private String content;    // 메일 내용 (HTML)

    private Boolean verified = false;   // 인증 성공 여부
    private Boolean activated = true;   // 활성 상태

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    // 인증 성공 처리
    public void verify() {
        this.verified = true;
        this.activated = false;
        this.deletedAt = LocalDateTime.now();
    }

    // 만료 처리
    public void deactivate() {
        this.activated = false;
        this.deletedAt = LocalDateTime.now();
    }
}
