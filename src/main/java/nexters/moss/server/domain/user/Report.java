package nexters.moss.server.domain.user;

import lombok.*;
import nexters.moss.server.domain.TimeProvider;

import javax.persistence.*;

@Entity
@Table(name = "reports")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends TimeProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "reason")
    private String reason;
}
