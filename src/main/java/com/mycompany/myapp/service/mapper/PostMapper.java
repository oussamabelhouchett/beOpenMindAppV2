package com.mycompany.myapp.service.mapper;


import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.PostDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Post} and its DTO {@link PostDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PostMapper extends EntityMapper<PostDTO, Post> {


    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "removeComments", ignore = true)
    @Mapping(target = "filesPosts", ignore = true)
    @Mapping(target = "removeFilesPost", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "removeUser", ignore = true)
    Post toEntity(PostDTO postDTO);

    default Post fromId(Long id) {
        if (id == null) {
            return null;
        }
        Post post = new Post();
        post.setId(id);
        return post;
    }
}
