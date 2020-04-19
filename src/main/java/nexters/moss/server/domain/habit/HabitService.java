package nexters.moss.server.domain.habit;

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
