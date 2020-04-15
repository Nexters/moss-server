package nexters.moss.server.domain.value;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryType {
    private Long id;
    private HabitType habitType;
    private CakeType cakeType;
}
