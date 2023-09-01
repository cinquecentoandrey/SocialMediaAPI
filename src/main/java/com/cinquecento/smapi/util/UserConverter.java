package com.cinquecento.smapi.util;

import com.cinquecento.smapi.dto.UserDTO;
import com.cinquecento.smapi.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    private final ModelMapper modelMapper;

    @Autowired
    public UserConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User convertToUser(UserDTO userDTO) {
        return this.modelMapper.map(userDTO, User.class);
    }

    public UserDTO convertToUserDTO(User user) { return this.modelMapper.map(user, UserDTO.class); }
}
