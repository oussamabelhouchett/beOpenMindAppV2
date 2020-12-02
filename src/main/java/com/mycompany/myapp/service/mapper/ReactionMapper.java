package com.mycompany.myapp.service.mapper;


import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.ReactionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reaction} and its DTO {@link ReactionDTO}.
 */
@Mapper(componentModel = "spring", uses = {PostMapper.class, ApplicationUserMapper.class})
public interface ReactionMapper extends EntityMapper<ReactionDTO, Reaction> {

    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "user.id", target = "userId")
    ReactionDTO toDto(Reaction reaction);

    @Mapping(source = "postId", target = "post")
    @Mapping(source = "userId", target = "user")
    Reaction toEntity(ReactionDTO reactionDTO);

    default Reaction fromId(Long id) {
        if (id == null) {
            return null;
        }
        Reaction reaction = new Reaction();
        reaction.setId(id);
        return reaction;
    }
}
