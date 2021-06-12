package com.ghpark.hotalk.security.model;


import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * @author pkh879
 */

@Data
@Document(value="users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    private String id;
    private String email;
    private String password;
    private String provider;
    private LocalDateTime createDate;
    private LocalDateTime authDate;
}