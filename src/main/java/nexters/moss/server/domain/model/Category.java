package nexters.moss.server.domain.model;


import lombok.*;
import nexters.moss.server.domain.value.CakeType;
import nexters.moss.server.domain.value.HabitType;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    private Long id;
    private HabitType habitType;
    private CakeType cakeType;
}
