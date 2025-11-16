package com.loglab.livlog.statistics.user.entity;

import com.loglab.livlog.auth.jwt.entity.DeviceInfo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 사용자 접속 기록(기기 / OS 정보)을 저장하는 Entity
 *
 * @author : 000flag
 * @fileName : UserDeviceLog
 * @since : 2025.11.05
 */
@Entity
@Table(name = "user_device_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDeviceLog {
    /**
     * PK: 접속 기록 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    /**
     * FK: 사용자 ID
     *
     * user_device_log.user_id → user.id
     * LAZY 로딩을 통해 실제 조회 시점에 사용자 정보 로드됨.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private com.loglab.livlog.user.entity.User user;

    /**
     * 접속 기기 유형 (MOBILE / DESKTOP)
     */
    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;

    /**
     * 접속 OS (예: WINDOWS, MAC, ANDROID, IOS 등)
     */
    @Enumerated(EnumType.STRING)
    private OsType osType;

    /**
     * 접속 기록 생성 시간
     */
    private LocalDateTime createdAt;

    /**
     * 사용자 기기 타입 ENUM
     *
     * MOBILE  - 모바일 디바이스(Android, iOS 등)
     * DESKTOP - 데스크탑 환경(Windows, MacOS 등)
     */
    public enum DeviceType {
        MOBILE, DESKTOP
    }

    /**
     * 사용자 운영체제 ENUM
     *
     * WINDOWS - 윈도우 OS
     * MAC     - 맥 OS
     * ANDROID - 안드로이드 기반 모바일 OS
     * IOS     - 애플 iOS
     * LINUX   - 리눅스 기반 OS
     * OTHER   - 위에 해당되지 않는 기타 OS
     */
    public enum OsType {
        WINDOWS, MAC, ANDROID, IOS, LINUX, OTHER
    }
}
