package nexters.moss.server.domain.service;

import nexters.moss.server.domain.model.Habit;
import nexters.moss.server.domain.model.HabitRecord;
import nexters.moss.server.domain.value.HabitStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitService {
    public void changeHabitsOrder (List<Habit> habits, int habitOrder, int changedOrder) {
        habits.get(habitOrder).changeOrder(changedOrder);

        if(habitOrder < changedOrder) {
            for (int index = habitOrder + 1; index <= changedOrder; index++) {
                habits.get(index).decreaseOneOrder();
            }
        } else {
            for (int index = changedOrder; index < habitOrder; index++) {
                habits.get(index).increaseOneOrder();
            }
        }
    }

    public void refreshHabitsOrderWhenDelete (List<Habit> habits, int deletedHabitOrder) {
        for(int index = deletedHabitOrder + 1; index < habits.size(); index++) {
            habits.get(index).decreaseOneOrder();
        }
    }
}
