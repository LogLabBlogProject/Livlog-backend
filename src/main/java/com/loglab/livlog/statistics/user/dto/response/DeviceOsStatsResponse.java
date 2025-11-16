package com.loglab.livlog.statistics.user.dto.response;

import lombok.*;

import java.util.List;

/**
 * 접속 기기(Device)와 OS 분포 통계를 담는 응답 DTO
 *
 * @author : 000flag
 * @fileName : DeviceOsStatsResponse
 * @since : 2025.11.05
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceOsStatsResponse {
    /**
     * 기기 유형(MOBILE, DESKTOP 등)별 사용 빈도
     */
    private List<DeviceRatio> deviceStats;

    /**
     * OS(WINDOWS, MAC, ANDROID, IOS 등)별 사용 빈도
     */
    private List<OsRatio> osStats;

    /**
     * 기기 통계
     */
    @Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
    public static class DeviceRatio {
        /**
         * 기기 타입: MOBILE / DESKTOP
         */
        private String deviceType;

        /**
         * 해당 기기 타입의 접속 횟수
         */
        private Long count;
    }

    /**
     * OS 통계
     */
    @Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
    public static class OsRatio {
        /**
         * OS 종류: WINDOWS, MAC, ANDROID, IOS, LINUX 등
         */
        private String osType;

        /**
         * 해당 OS의 접속 횟수
         */
        private Long count;
    }
}
