package com.loglab.livlog.media.service;

import com.loglab.livlog.media.dto.MediaDto;
import com.loglab.livlog.media.entity.Media;
import com.loglab.livlog.media.repository.MediaRepository;
import com.loglab.livlog.post.entity.Post;
import com.loglab.livlog.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MediaService {

    private final MediaRepository mediaRepository;
    private final PostRepository postRepository;

    private static final String UPLOAD_DIR = "uploads/media/";

    public List<MediaDto> getByPostId(Long postId) {
        List<Media> mediaList = mediaRepository.findByPostId(postId);
        return mediaList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public MediaDto getById(Long id) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Media not found: " + id));
        return convertToDto(media);
    }

    @Transactional
    public MediaDto uploadFile(MultipartFile file, Long postId) {
        try {
            // 파일 검증
            if (file.isEmpty()) {
                throw new RuntimeException("Empty file");
            }

            // Post 조회
            Post post = null;
            if (postId != null) {
                post = postRepository.findById(postId)
                        .orElseThrow(() -> new RuntimeException("Post not found: " + postId));
            }

            // 파일 저장 경로 생성
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 고유 파일명 생성
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            // 파일 저장
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath);

            // Media 엔티티 생성
            Media media = Media.builder()
                    .fileName(originalFilename)
                    .fileType(file.getContentType())
                    .url("/uploads/media/" + uniqueFilename)
                    .size(file.getSize())
                    .post(post)
                    .build();

            Media saved = mediaRepository.save(media);
            return convertToDto(saved);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }

    @Transactional
    public void delete(Long id) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Media not found: " + id));

        // 파일 삭제
        try {
            Path filePath = Paths.get(media.getUrl().substring(1)); // Remove leading '/'
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + e.getMessage());
        }

        mediaRepository.delete(media);
    }

    private MediaDto convertToDto(Media media) {
        return MediaDto.builder()
                .id(media.getId())
                .fileName(media.getFileName())
                .fileType(media.getFileType())
                .url(media.getUrl())
                .postId(media.getPost() != null ? media.getPost().getId() : null)
                .build();
    }
}
