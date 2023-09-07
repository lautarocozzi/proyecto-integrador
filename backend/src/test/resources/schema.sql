DROP TABLE IF EXISTS user_app;

CREATE TABLE user_app (
                          id LONG AUTO_INCREMENT  PRIMARY KEY,
                          email VARCHAR(255) NOT NULL,
                          password VARCHAR(255) NOT NULL
);

INSERT INTO user_app (email, password) VALUES
    ('Ibis@gmail.com', '$2a$10$jRASmaRhW4AJGRR66cQja.BiEGB8VrEpepduYpSdQnT8WNJF81obi');