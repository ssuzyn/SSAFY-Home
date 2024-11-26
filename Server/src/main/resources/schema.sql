use ssafyhome;

CREATE TABLE members (
                         user_id VARCHAR(50) PRIMARY KEY,         -- 사용자 ID
                         user_name VARCHAR(100) NOT NULL,         -- 사용자 이름
                         user_pwd VARCHAR(255) NOT NULL,          -- 비밀번호
                         email_id VARCHAR(200) NOT NULL,          -- 이메일
                         email_domain VARCHAR(100) NOT NULL,      -- 이메일 도메
                         gender ENUM('M', 'F') NOT NULL,          -- 성별 (M: 남성, F: 여성)
                         age INT UNSIGNED NOT NULL,               -- 나이
                         phone_number VARCHAR(15) NOT NULL,        -- 전화번호
                         token VARCHAR(255) DEFAULT NULL,         -- 토큰
                         join_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 가입일
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE fileinfo (
                          file_id INT AUTO_INCREMENT PRIMARY KEY,      -- 파일 고유 ID
                          user_id VARCHAR(50) DEFAULT NULL,                -- 사용자 ID (외래 키)
                          original_name VARCHAR(255) NOT NULL,         -- 업로드한 파일의 원래 이름
                          save_path VARCHAR(255) NOT NULL,             -- 서버에 저장된 파일 경로
                          upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 업로드 날짜
                          FOREIGN KEY (user_id) REFERENCES members(user_id)
                              ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE IF NOT EXISTS `interest_apt` (
                                              `user_id` VARCHAR(20) NOT NULL,              # 사용자 ID (외래 키)
    `apt_seq` VARCHAR(20) NOT NULL,              # 아파트 코드 (외래 키)
    `create_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  # 관심 아파트 등록 시간
    PRIMARY KEY (`user_id`, `apt_seq`),   # 복합 키 (user_id, apt_seq로 식별)
    CONSTRAINT `fk_interest_apt_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `members` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    CONSTRAINT `fk_interest_apt_houseinfos`
    FOREIGN KEY (`apt_seq`)
    REFERENCES `houseinfos` (`apt_seq`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
    ) ENGINE = InnoDB;


CREATE TABLE `board` (
                         `article_no` int NOT NULL AUTO_INCREMENT,
                         `user_id` varchar(16) DEFAULT NULL,
                         `subject` varchar(100) DEFAULT NULL,
                         `content` varchar(2000) DEFAULT NULL,
                         `hit` int DEFAULT '0',
                         `register_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         PRIMARY KEY (`article_no`),
                         KEY `fk_user_id` (`user_id`),
                         CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `members` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE IF NOT EXISTS `comments` (
                                          comment_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, -- 댓글 고유 ID
                                          article_no INT NOT NULL,                                -- QnA 번호 (외래 키)
                                          user_id VARCHAR(20) NULL DEFAULT NULL,             -- 댓글 작성자 ID (외래 키)
    content TEXT NOT NULL,                             -- 댓글 내용
    register_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 작성 시간
    CONSTRAINT `fk_comments_qna`
    FOREIGN KEY (`article_no`)
    REFERENCES `board` (`article_no`)                    -- QnA 테이블 참조
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    CONSTRAINT `fk_comments_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `members` (`user_id`)                 -- 사용자 테이블 참조
    ON DELETE SET NULL
    ON UPDATE CASCADE
    ) ENGINE = InnoDB;


CREATE TABLE `sidocodes` (
                             `sido_code` varchar(10) NOT NULL,
                             `sido_name` varchar(30) DEFAULT NULL,
                             PRIMARY KEY (`sido_code`),
                             UNIQUE KEY `sido_name` (`sido_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `guguncodes` (
                                            `gugun_code` VARCHAR(10) NOT NULL,
    `gugun_name` VARCHAR(30) NULL DEFAULT NULL,
    PRIMARY KEY (`gugun_code`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

-- dong_code 컬럼 추가 및 houseinfos 수정
ALTER TABLE `houseinfos`
    ADD COLUMN `dong_code` VARCHAR(10) NULL DEFAULT NULL,
ADD CONSTRAINT `fk_dong_code`
    FOREIGN KEY (`dong_code`)
    REFERENCES `dongcodes` (`dong_code`)
    ON DELETE SET NULL
    ON UPDATE CASCADE;

SET SQL_SAFE_UPDATES = 0;
UPDATE houseinfos h
    JOIN dongcodes d ON CONCAT(h.sgg_cd, h.umd_cd) = d.dong_code
    SET h.dong_code = d.dong_code;

CREATE TABLE IF NOT EXISTS `news` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(255) NOT NULL,
    `link` VARCHAR(500) NOT NULL,
    `date` DATE NOT NULL,
    `thumbnail` VARCHAR(500) NULL DEFAULT NULL,
    `category` VARCHAR(50) NOT NULL,
    `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
    )
    ENGINE = InnoDB
    AUTO_INCREMENT = 106
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `latest_housedeals` (
                                     `no` INT NOT NULL AUTO_INCREMENT,
                                     `apt_seq` VARCHAR(20) NOT NULL,
                                     `apt_dong` VARCHAR(40) NULL DEFAULT NULL,
                                     `floor` VARCHAR(3) NULL DEFAULT NULL,
                                     `deal_year` INT NULL DEFAULT NULL,
                                     `deal_month` INT NULL DEFAULT NULL,
                                     `deal_day` INT NULL DEFAULT NULL,
                                     `exclu_use_ar` DECIMAL(7,2) NULL DEFAULT NULL,
                                     `deal_amount` VARCHAR(10) NULL DEFAULT NULL,
                                     `deal_count` INT NULL DEFAULT NULL,
                                     PRIMARY KEY (`no`),
                                     UNIQUE KEY `uk_apt_seq` (`apt_seq`),
                                     CONSTRAINT `fk_latest_housedeals_apt_seq`
                                         FOREIGN KEY (`apt_seq`)
                                             REFERENCES `houseinfos` (`apt_seq`)
                                             ON DELETE CASCADE
                                             ON UPDATE CASCADE
);

CREATE TABLE house_price_stats (
                                   apt_seq VARCHAR(20) NOT NULL,
                                   apt_nm VARCHAR(100),
                                   latest_deal_amount VARCHAR(10),
                                   prev_deal_amount VARCHAR(10),
                                   price_change_rate DECIMAL(8,1),
                                   deal_count INT NULL DEFAULT NULL,
                                   last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   PRIMARY KEY (apt_seq),
                                   FOREIGN KEY (apt_seq) REFERENCES houseinfos(apt_seq)
);