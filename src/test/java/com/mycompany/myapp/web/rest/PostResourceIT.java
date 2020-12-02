package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BeOpenMindAppV2App;
import com.mycompany.myapp.domain.Post;
import com.mycompany.myapp.domain.Comments;
import com.mycompany.myapp.domain.FilesPost;
import com.mycompany.myapp.domain.ApplicationUser;
import com.mycompany.myapp.repository.PostRepository;
import com.mycompany.myapp.repository.search.PostSearchRepository;
import com.mycompany.myapp.service.PostService;
import com.mycompany.myapp.service.dto.PostDTO;
import com.mycompany.myapp.service.mapper.PostMapper;
import com.mycompany.myapp.service.dto.PostCriteria;
import com.mycompany.myapp.service.PostQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PostResource} REST controller.
 */
@SpringBootTest(classes = BeOpenMindAppV2App.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PostResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_PUB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_PUB = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_PUB = LocalDate.ofEpochDay(-1L);

    private static final Instant DEFAULT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_NAME_VISIBALE = false;
    private static final Boolean UPDATED_IS_NAME_VISIBALE = true;

    private static final Boolean DEFAULT_IS_PHOTO_VISIBALE = false;
    private static final Boolean UPDATED_IS_PHOTO_VISIBALE = true;

    private static final Integer DEFAULT_NBRE_LIKE = 1;
    private static final Integer UPDATED_NBRE_LIKE = 2;
    private static final Integer SMALLER_NBRE_LIKE = 1 - 1;

    private static final Integer DEFAULT_NBRE_COMMENTS = 1;
    private static final Integer UPDATED_NBRE_COMMENTS = 2;
    private static final Integer SMALLER_NBRE_COMMENTS = 1 - 1;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostService postService;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.PostSearchRepositoryMockConfiguration
     */
    @Autowired
    private PostSearchRepository mockPostSearchRepository;

    @Autowired
    private PostQueryService postQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPostMockMvc;

    private Post post;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Post createEntity(EntityManager em) {
        Post post = new Post()
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .datePub(DEFAULT_DATE_PUB)
            .time(DEFAULT_TIME)
            .isNameVisibale(DEFAULT_IS_NAME_VISIBALE)
            .isPhotoVisibale(DEFAULT_IS_PHOTO_VISIBALE)
            .nbreLike(DEFAULT_NBRE_LIKE)
            .nbreComments(DEFAULT_NBRE_COMMENTS);
        return post;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Post createUpdatedEntity(EntityManager em) {
        Post post = new Post()
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .datePub(UPDATED_DATE_PUB)
            .time(UPDATED_TIME)
            .isNameVisibale(UPDATED_IS_NAME_VISIBALE)
            .isPhotoVisibale(UPDATED_IS_PHOTO_VISIBALE)
            .nbreLike(UPDATED_NBRE_LIKE)
            .nbreComments(UPDATED_NBRE_COMMENTS);
        return post;
    }

    @BeforeEach
    public void initTest() {
        post = createEntity(em);
    }

    @Test
    @Transactional
    public void createPost() throws Exception {
        int databaseSizeBeforeCreate = postRepository.findAll().size();
        // Create the Post
        PostDTO postDTO = postMapper.toDto(post);
        restPostMockMvc.perform(post("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postDTO)))
            .andExpect(status().isCreated());

        // Validate the Post in the database
        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeCreate + 1);
        Post testPost = postList.get(postList.size() - 1);
        assertThat(testPost.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPost.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testPost.getDatePub()).isEqualTo(DEFAULT_DATE_PUB);
        assertThat(testPost.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testPost.isIsNameVisibale()).isEqualTo(DEFAULT_IS_NAME_VISIBALE);
        assertThat(testPost.isIsPhotoVisibale()).isEqualTo(DEFAULT_IS_PHOTO_VISIBALE);
        assertThat(testPost.getNbreLike()).isEqualTo(DEFAULT_NBRE_LIKE);
        assertThat(testPost.getNbreComments()).isEqualTo(DEFAULT_NBRE_COMMENTS);

        // Validate the Post in Elasticsearch
        verify(mockPostSearchRepository, times(1)).save(testPost);
    }

    @Test
    @Transactional
    public void createPostWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = postRepository.findAll().size();

        // Create the Post with an existing ID
        post.setId(1L);
        PostDTO postDTO = postMapper.toDto(post);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostMockMvc.perform(post("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Post in the database
        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeCreate);

        // Validate the Post in Elasticsearch
        verify(mockPostSearchRepository, times(0)).save(post);
    }


    @Test
    @Transactional
    public void getAllPosts() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList
        restPostMockMvc.perform(get("/api/posts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(post.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].datePub").value(hasItem(DEFAULT_DATE_PUB.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].isNameVisibale").value(hasItem(DEFAULT_IS_NAME_VISIBALE.booleanValue())))
            .andExpect(jsonPath("$.[*].isPhotoVisibale").value(hasItem(DEFAULT_IS_PHOTO_VISIBALE.booleanValue())))
            .andExpect(jsonPath("$.[*].nbreLike").value(hasItem(DEFAULT_NBRE_LIKE)))
            .andExpect(jsonPath("$.[*].nbreComments").value(hasItem(DEFAULT_NBRE_COMMENTS)));
    }
    
    @Test
    @Transactional
    public void getPost() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get the post
        restPostMockMvc.perform(get("/api/posts/{id}", post.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(post.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.datePub").value(DEFAULT_DATE_PUB.toString()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()))
            .andExpect(jsonPath("$.isNameVisibale").value(DEFAULT_IS_NAME_VISIBALE.booleanValue()))
            .andExpect(jsonPath("$.isPhotoVisibale").value(DEFAULT_IS_PHOTO_VISIBALE.booleanValue()))
            .andExpect(jsonPath("$.nbreLike").value(DEFAULT_NBRE_LIKE))
            .andExpect(jsonPath("$.nbreComments").value(DEFAULT_NBRE_COMMENTS));
    }


    @Test
    @Transactional
    public void getPostsByIdFiltering() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        Long id = post.getId();

        defaultPostShouldBeFound("id.equals=" + id);
        defaultPostShouldNotBeFound("id.notEquals=" + id);

        defaultPostShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPostShouldNotBeFound("id.greaterThan=" + id);

        defaultPostShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPostShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPostsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where title equals to DEFAULT_TITLE
        defaultPostShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the postList where title equals to UPDATED_TITLE
        defaultPostShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllPostsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where title not equals to DEFAULT_TITLE
        defaultPostShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the postList where title not equals to UPDATED_TITLE
        defaultPostShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllPostsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultPostShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the postList where title equals to UPDATED_TITLE
        defaultPostShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllPostsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where title is not null
        defaultPostShouldBeFound("title.specified=true");

        // Get all the postList where title is null
        defaultPostShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllPostsByTitleContainsSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where title contains DEFAULT_TITLE
        defaultPostShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the postList where title contains UPDATED_TITLE
        defaultPostShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllPostsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where title does not contain DEFAULT_TITLE
        defaultPostShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the postList where title does not contain UPDATED_TITLE
        defaultPostShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllPostsByContentIsEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where content equals to DEFAULT_CONTENT
        defaultPostShouldBeFound("content.equals=" + DEFAULT_CONTENT);

        // Get all the postList where content equals to UPDATED_CONTENT
        defaultPostShouldNotBeFound("content.equals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void getAllPostsByContentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where content not equals to DEFAULT_CONTENT
        defaultPostShouldNotBeFound("content.notEquals=" + DEFAULT_CONTENT);

        // Get all the postList where content not equals to UPDATED_CONTENT
        defaultPostShouldBeFound("content.notEquals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void getAllPostsByContentIsInShouldWork() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where content in DEFAULT_CONTENT or UPDATED_CONTENT
        defaultPostShouldBeFound("content.in=" + DEFAULT_CONTENT + "," + UPDATED_CONTENT);

        // Get all the postList where content equals to UPDATED_CONTENT
        defaultPostShouldNotBeFound("content.in=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void getAllPostsByContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where content is not null
        defaultPostShouldBeFound("content.specified=true");

        // Get all the postList where content is null
        defaultPostShouldNotBeFound("content.specified=false");
    }
                @Test
    @Transactional
    public void getAllPostsByContentContainsSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where content contains DEFAULT_CONTENT
        defaultPostShouldBeFound("content.contains=" + DEFAULT_CONTENT);

        // Get all the postList where content contains UPDATED_CONTENT
        defaultPostShouldNotBeFound("content.contains=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void getAllPostsByContentNotContainsSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where content does not contain DEFAULT_CONTENT
        defaultPostShouldNotBeFound("content.doesNotContain=" + DEFAULT_CONTENT);

        // Get all the postList where content does not contain UPDATED_CONTENT
        defaultPostShouldBeFound("content.doesNotContain=" + UPDATED_CONTENT);
    }


    @Test
    @Transactional
    public void getAllPostsByDatePubIsEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where datePub equals to DEFAULT_DATE_PUB
        defaultPostShouldBeFound("datePub.equals=" + DEFAULT_DATE_PUB);

        // Get all the postList where datePub equals to UPDATED_DATE_PUB
        defaultPostShouldNotBeFound("datePub.equals=" + UPDATED_DATE_PUB);
    }

    @Test
    @Transactional
    public void getAllPostsByDatePubIsNotEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where datePub not equals to DEFAULT_DATE_PUB
        defaultPostShouldNotBeFound("datePub.notEquals=" + DEFAULT_DATE_PUB);

        // Get all the postList where datePub not equals to UPDATED_DATE_PUB
        defaultPostShouldBeFound("datePub.notEquals=" + UPDATED_DATE_PUB);
    }

    @Test
    @Transactional
    public void getAllPostsByDatePubIsInShouldWork() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where datePub in DEFAULT_DATE_PUB or UPDATED_DATE_PUB
        defaultPostShouldBeFound("datePub.in=" + DEFAULT_DATE_PUB + "," + UPDATED_DATE_PUB);

        // Get all the postList where datePub equals to UPDATED_DATE_PUB
        defaultPostShouldNotBeFound("datePub.in=" + UPDATED_DATE_PUB);
    }

    @Test
    @Transactional
    public void getAllPostsByDatePubIsNullOrNotNull() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where datePub is not null
        defaultPostShouldBeFound("datePub.specified=true");

        // Get all the postList where datePub is null
        defaultPostShouldNotBeFound("datePub.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostsByDatePubIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where datePub is greater than or equal to DEFAULT_DATE_PUB
        defaultPostShouldBeFound("datePub.greaterThanOrEqual=" + DEFAULT_DATE_PUB);

        // Get all the postList where datePub is greater than or equal to UPDATED_DATE_PUB
        defaultPostShouldNotBeFound("datePub.greaterThanOrEqual=" + UPDATED_DATE_PUB);
    }

    @Test
    @Transactional
    public void getAllPostsByDatePubIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where datePub is less than or equal to DEFAULT_DATE_PUB
        defaultPostShouldBeFound("datePub.lessThanOrEqual=" + DEFAULT_DATE_PUB);

        // Get all the postList where datePub is less than or equal to SMALLER_DATE_PUB
        defaultPostShouldNotBeFound("datePub.lessThanOrEqual=" + SMALLER_DATE_PUB);
    }

    @Test
    @Transactional
    public void getAllPostsByDatePubIsLessThanSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where datePub is less than DEFAULT_DATE_PUB
        defaultPostShouldNotBeFound("datePub.lessThan=" + DEFAULT_DATE_PUB);

        // Get all the postList where datePub is less than UPDATED_DATE_PUB
        defaultPostShouldBeFound("datePub.lessThan=" + UPDATED_DATE_PUB);
    }

    @Test
    @Transactional
    public void getAllPostsByDatePubIsGreaterThanSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where datePub is greater than DEFAULT_DATE_PUB
        defaultPostShouldNotBeFound("datePub.greaterThan=" + DEFAULT_DATE_PUB);

        // Get all the postList where datePub is greater than SMALLER_DATE_PUB
        defaultPostShouldBeFound("datePub.greaterThan=" + SMALLER_DATE_PUB);
    }


    @Test
    @Transactional
    public void getAllPostsByTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where time equals to DEFAULT_TIME
        defaultPostShouldBeFound("time.equals=" + DEFAULT_TIME);

        // Get all the postList where time equals to UPDATED_TIME
        defaultPostShouldNotBeFound("time.equals=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllPostsByTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where time not equals to DEFAULT_TIME
        defaultPostShouldNotBeFound("time.notEquals=" + DEFAULT_TIME);

        // Get all the postList where time not equals to UPDATED_TIME
        defaultPostShouldBeFound("time.notEquals=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllPostsByTimeIsInShouldWork() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where time in DEFAULT_TIME or UPDATED_TIME
        defaultPostShouldBeFound("time.in=" + DEFAULT_TIME + "," + UPDATED_TIME);

        // Get all the postList where time equals to UPDATED_TIME
        defaultPostShouldNotBeFound("time.in=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllPostsByTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where time is not null
        defaultPostShouldBeFound("time.specified=true");

        // Get all the postList where time is null
        defaultPostShouldNotBeFound("time.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostsByIsNameVisibaleIsEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where isNameVisibale equals to DEFAULT_IS_NAME_VISIBALE
        defaultPostShouldBeFound("isNameVisibale.equals=" + DEFAULT_IS_NAME_VISIBALE);

        // Get all the postList where isNameVisibale equals to UPDATED_IS_NAME_VISIBALE
        defaultPostShouldNotBeFound("isNameVisibale.equals=" + UPDATED_IS_NAME_VISIBALE);
    }

    @Test
    @Transactional
    public void getAllPostsByIsNameVisibaleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where isNameVisibale not equals to DEFAULT_IS_NAME_VISIBALE
        defaultPostShouldNotBeFound("isNameVisibale.notEquals=" + DEFAULT_IS_NAME_VISIBALE);

        // Get all the postList where isNameVisibale not equals to UPDATED_IS_NAME_VISIBALE
        defaultPostShouldBeFound("isNameVisibale.notEquals=" + UPDATED_IS_NAME_VISIBALE);
    }

    @Test
    @Transactional
    public void getAllPostsByIsNameVisibaleIsInShouldWork() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where isNameVisibale in DEFAULT_IS_NAME_VISIBALE or UPDATED_IS_NAME_VISIBALE
        defaultPostShouldBeFound("isNameVisibale.in=" + DEFAULT_IS_NAME_VISIBALE + "," + UPDATED_IS_NAME_VISIBALE);

        // Get all the postList where isNameVisibale equals to UPDATED_IS_NAME_VISIBALE
        defaultPostShouldNotBeFound("isNameVisibale.in=" + UPDATED_IS_NAME_VISIBALE);
    }

    @Test
    @Transactional
    public void getAllPostsByIsNameVisibaleIsNullOrNotNull() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where isNameVisibale is not null
        defaultPostShouldBeFound("isNameVisibale.specified=true");

        // Get all the postList where isNameVisibale is null
        defaultPostShouldNotBeFound("isNameVisibale.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostsByIsPhotoVisibaleIsEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where isPhotoVisibale equals to DEFAULT_IS_PHOTO_VISIBALE
        defaultPostShouldBeFound("isPhotoVisibale.equals=" + DEFAULT_IS_PHOTO_VISIBALE);

        // Get all the postList where isPhotoVisibale equals to UPDATED_IS_PHOTO_VISIBALE
        defaultPostShouldNotBeFound("isPhotoVisibale.equals=" + UPDATED_IS_PHOTO_VISIBALE);
    }

    @Test
    @Transactional
    public void getAllPostsByIsPhotoVisibaleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where isPhotoVisibale not equals to DEFAULT_IS_PHOTO_VISIBALE
        defaultPostShouldNotBeFound("isPhotoVisibale.notEquals=" + DEFAULT_IS_PHOTO_VISIBALE);

        // Get all the postList where isPhotoVisibale not equals to UPDATED_IS_PHOTO_VISIBALE
        defaultPostShouldBeFound("isPhotoVisibale.notEquals=" + UPDATED_IS_PHOTO_VISIBALE);
    }

    @Test
    @Transactional
    public void getAllPostsByIsPhotoVisibaleIsInShouldWork() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where isPhotoVisibale in DEFAULT_IS_PHOTO_VISIBALE or UPDATED_IS_PHOTO_VISIBALE
        defaultPostShouldBeFound("isPhotoVisibale.in=" + DEFAULT_IS_PHOTO_VISIBALE + "," + UPDATED_IS_PHOTO_VISIBALE);

        // Get all the postList where isPhotoVisibale equals to UPDATED_IS_PHOTO_VISIBALE
        defaultPostShouldNotBeFound("isPhotoVisibale.in=" + UPDATED_IS_PHOTO_VISIBALE);
    }

    @Test
    @Transactional
    public void getAllPostsByIsPhotoVisibaleIsNullOrNotNull() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where isPhotoVisibale is not null
        defaultPostShouldBeFound("isPhotoVisibale.specified=true");

        // Get all the postList where isPhotoVisibale is null
        defaultPostShouldNotBeFound("isPhotoVisibale.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostsByNbreLikeIsEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where nbreLike equals to DEFAULT_NBRE_LIKE
        defaultPostShouldBeFound("nbreLike.equals=" + DEFAULT_NBRE_LIKE);

        // Get all the postList where nbreLike equals to UPDATED_NBRE_LIKE
        defaultPostShouldNotBeFound("nbreLike.equals=" + UPDATED_NBRE_LIKE);
    }

    @Test
    @Transactional
    public void getAllPostsByNbreLikeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where nbreLike not equals to DEFAULT_NBRE_LIKE
        defaultPostShouldNotBeFound("nbreLike.notEquals=" + DEFAULT_NBRE_LIKE);

        // Get all the postList where nbreLike not equals to UPDATED_NBRE_LIKE
        defaultPostShouldBeFound("nbreLike.notEquals=" + UPDATED_NBRE_LIKE);
    }

    @Test
    @Transactional
    public void getAllPostsByNbreLikeIsInShouldWork() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where nbreLike in DEFAULT_NBRE_LIKE or UPDATED_NBRE_LIKE
        defaultPostShouldBeFound("nbreLike.in=" + DEFAULT_NBRE_LIKE + "," + UPDATED_NBRE_LIKE);

        // Get all the postList where nbreLike equals to UPDATED_NBRE_LIKE
        defaultPostShouldNotBeFound("nbreLike.in=" + UPDATED_NBRE_LIKE);
    }

    @Test
    @Transactional
    public void getAllPostsByNbreLikeIsNullOrNotNull() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where nbreLike is not null
        defaultPostShouldBeFound("nbreLike.specified=true");

        // Get all the postList where nbreLike is null
        defaultPostShouldNotBeFound("nbreLike.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostsByNbreLikeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where nbreLike is greater than or equal to DEFAULT_NBRE_LIKE
        defaultPostShouldBeFound("nbreLike.greaterThanOrEqual=" + DEFAULT_NBRE_LIKE);

        // Get all the postList where nbreLike is greater than or equal to UPDATED_NBRE_LIKE
        defaultPostShouldNotBeFound("nbreLike.greaterThanOrEqual=" + UPDATED_NBRE_LIKE);
    }

    @Test
    @Transactional
    public void getAllPostsByNbreLikeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where nbreLike is less than or equal to DEFAULT_NBRE_LIKE
        defaultPostShouldBeFound("nbreLike.lessThanOrEqual=" + DEFAULT_NBRE_LIKE);

        // Get all the postList where nbreLike is less than or equal to SMALLER_NBRE_LIKE
        defaultPostShouldNotBeFound("nbreLike.lessThanOrEqual=" + SMALLER_NBRE_LIKE);
    }

    @Test
    @Transactional
    public void getAllPostsByNbreLikeIsLessThanSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where nbreLike is less than DEFAULT_NBRE_LIKE
        defaultPostShouldNotBeFound("nbreLike.lessThan=" + DEFAULT_NBRE_LIKE);

        // Get all the postList where nbreLike is less than UPDATED_NBRE_LIKE
        defaultPostShouldBeFound("nbreLike.lessThan=" + UPDATED_NBRE_LIKE);
    }

    @Test
    @Transactional
    public void getAllPostsByNbreLikeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where nbreLike is greater than DEFAULT_NBRE_LIKE
        defaultPostShouldNotBeFound("nbreLike.greaterThan=" + DEFAULT_NBRE_LIKE);

        // Get all the postList where nbreLike is greater than SMALLER_NBRE_LIKE
        defaultPostShouldBeFound("nbreLike.greaterThan=" + SMALLER_NBRE_LIKE);
    }


    @Test
    @Transactional
    public void getAllPostsByNbreCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where nbreComments equals to DEFAULT_NBRE_COMMENTS
        defaultPostShouldBeFound("nbreComments.equals=" + DEFAULT_NBRE_COMMENTS);

        // Get all the postList where nbreComments equals to UPDATED_NBRE_COMMENTS
        defaultPostShouldNotBeFound("nbreComments.equals=" + UPDATED_NBRE_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllPostsByNbreCommentsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where nbreComments not equals to DEFAULT_NBRE_COMMENTS
        defaultPostShouldNotBeFound("nbreComments.notEquals=" + DEFAULT_NBRE_COMMENTS);

        // Get all the postList where nbreComments not equals to UPDATED_NBRE_COMMENTS
        defaultPostShouldBeFound("nbreComments.notEquals=" + UPDATED_NBRE_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllPostsByNbreCommentsIsInShouldWork() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where nbreComments in DEFAULT_NBRE_COMMENTS or UPDATED_NBRE_COMMENTS
        defaultPostShouldBeFound("nbreComments.in=" + DEFAULT_NBRE_COMMENTS + "," + UPDATED_NBRE_COMMENTS);

        // Get all the postList where nbreComments equals to UPDATED_NBRE_COMMENTS
        defaultPostShouldNotBeFound("nbreComments.in=" + UPDATED_NBRE_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllPostsByNbreCommentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where nbreComments is not null
        defaultPostShouldBeFound("nbreComments.specified=true");

        // Get all the postList where nbreComments is null
        defaultPostShouldNotBeFound("nbreComments.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostsByNbreCommentsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where nbreComments is greater than or equal to DEFAULT_NBRE_COMMENTS
        defaultPostShouldBeFound("nbreComments.greaterThanOrEqual=" + DEFAULT_NBRE_COMMENTS);

        // Get all the postList where nbreComments is greater than or equal to UPDATED_NBRE_COMMENTS
        defaultPostShouldNotBeFound("nbreComments.greaterThanOrEqual=" + UPDATED_NBRE_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllPostsByNbreCommentsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where nbreComments is less than or equal to DEFAULT_NBRE_COMMENTS
        defaultPostShouldBeFound("nbreComments.lessThanOrEqual=" + DEFAULT_NBRE_COMMENTS);

        // Get all the postList where nbreComments is less than or equal to SMALLER_NBRE_COMMENTS
        defaultPostShouldNotBeFound("nbreComments.lessThanOrEqual=" + SMALLER_NBRE_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllPostsByNbreCommentsIsLessThanSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where nbreComments is less than DEFAULT_NBRE_COMMENTS
        defaultPostShouldNotBeFound("nbreComments.lessThan=" + DEFAULT_NBRE_COMMENTS);

        // Get all the postList where nbreComments is less than UPDATED_NBRE_COMMENTS
        defaultPostShouldBeFound("nbreComments.lessThan=" + UPDATED_NBRE_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllPostsByNbreCommentsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where nbreComments is greater than DEFAULT_NBRE_COMMENTS
        defaultPostShouldNotBeFound("nbreComments.greaterThan=" + DEFAULT_NBRE_COMMENTS);

        // Get all the postList where nbreComments is greater than SMALLER_NBRE_COMMENTS
        defaultPostShouldBeFound("nbreComments.greaterThan=" + SMALLER_NBRE_COMMENTS);
    }


    @Test
    @Transactional
    public void getAllPostsByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);
        Comments comments = CommentsResourceIT.createEntity(em);
        em.persist(comments);
        em.flush();
        post.addComments(comments);
        postRepository.saveAndFlush(post);
        Long commentsId = comments.getId();

        // Get all the postList where comments equals to commentsId
        defaultPostShouldBeFound("commentsId.equals=" + commentsId);

        // Get all the postList where comments equals to commentsId + 1
        defaultPostShouldNotBeFound("commentsId.equals=" + (commentsId + 1));
    }


    @Test
    @Transactional
    public void getAllPostsByFilesPostIsEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);
        FilesPost filesPost = FilesPostResourceIT.createEntity(em);
        em.persist(filesPost);
        em.flush();
        post.addFilesPost(filesPost);
        postRepository.saveAndFlush(post);
        Long filesPostId = filesPost.getId();

        // Get all the postList where filesPost equals to filesPostId
        defaultPostShouldBeFound("filesPostId.equals=" + filesPostId);

        // Get all the postList where filesPost equals to filesPostId + 1
        defaultPostShouldNotBeFound("filesPostId.equals=" + (filesPostId + 1));
    }


    @Test
    @Transactional
    public void getAllPostsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);
        ApplicationUser user = ApplicationUserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        post.addUser(user);
        postRepository.saveAndFlush(post);
        Long userId = user.getId();

        // Get all the postList where user equals to userId
        defaultPostShouldBeFound("userId.equals=" + userId);

        // Get all the postList where user equals to userId + 1
        defaultPostShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPostShouldBeFound(String filter) throws Exception {
        restPostMockMvc.perform(get("/api/posts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(post.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].datePub").value(hasItem(DEFAULT_DATE_PUB.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].isNameVisibale").value(hasItem(DEFAULT_IS_NAME_VISIBALE.booleanValue())))
            .andExpect(jsonPath("$.[*].isPhotoVisibale").value(hasItem(DEFAULT_IS_PHOTO_VISIBALE.booleanValue())))
            .andExpect(jsonPath("$.[*].nbreLike").value(hasItem(DEFAULT_NBRE_LIKE)))
            .andExpect(jsonPath("$.[*].nbreComments").value(hasItem(DEFAULT_NBRE_COMMENTS)));

        // Check, that the count call also returns 1
        restPostMockMvc.perform(get("/api/posts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPostShouldNotBeFound(String filter) throws Exception {
        restPostMockMvc.perform(get("/api/posts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPostMockMvc.perform(get("/api/posts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPost() throws Exception {
        // Get the post
        restPostMockMvc.perform(get("/api/posts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePost() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        int databaseSizeBeforeUpdate = postRepository.findAll().size();

        // Update the post
        Post updatedPost = postRepository.findById(post.getId()).get();
        // Disconnect from session so that the updates on updatedPost are not directly saved in db
        em.detach(updatedPost);
        updatedPost
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .datePub(UPDATED_DATE_PUB)
            .time(UPDATED_TIME)
            .isNameVisibale(UPDATED_IS_NAME_VISIBALE)
            .isPhotoVisibale(UPDATED_IS_PHOTO_VISIBALE)
            .nbreLike(UPDATED_NBRE_LIKE)
            .nbreComments(UPDATED_NBRE_COMMENTS);
        PostDTO postDTO = postMapper.toDto(updatedPost);

        restPostMockMvc.perform(put("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postDTO)))
            .andExpect(status().isOk());

        // Validate the Post in the database
        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeUpdate);
        Post testPost = postList.get(postList.size() - 1);
        assertThat(testPost.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPost.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testPost.getDatePub()).isEqualTo(UPDATED_DATE_PUB);
        assertThat(testPost.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testPost.isIsNameVisibale()).isEqualTo(UPDATED_IS_NAME_VISIBALE);
        assertThat(testPost.isIsPhotoVisibale()).isEqualTo(UPDATED_IS_PHOTO_VISIBALE);
        assertThat(testPost.getNbreLike()).isEqualTo(UPDATED_NBRE_LIKE);
        assertThat(testPost.getNbreComments()).isEqualTo(UPDATED_NBRE_COMMENTS);

        // Validate the Post in Elasticsearch
        verify(mockPostSearchRepository, times(1)).save(testPost);
    }

    @Test
    @Transactional
    public void updateNonExistingPost() throws Exception {
        int databaseSizeBeforeUpdate = postRepository.findAll().size();

        // Create the Post
        PostDTO postDTO = postMapper.toDto(post);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostMockMvc.perform(put("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Post in the database
        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Post in Elasticsearch
        verify(mockPostSearchRepository, times(0)).save(post);
    }

    @Test
    @Transactional
    public void deletePost() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        int databaseSizeBeforeDelete = postRepository.findAll().size();

        // Delete the post
        restPostMockMvc.perform(delete("/api/posts/{id}", post.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Post in Elasticsearch
        verify(mockPostSearchRepository, times(1)).deleteById(post.getId());
    }

    @Test
    @Transactional
    public void searchPost() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        postRepository.saveAndFlush(post);
        when(mockPostSearchRepository.search(queryStringQuery("id:" + post.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(post), PageRequest.of(0, 1), 1));

        // Search the post
        restPostMockMvc.perform(get("/api/_search/posts?query=id:" + post.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(post.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].datePub").value(hasItem(DEFAULT_DATE_PUB.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].isNameVisibale").value(hasItem(DEFAULT_IS_NAME_VISIBALE.booleanValue())))
            .andExpect(jsonPath("$.[*].isPhotoVisibale").value(hasItem(DEFAULT_IS_PHOTO_VISIBALE.booleanValue())))
            .andExpect(jsonPath("$.[*].nbreLike").value(hasItem(DEFAULT_NBRE_LIKE)))
            .andExpect(jsonPath("$.[*].nbreComments").value(hasItem(DEFAULT_NBRE_COMMENTS)));
    }
}
