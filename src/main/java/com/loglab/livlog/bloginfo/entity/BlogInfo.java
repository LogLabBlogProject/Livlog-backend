package com.loglab.livlog.bloginfo.entity;

import com.loglab.livlog.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "blog_info")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class BlogInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}
