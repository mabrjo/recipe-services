package com.mcccodeschool.recipeservices.model;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(
    name = "user",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
    }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String providerId;

    @Column(nullable = false)
    private String provider;


    private String photoUrl, username, displayName, name;

    // Set to false when user signs in with oauth, creating a new user entity, ->
    //   but has yet to set a username.
    //   front-end check for 'enabled' to determine if authorized to proceed to other pages
    private boolean enabled;
}