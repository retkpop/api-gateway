package com.tk.api.gateway.service.posts;

import com.tk.api.gateway.domain.Actions;
import com.tk.api.gateway.domain.Posts;
import com.tk.api.gateway.domain.User;
import com.tk.api.gateway.domain.clientRequest.ActionReq;
import com.tk.api.gateway.domain.clientResponse.StaticsActions;
import com.tk.api.gateway.domain.enumeration.Action;
import com.tk.api.gateway.repository.ActionsRepository;
import com.tk.api.gateway.repository.PostsRepository;
import com.tk.api.gateway.repository.UserRepository;
import com.tk.api.gateway.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ActionService {
    @Autowired
    PostsRepository postsRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ActionsRepository actionsRepository;

    public Optional<Actions> generateDataAction(ActionReq actionReq) {
        Actions actions = new Actions();
        try {
            Optional<Posts> posts = postsRepository.findById(actionReq.getPost_id());
            actions.setPosts(posts.get());
            Optional<String> username = SecurityUtils.getCurrentUserLogin();
            Optional<User> user = userRepository.findOneByLogin(username.get());
            actions.setUser(user.get());
            actions.setAction(actionReq.getAction());

            List<Actions> actionsValid = actionsRepository.findAllByUserAndPosts(user.get(), posts.get());
            if(actionsValid.size()>0) {
                for (Actions action : actionsValid) {
                    if(actionReq.getAction().compareTo(action.getAction())==0) {
                        actionsRepository.deleteById(action.getId());
                    } else if(actionReq.getAction().compareTo(Action.SAVE) != 0) {
                        actions.setCreatedDate(action.getCreatedDate());
                        actions.setId(action.getId());
                        actionsRepository.deleteById(action.getId());
                        actions.setLastUpdate(ZonedDateTime.now());
                        return Optional.of(actions);
                    }
                }
                Actions actionsCheck = actionsValid.stream().filter(actionF -> Action.SAVE.equals(actionF.getAction())).findAny().orElse(null);
                if(actionsCheck == null && actionReq.getAction().compareTo(Action.SAVE) == 0) {
                    actions.setCreatedDate(ZonedDateTime.now());
                    return Optional.of(actions);
                }
            } else {
                actions.setCreatedDate(ZonedDateTime.now());
                return Optional.of(actions);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    public StaticsActions generateCountAction(List<Actions> actions) {
        StaticsActions staticsActions = new StaticsActions();
        int countLike = 0;
        int countDislike = 0;
        int countSave = 0;
        int countShare = 0;
        for (Actions action : actions) {
            if(action.getAction().compareTo(Action.LIKE) == 0) {
                countLike = countLike + 1;
            }
            if(action.getAction().compareTo(Action.DISLIKE) == 0) {
                countDislike = countDislike + 1;
            }
            if(action.getAction().compareTo(Action.SAVE) == 0) {
                countSave = countSave + 1;
            }
            if(action.getAction().compareTo(Action.SHARE) == 0) {
                countShare = countShare + 1;
            }
        }
        staticsActions.setLike(countLike);
        staticsActions.setDislike(countDislike);
        staticsActions.setSave(countSave);
        staticsActions.setShare(countShare);
        return staticsActions;
    }
    public Optional<StaticsActions> getActionsByUser(Long postId) {
        StaticsActions staticsActions = new StaticsActions();
        try {
            Optional<String> username = SecurityUtils.getCurrentUserLogin();
            Optional<User> user = userRepository.findOneByLogin(username.get());
            Optional<Posts> posts = postsRepository.findById(postId);
            List<Actions> actions = actionsRepository.findAllByUserAndPosts(user.get(), posts.get());
            staticsActions = this.generateCountAction(actions);
        } catch (Exception e) {
            e.printStackTrace();
            Optional.empty();
        }
        return Optional.of(staticsActions);
    }
}
