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
public class PostSimpleResponseDto {
    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<String> tagNames;

    public PostSimpleResponseDto(Post entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
        this.tagNames = entity.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());
    }
}
