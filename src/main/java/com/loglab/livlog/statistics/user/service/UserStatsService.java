package com.loglab.livlog.statistics.user.service;

import com.loglab.livlog.statistics.user.dto.response.DailySignUpResponse;
import com.loglab.livlog.statistics.user.dto.response.DeviceOsStatsResponse;
import com.loglab.livlog.statistics.user.dto.response.UserTotalCountResponse;
import com.loglab.livlog.statistics.user.repository.UserDeviceLogRepository;
import com.loglab.livlog.statistics.user.repository.UserStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 관리자 대시보드에서 필요한 사용자 관련 통계 데이터를 제공하는 Service
 *
 * @author : 000flag
 * @fileName : UserStatsService
 * @since : 2025.11.05
 */
@Service
@RequiredArgsConstructor
public class UserStatsService {
    private final UserStatisticsRepository userStatisticsRepository;
    private final UserDeviceLogRepository deviceLogRepository;

    /**
     * 특정 기간(start ~ end) 동안의 일별 신규 가입자 수를 조회
     *
     * Repository 결과(Object[])를 DailySignUpResponse DTO로 변환하여 Controller로 전달
     *
     * @param start 조회 시작일
     * @param end   조회 종료일
     * @return 일별 가입자 수 리스트
     */
    public List<DailySignUpResponse> getDailySignUps(LocalDateTime start, LocalDateTime end) {
        return userStatisticsRepository.getDailySignUps(start, end)
                .stream()
                .map(obj -> new DailySignUpResponse(
                        obj[0].toString(),   // 날짜(Date → String)
                        (Long) obj[1]        // 가입자 수(Long)
                ))
                .collect(Collectors.toList());
    }

    /**
     * 특정 날짜(endDate) 기준 전체 누적 사용자 수를 반환
     *
     * @param end 조회 종료일
     * @return 전체 누적 가입자 수 DTO 객체
     */
    public UserTotalCountResponse getTotalUsers(LocalDateTime end) {
        long count = userStatisticsRepository.countByCreatedAtLessThanEqual(end);
        return new UserTotalCountResponse(count);
    }

    /**
     * 접속 기록(user_device_log)을 기반으로 기기(Device)와 OS 통계를 조회
     *
     * Repository 결과(Object[])를 DeviceRatio / OsRatio DTO로 변환하여 반환
     *
     * @param start 조회 시작일
     * @param end   조회 종료일
     * @return 기기/OS 통계를 포함한 DeviceOsStatsResponse 객체
     */
    public DeviceOsStatsResponse getDeviceOsStats(LocalDateTime start, LocalDateTime end) {

        // 기기(DeviceType) 통계 변환
        var device = deviceLogRepository.getDeviceStats(start, end)
                .stream()
                .map(obj -> new DeviceOsStatsResponse.DeviceRatio(
                        obj[0].toString(),   // 기기 타입
                        (Long) obj[1]        // 해당 기기 접속 횟수
                ))
                .collect(Collectors.toList());

        // OS 통계 변환
        var os = deviceLogRepository.getOsStats(start, end)
                .stream()
                .map(obj -> new DeviceOsStatsResponse.OsRatio(
                        obj[0].toString(),   // OS 타입
                        (Long) obj[1]        // 해당 OS 접속 횟수
                ))
                .collect(Collectors.toList());

        // DTO 조립 후 반환
        return DeviceOsStatsResponse.builder()
                .deviceStats(device)
                .osStats(os)
                .build();
    }
}
