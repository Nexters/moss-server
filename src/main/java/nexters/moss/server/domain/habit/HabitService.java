package nexters.moss.server.domain.habit;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitService {
    public void changeHabitsOrder(List<Habit> habits, int habitOrder, int changedOrder) {
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

    public void refreshHabitsOrder(List<Habit> habits) {
        for(int index = getDeletedHabitOrder(habits) + 1; index < habits.size(); index++) {
            habits.get(index).decreaseOneOrder();
        }
    }

    private int getDeletedHabitOrder(List<Habit> habits) {
        for(int index = 0 ; index < habits.size() ; index++) {
            if(habits.get(index).getOrder() != index) {
                return habits.get(index).getOrder() - 1;
            }
        }

        return habits.size() - 1;
    }
}
