package com.mycompany.myapp.service.mapper;


import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.FilesPostDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link FilesPost} and its DTO {@link FilesPostDTO}.
 */
@Mapper(componentModel = "spring", uses = {PostMapper.class})
public interface FilesPostMapper extends EntityMapper<FilesPostDTO, FilesPost> {

    @Mapping(source = "filesPost.id", target = "filesPostId")
    FilesPostDTO toDto(FilesPost filesPost);

    @Mapping(source = "filesPostId", target = "filesPost")
    FilesPost toEntity(FilesPostDTO filesPostDTO);

    default FilesPost fromId(Long id) {
        if (id == null) {
            return null;
        }
        FilesPost filesPost = new FilesPost();
        filesPost.setId(id);
        return filesPost;
    }
}
