package com.loglab.livlog.statistics.user.controller;

import com.loglab.livlog.global.dto.CommonResponse;
import com.loglab.livlog.statistics.user.dto.response.DailySignUpResponse;
import com.loglab.livlog.statistics.user.dto.response.DeviceOsStatsResponse;
import com.loglab.livlog.statistics.user.dto.request.UserStatsRequest;
import com.loglab.livlog.statistics.user.dto.response.UserTotalCountResponse;
import com.loglab.livlog.statistics.user.service.UserStatsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 관리자 대시보드에서 사용하는 사용자 통계(User Statistics) Controller
 *
 * @author : 000flag
 * @fileName : UserStatsController
 * @since : 2025.11.05
 */
@RestController
@RequestMapping("/api/admin/users/stats")
@RequiredArgsConstructor
public class UserStatsController {
    private final UserStatsService userStatsService;

    /**
     * 일간 가입자 수 통계를 조회한다.
     *
     * @param request 기간(startDate, endDate) 정보를 포함하는 요청 DTO
     * @return 날짜별 가입 수 리스트를 CommonResponse 형태로 반환
     */
    @Operation(
            summary = "일간 가입자 수 조회",
            description = "선택한 기간(startDate ~ endDate) 동안의 날짜별 신규 가입자 수를 반환합니다."
    )
    @PostMapping("/daily-sign-ups")
    public ResponseEntity<CommonResponse<List<DailySignUpResponse>>> getDailySignUps(@RequestBody UserStatsRequest request) {
        List<DailySignUpResponse> data =
                userStatsService.getDailySignUps(request.getStartDate(), request.getEndDate());

        return ResponseEntity.ok(CommonResponse.success(data));
    }

    /**
     * 전체 사용자 누적 수를 조회한다.
     *
     * @param request 조회 종료일(endDate)을 포함하는 요청 DTO
     * @return endDate 기준 전체 누적 사용자 수를 CommonResponse 형태로 반환
     */
    @Operation(
            summary = "전체 사용자 누적 수 조회",
            description = "지정된 날짜(endDate) 기준으로 서비스에 가입한 전체 누적 사용자 수를 반환합니다."
    )
    @PostMapping("/total-users")
    public ResponseEntity<CommonResponse<UserTotalCountResponse>> getTotalUsers(@RequestBody UserStatsRequest request) {
        UserTotalCountResponse data =
                userStatsService.getTotalUsers(request.getEndDate());

        return ResponseEntity.ok(CommonResponse.success(data));
    }

    /**
     * 접속 기기(Device) / OS 통계를 조회한다.
     * <p>
     * user_device_log 테이블의 데이터를 기반으로 기기 타입/OS별 집계 결과를 반환한다.
     *
     * @param request 기간(startDate, endDate)을 포함하는 요청 DTO
     * @return 기기/OS 비율 리스트를 CommonResponse 형태로 반환
     */
    @PostMapping("/device-os")
    public ResponseEntity<CommonResponse<DeviceOsStatsResponse>> getDeviceOsStats(@RequestBody UserStatsRequest request) {
        DeviceOsStatsResponse data =
                userStatsService.getDeviceOsStats(request.getStartDate(), request.getEndDate());

        return ResponseEntity.ok(CommonResponse.success(data));
    }
}
