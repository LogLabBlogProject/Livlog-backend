package com.loglab.livlog.bloginfo.service;

import com.loglab.livlog.bloginfo.dto.BlogInfoDto;
import com.loglab.livlog.bloginfo.entity.BlogInfo;
import com.loglab.livlog.bloginfo.repository.BlogInfoRepository;
import com.loglab.livlog.user.entity.User;
import com.loglab.livlog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlogInfoService {

    private final BlogInfoRepository blogInfoRepository;
    private final UserRepository userRepository;

    public BlogInfoDto getByUserId(Long userId) {
        BlogInfo blogInfo = blogInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("BlogInfo not found for user: " + userId));
        return convertToDto(blogInfo);
    }

    @Transactional
    public BlogInfoDto create(BlogInfoDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found: " + dto.getUserId()));

        // 이미 블로그 정보가 있는지 확인
        if (blogInfoRepository.findByUserId(dto.getUserId()).isPresent()) {
            throw new RuntimeException("BlogInfo already exists for user: " + dto.getUserId());
        }

        BlogInfo blogInfo = BlogInfo.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .user(user)
                .build();

        BlogInfo saved = blogInfoRepository.save(blogInfo);
        return convertToDto(saved);
    }

    @Transactional
    public BlogInfoDto update(Long userId, BlogInfoDto dto) {
        BlogInfo blogInfo = blogInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("BlogInfo not found for user: " + userId));

        blogInfo.setTitle(dto.getTitle());
        blogInfo.setDescription(dto.getDescription());

        BlogInfo updated = blogInfoRepository.save(blogInfo);
        return convertToDto(updated);
    }

    @Transactional
    public void delete(Long userId) {
        BlogInfo blogInfo = blogInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("BlogInfo not found for user: " + userId));
        blogInfoRepository.delete(blogInfo);
    }

    private BlogInfoDto convertToDto(BlogInfo blogInfo) {
        return BlogInfoDto.builder()
                .id(blogInfo.getId())
                .title(blogInfo.getTitle())
                .description(blogInfo.getDescription())
                .userId(blogInfo.getUser().getId())
                .build();
    }
}
