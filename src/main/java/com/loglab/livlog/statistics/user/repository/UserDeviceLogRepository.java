package com.loglab.livlog.statistics.user.repository;

import com.loglab.livlog.statistics.user.entity.UserDeviceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 사용자 접속 기록(user_device_log) 테이블을 조회하는 Repository
 *
 * @author : 000flag
 * @fileName : UserDeviceLogRepository
 * @since : 2025.11.05
 */
@Repository
public interface UserDeviceLogRepository extends JpaRepository<UserDeviceLog, Long> {
    /**
     * 기기(DeviceType)별 접속 횟수 통계 조회
     *
     * @param start 조회 시작일
     * @param end   조회 종료일
     * @return Object[] -> [0] DeviceType, [1] count
     */
    @Query("SELECT d.deviceType, COUNT(d) " +
            "FROM UserDeviceLog d " +
            "WHERE d.createdAt BETWEEN :start AND :end " +
            "GROUP BY d.deviceType")
    List<Object[]> getDeviceStats(LocalDateTime start, LocalDateTime end);

    /**
     * OS(OsType)별 접속 횟수 통계 조회
     *
     * @param start 조회 시작일
     * @param end   조회 종료일
     * @return Object[] -> [0] OsType, [1] count
     */
    @Query("SELECT d.osType, COUNT(d) " +
            "FROM UserDeviceLog d " +
            "WHERE d.createdAt BETWEEN :start AND :end " +
            "GROUP BY d.osType")
    List<Object[]> getOsStats(LocalDateTime start, LocalDateTime end);
}
