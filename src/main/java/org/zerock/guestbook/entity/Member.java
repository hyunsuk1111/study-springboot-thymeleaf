package org.zerock.guestbook.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class Member extends BaseEntity{

    @Id
    private String email;

    @Column
    private String password;

    @Column
    private String name;

}
