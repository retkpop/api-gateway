package com.tk.api.gateway.web.rest;

import com.tk.api.gateway.domain.Categories;
import com.tk.api.gateway.domain.Posts;
import com.tk.api.gateway.repository.CategoriesRepository;
import com.tk.api.gateway.security.AuthoritiesConstants;
import com.tk.api.gateway.service.dto.CategoriesCustomDTO;
import com.tk.api.gateway.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
 * REST controller for managing {@link com.tk.api.gateway.domain.Categories}.
 */
@RestController
@RequestMapping("/api")
public class CategoriesResource {

    private final Logger log = LoggerFactory.getLogger(CategoriesResource.class);

    private static final String ENTITY_NAME = "categories";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoriesRepository categoriesRepository;

    public CategoriesResource(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    /**
     * {@code POST  /categories} : Create a new categories.
     *
     * @param categories the categories to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categories, or with status {@code 400 (Bad Request)} if the categories has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/categories")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Categories> createCategories(@RequestBody Categories categories) throws URISyntaxException {
        log.debug("REST request to save Categories : {}", categories);
        if (categories.getId() != null) {
            throw new BadRequestAlertException("A new categories cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Categories result = categoriesRepository.save(categories);
        return ResponseEntity.created(new URI("/api/categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /categories} : Updates an existing categories.
     *
     * @param categories the categories to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categories,
     * or with status {@code 400 (Bad Request)} if the categories is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categories couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/categories")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Categories> updateCategories(@RequestBody Categories categories) throws URISyntaxException {
        log.debug("REST request to update Categories : {}", categories);
        if (categories.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Categories result = categoriesRepository.save(categories);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categories.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /categories} : get all the categories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categories in body.
     */
    @GetMapping("/get-all-categories")
    @Secured({"ROLE_USER", "ROLE_ANONYMOUS"})
    public List<Categories> getAllCategories() {
        log.debug("REST request to get all Categories");
        return categoriesRepository.findAll();
    }

    /**
     * {@code GET  /categories/:id} : get the "id" categories.
     *
     * @param id the id of the categories to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categories, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/categories/{id}")
    @Secured({"ROLE_USER", "ROLE_ANONYMOUS"})
    public ResponseEntity<Categories> getCategories(@PathVariable Long id) {
        log.debug("REST request to get Categories : {}", id);
        Optional<Categories> categories = categoriesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(categories);
    }

    /**
     * {@code DELETE  /categories/:id} : delete the "id" categories.
     *
     * @param id the id of the categories to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/categories/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteCategories(@PathVariable Long id) {
        log.debug("REST request to delete Categories : {}", id);
        categoriesRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/categories/custom-get-all")
    @Secured({"ROLE_USER", "ROLE_ANONYMOUS"})
    public List<CategoriesCustomDTO> getAllCategoriesCustom() {
        log.debug("REST request to get all Categories");
        List<Categories> categories = categoriesRepository.findAll();
        List<CategoriesCustomDTO> categoriesCustomDTOS = new ArrayList<>();
        for (Categories category: categories) {
            CategoriesCustomDTO categoriesCustom = new CategoriesCustomDTO();
            categoriesCustom.setId(category.getId());
            categoriesCustom.setText(category.getName());
            categoriesCustomDTOS.add(categoriesCustom);
        }
        return categoriesCustomDTOS;
    }
    @GetMapping("/categories/cat-id/{id}/{page}/{limit}")
    @Secured({"ROLE_USER", "ROLE_ANONYMOUS"})
    public List<Posts> getPostByIdCat(@PathVariable Long id, @PathVariable int page, @PathVariable int limit) {
        Sort sort = Sort.by(
            Sort.Order.desc("id"));
        List<Posts> posts = new ArrayList<>();
        try {
            posts = categoriesRepository.getPostByIdCat(new PageRequest(page, limit, sort), id);
        } catch (Exception e) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        return posts;
    }
}
