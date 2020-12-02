package com.mycompany.myapp.service.mapper;


import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.CommentsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Comments} and its DTO {@link CommentsDTO}.
 */
@Mapper(componentModel = "spring", uses = {PostMapper.class})
public interface CommentsMapper extends EntityMapper<CommentsDTO, Comments> {

    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "post.id", target = "postId")
    CommentsDTO toDto(Comments comments);

    @Mapping(target = "users", ignore = true)
    @Mapping(target = "removeUser", ignore = true)
    @Mapping(source = "parentId", target = "parent")
    @Mapping(source = "postId", target = "post")
    Comments toEntity(CommentsDTO commentsDTO);

    default Comments fromId(Long id) {
        if (id == null) {
            return null;
        }
        Comments comments = new Comments();
        comments.setId(id);
        return comments;
    }
}
