package org.zerock.mreview.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "org.zerock.mreview.entity.Member")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
@Table(name = "m_member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mid;

    private String email;

    private String pw;

    private String nickName;
}
