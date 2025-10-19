package com.loglab.livlog.auth.smtp.Repository;

import com.loglab.livlog.auth.smtp.entity.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 이메일 인증 Repository
 *
 * @author : 000flag
 * @fileName : MailAuthRepository
 * @since : 2025.10.19
 */
@Repository
public interface MailAuthRepository extends JpaRepository<EmailAuth, Long> {

    /**
     * 특정 이메일 주소(receiver)에 대해 가장 최근에 발송된 인증 메일을 조회합니다.
     *
     * @param receiver 수신자 이메일 주소
     * @return Optional<EmailAuth> 최신 인증 메일 정보 (없을 경우 Optional.empty)
     */
    Optional<EmailAuth> findTopByReceiverOrderByCreatedAtDesc(String receiver);
}
