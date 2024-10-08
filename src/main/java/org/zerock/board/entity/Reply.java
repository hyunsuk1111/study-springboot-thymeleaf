package org.zerock.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString(exclude = "board")
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    @Column
    private String text;

    @Column
    private String replyer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

}
