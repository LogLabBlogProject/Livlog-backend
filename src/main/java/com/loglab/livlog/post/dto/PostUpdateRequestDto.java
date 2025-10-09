package com.loglab.livlog.post.dto;

import com.loglab.livlog.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateRequestDto {
    private Long id;
    private String title;
    private String content;

    public Post toEntity() {
        return Post.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .build();
    }
}
