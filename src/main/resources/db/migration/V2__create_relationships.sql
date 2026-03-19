ALTER TABLE posts
    ADD CONSTRAINT fk_posts_author
        FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE RESTRICT;

ALTER TABLE comments
    ADD CONSTRAINT fk_comments_post
        FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_comments_parent
        FOREIGN KEY (parent_id) REFERENCES comments (id) ON DELETE CASCADE;

ALTER TABLE post_revisions
    ADD CONSTRAINT fk_post_revisions_post
        FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_post_revisions_revised_by
        FOREIGN KEY (revised_by_user_id) REFERENCES users (id) ON DELETE RESTRICT;

ALTER TABLE embeddings
    ADD CONSTRAINT fk_embeddings_post
        FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE;

ALTER TABLE view_events
    ADD CONSTRAINT fk_view_events_post
        FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE;

CREATE TABLE post_categories (
    post_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (post_id, category_id),
    CONSTRAINT fk_post_categories_post
        FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
    CONSTRAINT fk_post_categories_category
        FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE
);

CREATE TABLE post_tags (
    post_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (post_id, tag_id),
    CONSTRAINT fk_post_tags_post
        FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
    CONSTRAINT fk_post_tags_tag
        FOREIGN KEY (tag_id) REFERENCES tags (id) ON DELETE CASCADE
);

CREATE INDEX idx_posts_author_id ON posts (author_id);
CREATE INDEX idx_comments_post_id ON comments (post_id);
CREATE INDEX idx_comments_parent_id ON comments (parent_id);
CREATE INDEX idx_post_revisions_post_id ON post_revisions (post_id);
CREATE INDEX idx_post_revisions_revised_by_user_id ON post_revisions (revised_by_user_id);
CREATE INDEX idx_view_events_post_id ON view_events (post_id);
CREATE INDEX idx_view_events_event_at ON view_events (event_at);
CREATE INDEX idx_jobs_status_scheduled_at ON jobs (status, scheduled_at);
CREATE INDEX idx_post_categories_category_id ON post_categories (category_id);
CREATE INDEX idx_post_tags_tag_id ON post_tags (tag_id);
