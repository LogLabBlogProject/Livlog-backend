package com.loglab.livlog.user.controller;

import com.loglab.livlog.global.dto.CommonResponse;
import com.loglab.livlog.user.dto.UserRequestDto;
import com.loglab.livlog.user.dto.UserResponseDto;
import com.loglab.livlog.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "사용자 관리 API")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "사용자 ID로 조회",
            description = "특정 사용자의 정보를 조회합니다"
    )
    @GetMapping("/{userId}")
    public ResponseEntity<CommonResponse<?>> getUser(@PathVariable Long userId) {
        UserResponseDto response = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(response));
    }

    @Operation(
            summary = "이메일로 사용자 조회",
            description = "이메일로 사용자 정보를 조회합니다"
    )
    @GetMapping("/email/{email}")
    public ResponseEntity<CommonResponse<?>> getUserByEmail(@PathVariable String email) {
        UserResponseDto response = userService.getUserByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(response));
    }

    @Operation(
            summary = "프로필 업데이트",
            description = "사용자 프로필 정보를 업데이트합니다"
    )
    @PutMapping("/{userId}/profile")
    public ResponseEntity<CommonResponse<?>> updateProfile(
            @PathVariable Long userId,
            @RequestBody UserRequestDto requestDto) {
        UserResponseDto response = userService.updateProfile(userId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(response));
    }

    @Operation(
            summary = "비밀번호 변경",
            description = "사용자 비밀번호를 변경합니다"
    )
    @PutMapping("/{userId}/password")
    public ResponseEntity<CommonResponse<?>> changePassword(
            @PathVariable Long userId,
            @RequestBody Map<String, String> passwords) {
        String oldPassword = passwords.get("oldPassword");
        String newPassword = passwords.get("newPassword");
        userService.changePassword(userId, oldPassword, newPassword);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("비밀번호가 변경되었습니다"));
    }

    @Operation(
            summary = "사용자 삭제",
            description = "사용자를 삭제합니다"
    )
    @DeleteMapping("/{userId}")
    public ResponseEntity<CommonResponse<?>> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success("사용자가 삭제되었습니다"));
    }
}
