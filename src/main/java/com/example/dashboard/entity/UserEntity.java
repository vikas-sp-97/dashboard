package com.example.dashboard.entity;

import com.example.dashboard.enums.ActiveStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

//    @NonNull
    private String userName;

    private String password;

    @Enumerated(EnumType.STRING)
    private ActiveStatus activeStatus;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    @JoinTable(name = "user_roles",
            joinColumns =@JoinColumn(name="user_id", referencedColumnName="user_id"),
            inverseJoinColumns =@JoinColumn(name="role_id", referencedColumnName="role_id"))
    private List<Role> roles;

}
