package com.loglab.livlog.tag.service;

import com.loglab.livlog.tag.dto.TagDto;
import com.loglab.livlog.tag.entity.Tag;
import com.loglab.livlog.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {
    private final TagRepository tagRepository;

    public List<TagDto> getAllTags() {
        return tagRepository.findAll().stream()
                .map(tag -> new TagDto(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
    }

    public TagDto getById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found: " + id));
        return new TagDto(tag.getId(), tag.getName());
    }

    @Transactional
    public TagDto createTag(TagDto dto) {
        Tag tag = tagRepository.save(Tag.builder().name(dto.getName()).build());
        return new TagDto(tag.getId(), tag.getName());
    }

    @Transactional
    public TagDto updateTag(Long id, TagDto dto) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found: " + id));
        tag.setName(dto.getName());
        Tag updated = tagRepository.save(tag);
        return new TagDto(updated.getId(), updated.getName());
    }

    @Transactional
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found: " + id));
        tagRepository.delete(tag);
    }
}
