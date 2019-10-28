package com.tk.api.gateway.web.rest;

import com.tk.api.gateway.ApiGatewayApp;
import com.tk.api.gateway.domain.Actions;
import com.tk.api.gateway.repository.ActionsRepository;
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

import com.tk.api.gateway.domain.enumeration.Action;
/**
 * Integration tests for the {@Link ActionsResource} REST controller.
 */
@SpringBootTest(classes = ApiGatewayApp.class)
public class ActionsResourceIT {

    private static final Action DEFAULT_ACTION = Action.LIKE;
    private static final Action UPDATED_ACTION = Action.DISLIKE;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ActionsRepository actionsRepository;

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

    private MockMvc restActionsMockMvc;

    private Actions actions;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ActionsResource actionsResource = new ActionsResource(actionsRepository);
        this.restActionsMockMvc = MockMvcBuilders.standaloneSetup(actionsResource)
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
    public static Actions createEntity(EntityManager em) {
        Actions actions = new Actions()
            .action(DEFAULT_ACTION)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdate(DEFAULT_LAST_UPDATE);
        return actions;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Actions createUpdatedEntity(EntityManager em) {
        Actions actions = new Actions()
            .action(UPDATED_ACTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdate(UPDATED_LAST_UPDATE);
        return actions;
    }

    @BeforeEach
    public void initTest() {
        actions = createEntity(em);
    }

    @Test
    @Transactional
    public void createActions() throws Exception {
        int databaseSizeBeforeCreate = actionsRepository.findAll().size();

        // Create the Actions
        restActionsMockMvc.perform(post("/api/actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actions)))
            .andExpect(status().isCreated());

        // Validate the Actions in the database
        List<Actions> actionsList = actionsRepository.findAll();
        assertThat(actionsList).hasSize(databaseSizeBeforeCreate + 1);
        Actions testActions = actionsList.get(actionsList.size() - 1);
        assertThat(testActions.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testActions.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testActions.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
    }

    @Test
    @Transactional
    public void createActionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = actionsRepository.findAll().size();

        // Create the Actions with an existing ID
        actions.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActionsMockMvc.perform(post("/api/actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actions)))
            .andExpect(status().isBadRequest());

        // Validate the Actions in the database
        List<Actions> actionsList = actionsRepository.findAll();
        assertThat(actionsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllActions() throws Exception {
        // Initialize the database
        actionsRepository.saveAndFlush(actions);

        // Get all the actionsList
        restActionsMockMvc.perform(get("/api/actions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(actions.getId().intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(sameInstant(DEFAULT_LAST_UPDATE))));
    }
    
    @Test
    @Transactional
    public void getActions() throws Exception {
        // Initialize the database
        actionsRepository.saveAndFlush(actions);

        // Get the actions
        restActionsMockMvc.perform(get("/api/actions/{id}", actions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(actions.getId().intValue()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.lastUpdate").value(sameInstant(DEFAULT_LAST_UPDATE)));
    }

    @Test
    @Transactional
    public void getNonExistingActions() throws Exception {
        // Get the actions
        restActionsMockMvc.perform(get("/api/actions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActions() throws Exception {
        // Initialize the database
        actionsRepository.saveAndFlush(actions);

        int databaseSizeBeforeUpdate = actionsRepository.findAll().size();

        // Update the actions
        Actions updatedActions = actionsRepository.findById(actions.getId()).get();
        // Disconnect from session so that the updates on updatedActions are not directly saved in db
        em.detach(updatedActions);
        updatedActions
            .action(UPDATED_ACTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdate(UPDATED_LAST_UPDATE);

        restActionsMockMvc.perform(put("/api/actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedActions)))
            .andExpect(status().isOk());

        // Validate the Actions in the database
        List<Actions> actionsList = actionsRepository.findAll();
        assertThat(actionsList).hasSize(databaseSizeBeforeUpdate);
        Actions testActions = actionsList.get(actionsList.size() - 1);
        assertThat(testActions.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testActions.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testActions.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
    }

    @Test
    @Transactional
    public void updateNonExistingActions() throws Exception {
        int databaseSizeBeforeUpdate = actionsRepository.findAll().size();

        // Create the Actions

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActionsMockMvc.perform(put("/api/actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(actions)))
            .andExpect(status().isBadRequest());

        // Validate the Actions in the database
        List<Actions> actionsList = actionsRepository.findAll();
        assertThat(actionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteActions() throws Exception {
        // Initialize the database
        actionsRepository.saveAndFlush(actions);

        int databaseSizeBeforeDelete = actionsRepository.findAll().size();

        // Delete the actions
        restActionsMockMvc.perform(delete("/api/actions/{id}", actions.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Actions> actionsList = actionsRepository.findAll();
        assertThat(actionsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Actions.class);
        Actions actions1 = new Actions();
        actions1.setId(1L);
        Actions actions2 = new Actions();
        actions2.setId(actions1.getId());
        assertThat(actions1).isEqualTo(actions2);
        actions2.setId(2L);
        assertThat(actions1).isNotEqualTo(actions2);
        actions1.setId(null);
        assertThat(actions1).isNotEqualTo(actions2);
    }
}
