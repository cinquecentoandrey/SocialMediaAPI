package com.cinquecento.smapi.util;


import com.cinquecento.smapi.dto.CommentDTO;
import com.cinquecento.smapi.model.Comment;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter {

    private final ModelMapper modelMapper;


    @Autowired
    public CommentConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Comment convertToComment(CommentDTO commentDTO) {
        return modelMapper.map(commentDTO, Comment.class);
    }

    public CommentDTO convertToCommentDTO(Comment comment) {
        return modelMapper.map(comment, CommentDTO.class);
    }
}
