package com.loglab.livlog.postlike.entity;

import com.loglab.livlog.post.entity.Post;
import com.loglab.livlog.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_like")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class PostLike {

    @EmbeddedId
    private PostLikeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;
}
