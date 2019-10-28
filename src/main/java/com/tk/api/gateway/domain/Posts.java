package com.tk.api.gateway.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * A Posts.
 */
@Entity
@Table(name = "posts")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Posts implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "slug")
    private String slug;

    @Lob
    @Column(name = "excerpt")
    private String excerpt;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "id_video")
    private String idVideo;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "type")
    private Long type;

    @Column(name = "views")
    private Long views;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "reset_date")
    private ZonedDateTime resetDate;

    @Column(name = "last_update")
    private ZonedDateTime lastUpdate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private ZonedDateTime lastModifiedDate;

    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @ManyToMany(fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "posts_hashtag",
               joinColumns = @JoinColumn(name = "posts_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "hashtag_id", referencedColumnName = "id"))
    private List<Hashtag> hashtags = new ArrayList<>();


    @ManyToOne()
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    @ManyToOne()
    @JoinColumn(name = "cat_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Categories categories;


    @ManyToMany(mappedBy = "posts")
    @JsonIgnore
    private Set<Suggestions> suggestions = new HashSet<>();


    @OneToMany(targetEntity=Actions.class,
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        mappedBy = "posts")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Actions> actions = new HashSet<>();


    public Set<Actions> getActions() {
        return actions;
    }
    public Posts posts(Set<Actions> actions) {
        this.actions = actions;
        return this;
    }
    public void setActions(Set<Actions> actions) {
        this.actions = actions;
    }


    public Set<Suggestions> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(Set<Suggestions> suggestions) {
        this.suggestions = suggestions;
    }

    public Posts addSuggestions(Suggestions suggestions) {
        this.suggestions.add(suggestions);
        suggestions.getPosts().add(this);
        return this;
    }

    public Posts removeSuggestions(Suggestions suggestions) {
        this.suggestions.remove(suggestions);
        suggestions.getPosts().remove(this);
        return this;
    }

    public String getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(String idVideo) {
        this.idVideo = idVideo;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public Categories getCategories() {
        return categories;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Posts title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public Posts slug(String slug) {
        this.slug = slug;
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public Posts excerpt(String excerpt) {
        this.excerpt = excerpt;
        return this;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getDescription() {
        return description;
    }

    public Posts description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public Posts thumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Long getType() {
        return type;
    }

    public Posts type(Long type) {
        this.type = type;
        return this;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Posts createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getResetDate() {
        return resetDate;
    }

    public Posts resetDate(ZonedDateTime resetDate) {
        this.resetDate = resetDate;
        return this;
    }

    public void setResetDate(ZonedDateTime resetDate) {
        this.resetDate = resetDate;
    }

    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    public Posts lastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public void setLastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Posts lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Posts lastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public List<Hashtag> getHashtags() {
        return hashtags;
    }

    public Posts hashtags(List<Hashtag> hashtags) {
        this.hashtags = hashtags;
        return this;
    }

    public Posts addHashtag(Hashtag hashtag) {
        this.hashtags.add(hashtag);
        hashtag.getPosts().add(this);
        return this;
    }

    public Posts removeHashtag(Hashtag hashtag) {
        this.hashtags.remove(hashtag);
        hashtag.getPosts().remove(this);
        return this;
    }

    public void setHashtags(List<Hashtag> hashtags) {
        this.hashtags = hashtags;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Posts)) {
            return false;
        }
        return id != null && id.equals(((Posts) o).id);
    }

    @Override
    public int hashCode() {
        Random rand = new Random();
        int result = rand.nextInt(100);
        result = 31 * result + title.hashCode();
        result = 31 * result + slug.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Posts{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", slug='" + getSlug() + "'" +
            ", excerpt='" + getExcerpt() + "'" +
            ", description='" + getDescription() + "'" +
            ", idVideo='" + getIdVideo() + "'" +
            ", thumbnail='" + getThumbnail() + "'" +
            ", type=" + getType() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", resetDate='" + getResetDate() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
