package com.mycompany.myapp.service.mapper;


import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.PostDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Post} and its DTO {@link PostDTO}.
 */
@Mapper(componentModel = "spring", uses = {ApplicationUserMapper.class,UserMapper.class})
public interface PostMapper extends EntityMapper<PostDTO, Post> {

    @Mapping(source = "user.id", target = "userAppId")
    @Mapping(source = "user.user.id", target = "userId")

    @Mapping(source = "user.user.firstName", target = "firstName")

    @Mapping(source = "user.user.lastName", target = "lastName")

    @Mapping(source = "user.user.imageUrl", target = "imageUrl")

    PostDTO toDto(Post post);

    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "removeComments", ignore = true)
    @Mapping(target = "filesPosts", ignore = true)
    @Mapping(target = "removeFilesPost", ignore = true)
    @Mapping(source = "userId", target = "user")
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
