package com.tk.api.gateway.web.rest;

import com.tk.api.gateway.domain.Hashtag;
import com.tk.api.gateway.repository.HashtagRepository;
import com.tk.api.gateway.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.tk.api.gateway.domain.Hashtag}.
 */
@RestController
@RequestMapping("/api")
public class HashtagResource {

    private final Logger log = LoggerFactory.getLogger(HashtagResource.class);

    private static final String ENTITY_NAME = "hashtag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HashtagRepository hashtagRepository;

    public HashtagResource(HashtagRepository hashtagRepository) {
        this.hashtagRepository = hashtagRepository;
    }

    /**
     * {@code POST  /hashtags} : Create a new hashtag.
     *
     * @param hashtag the hashtag to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hashtag, or with status {@code 400 (Bad Request)} if the hashtag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hashtags")
    public ResponseEntity<Hashtag> createHashtag(@RequestBody Hashtag hashtag) throws URISyntaxException {
        log.debug("REST request to save Hashtag : {}", hashtag);
        if (hashtag.getId() != null) {
            throw new BadRequestAlertException("A new hashtag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Hashtag result = hashtagRepository.save(hashtag);
        return ResponseEntity.created(new URI("/api/hashtags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hashtags} : Updates an existing hashtag.
     *
     * @param hashtag the hashtag to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hashtag,
     * or with status {@code 400 (Bad Request)} if the hashtag is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hashtag couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hashtags")
    public ResponseEntity<Hashtag> updateHashtag(@RequestBody Hashtag hashtag) throws URISyntaxException {
        log.debug("REST request to update Hashtag : {}", hashtag);
        if (hashtag.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Hashtag result = hashtagRepository.save(hashtag);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hashtag.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /hashtags} : get all the hashtags.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hashtags in body.
     */
    @GetMapping("/hashtags")
    public List<Hashtag> getAllHashtags() {
        log.debug("REST request to get all Hashtags");
        return hashtagRepository.findAll();
    }

    /**
     * {@code GET  /hashtags/:id} : get the "id" hashtag.
     *
     * @param id the id of the hashtag to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hashtag, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hashtags/{id}")
    public ResponseEntity<Hashtag> getHashtag(@PathVariable Long id) {
        log.debug("REST request to get Hashtag : {}", id);
        Optional<Hashtag> hashtag = hashtagRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(hashtag);
    }

    /**
     * {@code DELETE  /hashtags/:id} : delete the "id" hashtag.
     *
     * @param id the id of the hashtag to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hashtags/{id}")
    public ResponseEntity<Void> deleteHashtag(@PathVariable Long id) {
        log.debug("REST request to delete Hashtag : {}", id);
        hashtagRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
