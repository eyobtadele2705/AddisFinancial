create table _roles
(
    id      int            not null primary key default unique_rowid(),
    role_name    varchar(128)   not null,
    description    varchar(255)    not null
);



CREATE TABLE _users (
                       id VARCHAR(255) PRIMARY KEY,
                       username VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       first_name VARCHAR(255),
                       last_name VARCHAR(255),
                       phone_number VARCHAR(255),
                       date_of_birth VARCHAR(255),
                       income INTEGER,
                       account_locked BOOLEAN DEFAULT FALSE,
                       enabled BOOLEAN DEFAULT TRUE,
                       email_verified BOOLEAN DEFAULT FALSE,
                       created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_roles (
                            user_id VARCHAR(255) NOT NULL,
                            role_id INTEGER NOT NULL,
                            PRIMARY KEY (user_id, role_id),
                            FOREIGN KEY (user_id) REFERENCES _users(id) ON DELETE CASCADE,
                            FOREIGN KEY (role_id) REFERENCES _roles(id) ON DELETE CASCADE
);

CREATE TABLE verification_token (
                                    id SERIAL PRIMARY KEY,
                                    token VARCHAR(255) UNIQUE NOT NULL,
                                    expiration_time TIMESTAMP NOT NULL,
                                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    user_id VARCHAR(255),
                                    FOREIGN KEY (user_id) REFERENCES _users(id) ON DELETE CASCADE
);

CREATE TABLE coupon (
                        id SERIAL PRIMARY KEY,
                        coupon_number SERIAL,
                        available_coupons INTEGER,
                        coupon_value INTEGER,
                        coupon_type VARCHAR(125)
);

CREATE TABLE _bets (
                       id SERIAL PRIMARY KEY,
                       bet_description VARCHAR(125),
                       bet_amount INTEGER,
                       bet_type VARCHAR(125),
                       coupon_number INTEGER,
                       total_coupon INTEGER,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       user_id VARCHAR(255),
                       FOREIGN KEY (user_id) REFERENCES _users(id) ON DELETE CASCADE
);