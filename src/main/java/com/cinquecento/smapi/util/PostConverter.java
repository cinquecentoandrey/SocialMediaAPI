package com.cinquecento.smapi.util;

import com.cinquecento.smapi.dto.PostDTO;
import com.cinquecento.smapi.model.Post;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PostConverter {

    private final ModelMapper modelMapper;

    public PostConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Post convertToPost(PostDTO postDTO) {
        return modelMapper.map(postDTO, Post.class);
    }

    public PostDTO convertToPostDTO(Post post) {
        return modelMapper.map(post, PostDTO.class);
    }
}
