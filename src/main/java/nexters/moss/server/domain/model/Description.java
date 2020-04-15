package nexters.moss.server.domain.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "description")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Description {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "description_id")
    private Long id;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "receive_piece_of_cake")
    private String receivePieceOfCake;

    @Column(name = "diary")
    private String diary;
}
/*
        0. 물마시기
        1. 스트레칭
        2. 명상하기
        3. 산책하기
        4. 뉴스보기
        5. 아침식
        6. 일기쓰기
        7. 독서하기
         */