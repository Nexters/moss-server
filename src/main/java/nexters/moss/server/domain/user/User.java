package nexters.moss.server.domain.user;

import lombok.*;
import nexters.moss.server.domain.Category;
import nexters.moss.server.domain.TimeProvider;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends TimeProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "social_id")
    private Long socialId;

    @Column(name = "habikery_token")
    @Setter
    private String habikeryToken;

    @Column(name = "nickname")
    private String nickname;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinColumn(name = "check_list")
    private List<Long> checkList = new ArrayList();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    @Builder.Default
    private List<Report> reports = new ArrayList<>();

    public void reported(String reason) {
        reports.add(
                Report.builder()
                        .reason(reason)
                        .build()
        );
    }

    public void checkCategory(Category category) {
        this.checkList.add(category.getId());
    }

    public boolean isCheckedCategory(Category category) {
        return this.checkList.stream()
                .anyMatch((checkedCategoryId) -> checkedCategoryId.equals(category.getId()));
    }
}
