package com.tk.api.gateway.web.rest;

import com.tk.api.gateway.ApiGatewayApp;
import com.tk.api.gateway.domain.Suggestions;
import com.tk.api.gateway.repository.SuggestionsRepository;
import com.tk.api.gateway.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.tk.api.gateway.web.rest.TestUtil.sameInstant;
import static com.tk.api.gateway.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link SuggestionsResource} REST controller.
 */
@SpringBootTest(classes = ApiGatewayApp.class)
public class SuggestionsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_TYPE = 1L;
    private static final Long UPDATED_TYPE = 2L;

    private static final Long DEFAULT_SORT = 1L;
    private static final Long UPDATED_SORT = 2L;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private SuggestionsRepository suggestionsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restSuggestionsMockMvc;

    private Suggestions suggestions;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SuggestionsResource suggestionsResource = new SuggestionsResource(suggestionsRepository);
        this.restSuggestionsMockMvc = MockMvcBuilders.standaloneSetup(suggestionsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Suggestions createEntity(EntityManager em) {
        Suggestions suggestions = new Suggestions()
            .name(DEFAULT_NAME)
            .slug(DEFAULT_SLUG)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .sort(DEFAULT_SORT)
            .status(DEFAULT_STATUS)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdate(DEFAULT_LAST_UPDATE);
        return suggestions;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Suggestions createUpdatedEntity(EntityManager em) {
        Suggestions suggestions = new Suggestions()
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .sort(UPDATED_SORT)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdate(UPDATED_LAST_UPDATE);
        return suggestions;
    }

    @BeforeEach
    public void initTest() {
        suggestions = createEntity(em);
    }

    @Test
    @Transactional
    public void createSuggestions() throws Exception {
        int databaseSizeBeforeCreate = suggestionsRepository.findAll().size();

        // Create the Suggestions
        restSuggestionsMockMvc.perform(post("/api/suggestions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestions)))
            .andExpect(status().isCreated());

        // Validate the Suggestions in the database
        List<Suggestions> suggestionsList = suggestionsRepository.findAll();
        assertThat(suggestionsList).hasSize(databaseSizeBeforeCreate + 1);
        Suggestions testSuggestions = suggestionsList.get(suggestionsList.size() - 1);
        assertThat(testSuggestions.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSuggestions.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(testSuggestions.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSuggestions.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSuggestions.getSort()).isEqualTo(DEFAULT_SORT);
        assertThat(testSuggestions.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSuggestions.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSuggestions.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
    }

    @Test
    @Transactional
    public void createSuggestionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = suggestionsRepository.findAll().size();

        // Create the Suggestions with an existing ID
        suggestions.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSuggestionsMockMvc.perform(post("/api/suggestions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestions)))
            .andExpect(status().isBadRequest());

        // Validate the Suggestions in the database
        List<Suggestions> suggestionsList = suggestionsRepository.findAll();
        assertThat(suggestionsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSuggestions() throws Exception {
        // Initialize the database
        suggestionsRepository.saveAndFlush(suggestions);

        // Get all the suggestionsList
        restSuggestionsMockMvc.perform(get("/api/suggestions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(suggestions.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.intValue())))
            .andExpect(jsonPath("$.[*].sort").value(hasItem(DEFAULT_SORT.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATE))));
    }
    
    @Test
    @Transactional
    public void getSuggestions() throws Exception {
        // Initialize the database
        suggestionsRepository.saveAndFlush(suggestions);

        // Get the suggestions
        restSuggestionsMockMvc.perform(get("/api/suggestions/{id}", suggestions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(suggestions.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.intValue()))
            .andExpect(jsonPath("$.sort").value(DEFAULT_SORT.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdate").value(sameInstant(DEFAULT_LAST_UPDATE)));
    }

    @Test
    @Transactional
    public void getNonExistingSuggestions() throws Exception {
        // Get the suggestions
        restSuggestionsMockMvc.perform(get("/api/suggestions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSuggestions() throws Exception {
        // Initialize the database
        suggestionsRepository.saveAndFlush(suggestions);

        int databaseSizeBeforeUpdate = suggestionsRepository.findAll().size();

        // Update the suggestions
        Suggestions updatedSuggestions = suggestionsRepository.findById(suggestions.getId()).get();
        // Disconnect from session so that the updates on updatedSuggestions are not directly saved in db
        em.detach(updatedSuggestions);
        updatedSuggestions
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .sort(UPDATED_SORT)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdate(UPDATED_LAST_UPDATE);

        restSuggestionsMockMvc.perform(put("/api/suggestions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSuggestions)))
            .andExpect(status().isOk());

        // Validate the Suggestions in the database
        List<Suggestions> suggestionsList = suggestionsRepository.findAll();
        assertThat(suggestionsList).hasSize(databaseSizeBeforeUpdate);
        Suggestions testSuggestions = suggestionsList.get(suggestionsList.size() - 1);
        assertThat(testSuggestions.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSuggestions.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testSuggestions.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSuggestions.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSuggestions.getSort()).isEqualTo(UPDATED_SORT);
        assertThat(testSuggestions.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSuggestions.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSuggestions.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
    }

    @Test
    @Transactional
    public void updateNonExistingSuggestions() throws Exception {
        int databaseSizeBeforeUpdate = suggestionsRepository.findAll().size();

        // Create the Suggestions

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuggestionsMockMvc.perform(put("/api/suggestions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestions)))
            .andExpect(status().isBadRequest());

        // Validate the Suggestions in the database
        List<Suggestions> suggestionsList = suggestionsRepository.findAll();
        assertThat(suggestionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSuggestions() throws Exception {
        // Initialize the database
        suggestionsRepository.saveAndFlush(suggestions);

        int databaseSizeBeforeDelete = suggestionsRepository.findAll().size();

        // Delete the suggestions
        restSuggestionsMockMvc.perform(delete("/api/suggestions/{id}", suggestions.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Suggestions> suggestionsList = suggestionsRepository.findAll();
        assertThat(suggestionsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Suggestions.class);
        Suggestions suggestions1 = new Suggestions();
        suggestions1.setId(1L);
        Suggestions suggestions2 = new Suggestions();
        suggestions2.setId(suggestions1.getId());
        assertThat(suggestions1).isEqualTo(suggestions2);
        suggestions2.setId(2L);
        assertThat(suggestions1).isNotEqualTo(suggestions2);
        suggestions1.setId(null);
        assertThat(suggestions1).isNotEqualTo(suggestions2);
    }
}
