package nexters.moss.server.domain;


import lombok.*;
import nexters.moss.server.domain.value.CakeType;
import nexters.moss.server.domain.value.Description;
import nexters.moss.server.domain.value.HabitType;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    private Long id;
    private HabitType habitType;
    private CakeType cakeType;
    private Description receiveCakeDescription;
    private Description diaryDescription;
}
