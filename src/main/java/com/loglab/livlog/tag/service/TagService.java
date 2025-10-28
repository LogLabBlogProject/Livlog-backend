package com.loglab.livlog.tag.service;

import com.loglab.livlog.tag.dto.TagDto;
import com.loglab.livlog.tag.entity.Tag;
import com.loglab.livlog.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public List<TagDto> getAllTags() {
        return tagRepository.findAll().stream()
                .map(tag -> new TagDto(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
    }

    public TagDto createTag(TagDto dto) {
        Tag tag = tagRepository.save(Tag.builder().name(dto.getName()).build());
        return new TagDto(tag.getId(), tag.getName());
    }
}
