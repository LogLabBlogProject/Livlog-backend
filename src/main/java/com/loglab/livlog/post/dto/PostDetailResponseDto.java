package com.loglab.livlog.post.dto;

import com.loglab.livlog.post.entity.Post;
import com.loglab.livlog.tag.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<String> tagNames; // Tag 엔티티 대신 이름만

    public PostDetailResponseDto(Post entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
        // Tag 이름만 추출
        this.tagNames = entity.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());
    }
}
