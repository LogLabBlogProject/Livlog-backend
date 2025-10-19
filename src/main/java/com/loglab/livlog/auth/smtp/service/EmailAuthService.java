package com.loglab.livlog.auth.smtp.service;

import com.loglab.livlog.auth.smtp.Repository.MailAuthRepository;
import com.loglab.livlog.auth.smtp.dto.request.EmailAuthRequestDto;
import com.loglab.livlog.auth.smtp.dto.request.EmailAuthVerifyRequestDto;
import com.loglab.livlog.auth.smtp.dto.response.EmailAuthSendResponseDto;
import com.loglab.livlog.auth.smtp.dto.response.EmailAuthVerifyResponseDto;
import com.loglab.livlog.auth.smtp.entity.EmailAuth;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * 이메일 인증코드 서비스
 *
 * @author : 000flag
 * @fileName : EmailAuthService
 * @since : 2025.10.19
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailAuthService {

    private final JavaMailSender mailSender;
    private final MailAuthRepository mailAuthRepository;
    private final TemplateEngine templateEngine;

    /**
     * 이메일 인증코드 발송
     *
     * @param dto EmailAuthRequestDto (receiver: 이메일 주소)
     * @return EmailAuthSendResponseDto (receiver, success, message)
     */
    public EmailAuthSendResponseDto sendAuthCode(EmailAuthRequestDto dto) {
        String email = dto.getReceiver();
        String code = String.format("%06d", new Random().nextInt(999999));
        String subject = "[LIVLOG] 이메일 인증코드";

        // Thymeleaf 템플릿 데이터 설정
        Context context = new Context();
        context.setVariable("code", code);
        context.setVariable("email", email);

        // HTML 템플릿 렌더링
        String htmlContent = templateEngine.process("email-auth", context);

        try {
            // 메일 발송
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);

            // DB 저장
            EmailAuth mail = EmailAuth.builder()
                    .receiver(email)
                    .subject(subject)
                    .content(htmlContent)
                    .code(code)
                    .createdAt(LocalDateTime.now())
                    .build();
            mailAuthRepository.save(mail);

            log.info("[이메일 인증] 코드 발송 완료: {}", email);

            return EmailAuthSendResponseDto.builder()
                    .receiver(email)
                    .success(true)
                    .message("인증코드 발송 완료")
                    .build();

        } catch (Exception e) {
            log.error("[이메일 인증] 발송 실패: {}", e.getMessage());
            return EmailAuthSendResponseDto.builder()
                    .receiver(email)
                    .success(false)
                    .message("이메일 발송 실패")
                    .build();
        }
    }

    /**
     * 인증코드 검증
     *
     * @param dto EmailAuthVerifyRequestDto (email: 인증 대상 이메일, code: 사용자가 입력한 인증코드)
     * @return EmailAuthVerifyResponseDto (receiver, verified, message 필드 포함 — verified=true 시 인증 성공)
     */
    public EmailAuthVerifyResponseDto verifyCode(EmailAuthVerifyRequestDto dto) {
        EmailAuth emailAuth = mailAuthRepository.findTopByReceiverOrderByCreatedAtDesc(dto.getReceiver())
                .orElseThrow(() -> new RuntimeException("인증 요청 내역이 없습니다."));

        boolean isValid = emailAuth.getCode().equals(dto.getCode())
                && emailAuth.getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(5));

        if (isValid) {
            emailAuth.verify();
            mailAuthRepository.save(emailAuth);
            log.info("[이메일 인증] 성공: {}", dto.getReceiver());
            return EmailAuthVerifyResponseDto.builder()
                    .receiver(dto.getReceiver())
                    .verified(true)
                    .message("이메일 인증에 성공했습니다.")
                    .build();
        } else {
            log.warn("[이메일 인증] 실패: {}", dto.getReceiver());
            return EmailAuthVerifyResponseDto.builder()
                    .receiver(dto.getReceiver())
                    .verified(false)
                    .message("인증코드가 올바르지 않거나 만료되었습니다.")
                    .build();
        }
    }
}
