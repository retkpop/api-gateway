package com.tk.api.gateway.web.rest;

import com.tk.api.gateway.domain.Actions;
import com.tk.api.gateway.domain.Posts;
import com.tk.api.gateway.domain.clientRequest.ActionReq;
import com.tk.api.gateway.domain.clientResponse.StaticsActions;
import com.tk.api.gateway.repository.ActionsRepository;
import com.tk.api.gateway.repository.PostsRepository;
import com.tk.api.gateway.security.AuthoritiesConstants;
import com.tk.api.gateway.service.posts.ActionService;
import com.tk.api.gateway.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.tk.api.gateway.domain.Actions}.
 */
@RestController
@RequestMapping("/api")
public class ActionsResource {

    @Autowired
    ActionService actionService;

    @Autowired
    PostsRepository postsRepository;

    private final Logger log = LoggerFactory.getLogger(ActionsResource.class);

    private static final String ENTITY_NAME = "actions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActionsRepository actionsRepository;

    public ActionsResource(ActionsRepository actionsRepository) {
        this.actionsRepository = actionsRepository;
    }

    /**
     * {@code POST  /actions} : Create a new actions.
     *
     * @param actionReq the actions to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new actions, or with status {@code 400 (Bad Request)} if the actions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/actions")
    @Secured("ROLE_USER")
    public ResponseEntity<Optional<StaticsActions>> createActions(@RequestBody ActionReq actionReq) throws URISyntaxException {
        log.debug("REST request to save Actions : {}", actionReq);
        if (actionReq.getPost_id() == null) {
            throw new BadRequestAlertException("Post is not NULL", ENTITY_NAME, "postIdNull");
        }
        Optional<StaticsActions> staticsActions = Optional.of(new StaticsActions());
        try {
            Optional<Actions> actions = actionService.generateDataAction(actionReq);
            if(actions.isPresent()) {
                actionsRepository.save(actions.get());
            }
            staticsActions = actionService.getActionsByUser(actionReq.getPost_id());
        } catch (Exception e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "ACTION_ERROR");
        }
        return ResponseEntity.created(new URI("/api/actions/"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, staticsActions.toString()))
            .body(staticsActions);
    }

    /**
     * {@code PUT  /actions} : Updates an existing actions.
     *
     * @param actions the actions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated actions,
     * or with status {@code 400 (Bad Request)} if the actions is not valid,
     * or with status {@code 500 (Internal Server Error)} if the actions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/actions")
    @Secured("ROLE_USER")
    public ResponseEntity<Actions> updateActions(@RequestBody Actions actions) throws URISyntaxException {
        log.debug("REST request to update Actions : {}", actions);
        if (actions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Actions result = actionsRepository.save(actions);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, actions.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /actions} : get all the actions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of actions in body.
     */
    @GetMapping("/actions")
    @Secured({"ROLE_USER", "ROLE_ANONYMOUS"})
    public List<Actions> getAllActions() {
        log.debug("REST request to get all Actions");
        return actionsRepository.findAll();
    }

    /**
     * {@code GET  /actions/get-all-action-by-post-id/{post_id}} : get all the actions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of actions in body.
     */
    @GetMapping("/actions/count-all-action-by-post-id/{postId}")
    @Secured({"ROLE_USER", "ROLE_ANONYMOUS"})
    public Optional<StaticsActions> countAllActionsPostById(@PathVariable Long postId) {
        log.debug("REST request to get all Actions");
        StaticsActions staticsActions = new StaticsActions();
        try {
            Optional<Posts> posts = postsRepository.findById(postId);
            List<Actions> actions = actionsRepository.findAllByPosts(posts.get());
            staticsActions = actionService.generateCountAction(actions);
        } catch (Exception e) {
            e.printStackTrace();
            Optional.empty();
        }
        return Optional.of(staticsActions);
    }

    /**
     * {@code GET  /actions/:id} : get the "id" actions.
     *
     * @param id the id of the actions to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the actions, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/actions/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Actions> getActions(@PathVariable Long id) {
        log.debug("REST request to get Actions : {}", id);
        Optional<Actions> actions = actionsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(actions);
    }

    /**
     * {@code DELETE  /actions/:id} : delete the "id" actions.
     *
     * @param id the id of the actions to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/actions/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteActions(@PathVariable Long id) {
        log.debug("REST request to delete Actions : {}", id);
        actionsRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     *
     * @param postId
     * @return
     */
    @GetMapping("/actions/get-by-user-and-post/{postId}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Optional<StaticsActions>> getActionsByUserAndPost(@PathVariable Long postId) {
        log.debug("REST request to get Actions : {}", postId);
        Optional<StaticsActions> staticsActions = actionService.getActionsByUser(postId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(staticsActions));
    }
}
