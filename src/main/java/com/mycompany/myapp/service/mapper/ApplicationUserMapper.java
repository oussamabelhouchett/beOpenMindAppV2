package com.mycompany.myapp.service.mapper;


import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.ApplicationUserDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ApplicationUser} and its DTO {@link ApplicationUserDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, PostMapper.class, CommentsMapper.class, ReactionMapper.class})
public interface ApplicationUserMapper extends EntityMapper<ApplicationUserDTO, ApplicationUser> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "comments.id", target = "commentsId")
    @Mapping(source = "reaction.id", target = "reactionId")
    ApplicationUserDTO toDto(ApplicationUser applicationUser);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "postId", target = "post")
    @Mapping(source = "commentsId", target = "comments")
    @Mapping(source = "reactionId", target = "reaction")
    ApplicationUser toEntity(ApplicationUserDTO applicationUserDTO);

    default ApplicationUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setId(id);
        return applicationUser;
    }
}
