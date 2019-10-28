package com.tk.api.gateway.web.rest;

import com.tk.api.gateway.domain.Suggestions;
import com.tk.api.gateway.repository.SuggestionsRepository;
import com.tk.api.gateway.security.AuthoritiesConstants;
import com.tk.api.gateway.service.dto.CategoriesCustomDTO;
import com.tk.api.gateway.service.dto.SuggestionsDTO;
import com.tk.api.gateway.service.posts.SuggestionService;
import com.tk.api.gateway.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.tk.api.gateway.domain.Suggestions}.
 */
@RestController
@RequestMapping("/api")
public class SuggestionsResource {

    private final Logger log = LoggerFactory.getLogger(SuggestionsResource.class);

    private static final String ENTITY_NAME = "suggestions";

    @Autowired
    SuggestionService suggestionService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SuggestionsRepository suggestionsRepository;

    public SuggestionsResource(SuggestionsRepository suggestionsRepository) {
        this.suggestionsRepository = suggestionsRepository;
    }

    /**
     * {@code POST  /suggestions} : Create a new suggestions.
     *
     * @param suggestions the suggestions to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new suggestions, or with status {@code 400 (Bad Request)} if the suggestions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/suggestions")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Suggestions> createSuggestions(@RequestBody Suggestions suggestions) throws URISyntaxException {
        log.debug("REST request to save Suggestions : {}", suggestions);
        if (suggestions.getId() != null) {
            throw new BadRequestAlertException("A new suggestions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Suggestions result = suggestionsRepository.save(suggestions);
        return ResponseEntity.created(new URI("/api/suggestions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /suggestions} : Updates an existing suggestions.
     *
     * @param suggestions the suggestions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated suggestions,
     * or with status {@code 400 (Bad Request)} if the suggestions is not valid,
     * or with status {@code 500 (Internal Server Error)} if the suggestions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/suggestions")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Suggestions> updateSuggestions(@RequestBody Suggestions suggestions) throws URISyntaxException {
        log.debug("REST request to update Suggestions : {}", suggestions);
        if (suggestions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Suggestions result = suggestionsRepository.save(suggestions);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, suggestions.getId().toString()))
            .body(result);
    }


    @PostMapping("/suggestions/new")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Optional<Suggestions>> updateAddSuggestions(@RequestBody SuggestionsDTO suggestionss) throws URISyntaxException {
        log.debug("REST request to update Suggestions : {}", suggestionss);
        Optional<Suggestions> suggestions = Optional.of(new Suggestions());
        try {
            suggestions = suggestionService.generateDataPost(suggestionss);
        } catch (Exception e) {
            throw new BadRequestAlertException("Error connet api", ENTITY_NAME, "ERROR");
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, suggestionss.toString()))
            .body(suggestions);
    }

    /**
     * {@code GET  /suggestions} : get all the suggestions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of suggestions in body.
     */
    @GetMapping("/suggestions")
    @Secured({"ROLE_USER", "ROLE_ANONYMOUS"})
    public List<Suggestions> getAllSuggestions() {
        log.debug("REST request to get all Suggestions");
        return suggestionsRepository.findAll();
    }
    @GetMapping("/suggestions-custom")
    @Secured({"ROLE_USER", "ROLE_ANONYMOUS"})
    public List<CategoriesCustomDTO> getAllSuggestionsCustom() {
        log.debug("REST request to get all Suggestions");
        List<Suggestions> suggestions = suggestionsRepository.findAll();
        List<CategoriesCustomDTO> categoriesCustomDTOS = new ArrayList<>();
        for (Suggestions suggestion: suggestions) {
            CategoriesCustomDTO categoriesCustom = new CategoriesCustomDTO();
            categoriesCustom.setId(suggestion.getId());
            categoriesCustom.setText(suggestion.getName());
            categoriesCustomDTOS.add(categoriesCustom);
        }
        return categoriesCustomDTOS;
    }

    /**
     * {@code GET  /suggestions/:id} : get the "id" suggestions.
     *
     * @param id the id of the suggestions to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the suggestions, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/suggestions/{id}")
    @Secured({"ROLE_USER", "ROLE_ANONYMOUS"})
    public ResponseEntity<Suggestions> getSuggestions(@PathVariable Long id) {
        log.debug("REST request to get Suggestions : {}", id);
        Optional<Suggestions> suggestions = suggestionsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(suggestions);
    }

    /**
     * {@code DELETE  /suggestions/:id} : delete the "id" suggestions.
     *
     * @param id the id of the suggestions to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/suggestions/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteSuggestions(@PathVariable Long id) {
        log.debug("REST request to delete Suggestions : {}", id);
        suggestionsRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/suggestions/get-suggestions-custom/{page}/{limit}")
    public ResponseEntity<List<Suggestions>> getSuggestionsCustom(@PathVariable int page, @PathVariable int limit) throws URISyntaxException {
        log.debug("REST request to custom get Posts");
        List<Suggestions> suggestions = new ArrayList<>();
        suggestions = suggestionsRepository.findCustomSuggestions(new PageRequest(page, limit));
        return ResponseEntity.created(new URI("/api/suggestions/get-suggestions-custom/" + page + "/" + limit))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, suggestions.toString()))
            .body(suggestions);
    }

    /**
     *
     * @param slug
     * @return
     */
    @GetMapping("/suggestions/get-suggestion-by-slug/{slug}")
    @Secured({"ROLE_USER", "ROLE_ANONYMOUS"})
    public ResponseEntity<Suggestions> getSuggestionBySlug(@PathVariable String slug) {
        log.debug("REST request to get Posts : {}", slug);
        Optional<Suggestions> suggestions = suggestionsRepository.findFirstBySlug(slug);
        return ResponseUtil.wrapOrNotFound(suggestions);
    }
}
