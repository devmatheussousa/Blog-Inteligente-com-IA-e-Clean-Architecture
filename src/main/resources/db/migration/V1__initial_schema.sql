-- =========================
-- USERS
-- =========================
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       bio VARCHAR(500),
                       foto_url VARCHAR(255),
                       role VARCHAR(20) NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP,

                       CONSTRAINT uk_users_email UNIQUE (email),
                       CONSTRAINT ck_users_role CHECK (role IN ('USER', 'ADMIN'))
);

-- =========================
-- CATEGORIES
-- =========================
CREATE TABLE categories (
                            id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(120) NOT NULL,
                            slug VARCHAR(120) NOT NULL,
                            description VARCHAR(500),
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP,

                            CONSTRAINT uk_categories_name UNIQUE (name),
                            CONSTRAINT uk_categories_slug UNIQUE (slug)
);

-- =========================
-- TAGS
-- =========================
CREATE TABLE tags (
                      id BIGSERIAL PRIMARY KEY,
                      name VARCHAR(100) NOT NULL,
                      slug VARCHAR(120) NOT NULL,
                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP,

                      CONSTRAINT uk_tags_name UNIQUE (name),
                      CONSTRAINT uk_tags_slug UNIQUE (slug)
);

-- =========================
-- JOBS
-- =========================
CREATE TABLE jobs (
                      id BIGSERIAL PRIMARY KEY,
                      type VARCHAR(100) NOT NULL,
                      status VARCHAR(20) NOT NULL,
                      payload_json TEXT,
                      error_message VARCHAR(1000),
                      attempts INTEGER NOT NULL DEFAULT 0,
                      scheduled_at TIMESTAMP,
                      started_at TIMESTAMP,
                      finished_at TIMESTAMP,
                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP,

                      CONSTRAINT ck_jobs_status CHECK (status IN ('PENDING', 'RUNNING', 'COMPLETED', 'FAILED'))
);

-- =========================
-- POSTS
-- =========================
CREATE TABLE posts (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(180) NOT NULL,
                       slug VARCHAR(200) NOT NULL,
                       excerpt VARCHAR(400),
                       content TEXT NOT NULL,
                       status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
                       published_at TIMESTAMP,
                       author_id BIGINT NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP,

                       CONSTRAINT uk_posts_slug UNIQUE (slug),
                       CONSTRAINT ck_posts_status CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED')),
                       CONSTRAINT fk_posts_author_id FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE RESTRICT
);

-- =========================
-- COMMENTS
-- =========================
CREATE TABLE comments (
                          id BIGSERIAL PRIMARY KEY,
                          content VARCHAR(2000) NOT NULL,
                          author_name VARCHAR(120) NOT NULL,
                          author_email VARCHAR(255),
                          approved BOOLEAN NOT NULL DEFAULT FALSE,
                          post_id BIGINT NOT NULL,
                          parent_id BIGINT,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP,

                          CONSTRAINT fk_comments_post_id FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
                          CONSTRAINT fk_comments_parent_id FOREIGN KEY (parent_id) REFERENCES comments(id) ON DELETE CASCADE
);

-- =========================
-- POST REVISIONS
-- =========================
CREATE TABLE post_revisions (
                                id BIGSERIAL PRIMARY KEY,
                                version_number INTEGER NOT NULL,
                                title VARCHAR(180) NOT NULL,
                                content TEXT NOT NULL,
                                summary VARCHAR(500),
                                post_id BIGINT NOT NULL,
                                revised_by_user_id BIGINT NOT NULL,
                                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                CONSTRAINT fk_post_revisions_post_id FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
                                CONSTRAINT fk_post_revisions_user_id FOREIGN KEY (revised_by_user_id) REFERENCES users(id) ON DELETE RESTRICT
);

-- =========================
-- EMBEDDINGS
-- =========================
CREATE TABLE embeddings (
                            id BIGSERIAL PRIMARY KEY,
                            model_name VARCHAR(100) NOT NULL,
                            vector_json TEXT NOT NULL,
                            post_id BIGINT NOT NULL,
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP,

                            CONSTRAINT uk_embeddings_post_id UNIQUE (post_id),
                            CONSTRAINT fk_embeddings_post_id FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);

-- =========================
-- VIEW EVENTS
-- =========================
CREATE TABLE view_events (
                             id BIGSERIAL PRIMARY KEY,
                             post_id BIGINT NOT NULL,
                             viewer_id VARCHAR(45),
                             user_agent VARCHAR(500),
                             referrer VARCHAR(500),
                             event_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                             CONSTRAINT fk_view_events_post_id FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);

-- =========================
-- N:N RELATIONSHIPS
-- =========================
CREATE TABLE post_categories (
                                 post_id BIGINT NOT NULL,
                                 category_id BIGINT NOT NULL,
                                 PRIMARY KEY (post_id, category_id),

                                 CONSTRAINT fk_post_categories_post_id FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
                                 CONSTRAINT fk_post_categories_category_id FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

CREATE TABLE post_tags (
                           post_id BIGINT NOT NULL,
                           tag_id BIGINT NOT NULL,
                           PRIMARY KEY (post_id, tag_id),

                           CONSTRAINT fk_post_tags_post_id FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
                           CONSTRAINT fk_post_tags_tag_id FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
);

-- =========================
-- INDEXES
-- =========================
CREATE INDEX idx_posts_author_id ON posts (author_id);

CREATE INDEX idx_comments_post_id ON comments (post_id);
CREATE INDEX idx_comments_parent_id ON comments (parent_id);

CREATE INDEX idx_post_revisions_post_id ON post_revisions (post_id);
CREATE INDEX idx_post_revisions_user_id ON post_revisions (revised_by_user_id);

CREATE INDEX idx_embeddings_post_id ON embeddings (post_id);

CREATE INDEX idx_view_events_post_id ON view_events (post_id);
CREATE INDEX idx_view_events_event_at ON view_events (event_at);

CREATE INDEX idx_jobs_status_scheduled_at ON jobs (status, scheduled_at);

CREATE INDEX idx_post_categories_category_id ON post_categories (category_id);
CREATE INDEX idx_post_tags_tag_id ON post_tags (tag_id);