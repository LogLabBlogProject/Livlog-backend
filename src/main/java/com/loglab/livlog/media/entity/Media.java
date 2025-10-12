package com.loglab.livlog.media.entity;

import com.loglab.livlog.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "media")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileType;
    private String url;
    private Long size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
