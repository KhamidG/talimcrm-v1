package com.gaipov.talim_crm.entity;

import com.gaipov.talim_crm.enums.UserRole;
import com.gaipov.talim_crm.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "students_entity")
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String fullName;

    @Column
    private String phoneNum;

    @Column
    @Enumerated(EnumType.STRING)
    private UserRole roles = UserRole.STUDENT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private GroupEntity group;

    @Column
    private UserStatus status;

    @CreatedDate
    private LocalDate created_at;

    @Column
    private LocalDate deleted_at;
}
