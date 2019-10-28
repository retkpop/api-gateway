package com.tk.api.gateway.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

import java.io.Serializable;
import java.util.*;

/**
 * A Hashtag.
 */
@Entity
@Table(name = "hashtag")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Hashtag implements Serializable {

    private static final long serialVersionUID = 123L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @Cascade({})
    @ManyToMany(mappedBy = "hashtags", cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private List<Posts> posts = new ArrayList<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Hashtag name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public Hashtag slug(String slug) {
        this.slug = slug;
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public List<Posts> getPosts() {
        return posts;
    }

    public Hashtag posts(List<Posts> posts) {
        this.posts = posts;
        return this;
    }

    public Hashtag addPosts(Posts posts) {
        this.posts.add(posts);
        posts.getHashtags().add(this);
        return this;
    }

    public Hashtag removePosts(Posts posts) {
        this.posts.remove(posts);
        posts.getHashtags().remove(this);
        return this;
    }

    public void setPosts(List<Posts> posts) {
        this.posts = posts;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public Hashtag() { }

    public Hashtag(String name, String slug) {
        this.name = name;
        this.slug = slug;
    }
    public Hashtag(Long id, String name, String slug) {
        this.id = id;
        this.name = name;
        this.slug = slug;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hashtag)) {
            return false;
        }
        return id != null && id.equals(((Hashtag) o).id);
    }

    @Override
    public int hashCode() {
        Random rand = new Random();
        int result = rand.nextInt(100);
        result = 31 * result + name.hashCode();
        result = 31 * result + slug.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Hashtag{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", slug='" + getSlug() + "'" +
            "}";
    }
}
