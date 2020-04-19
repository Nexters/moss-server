package nexters.moss.server.habit;

import nexters.moss.server.domain.habit.Habit;
import nexters.moss.server.domain.habit.HabitService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class HabitServiceTest {
    @Autowired
    HabitService habitService;

    List<Habit> testHabits = new ArrayList<>();

    @Before
    public void setup() {
        testHabits = new ArrayList<>();
        // 실제 db의 id는 1부터 시작
        for(int index = 0 ; index < 5 ; index++) {
            Habit habit = new Habit(
                    (long)index,
                    null,
                    null,
                    null,
                    index,
                    false,
                    0
            );
            testHabits.add(habit);
        }
    }

    /*
    0 -> 4
    1 -> 0
    2 -> 1
    3 -> 2
    4 -> 3
     */
    @Test
    public void changeHabitsOrderTest1() {
        habitService.changeHabitsOrder(testHabits, 0, 4);
        Assert.assertEquals(Integer.valueOf(3), testHabits.get(4).getOrder());
        Assert.assertEquals(Integer.valueOf(4), testHabits.get(0).getOrder());
    }

    /*
    0 -> 0
    1 -> 1
    2 -> 3
    3 -> 2
    4 -> 4
     */
    @Test
    public void changeHabitsOrderTest2() {
        habitService.changeHabitsOrder(testHabits, 2, 3);
        Assert.assertEquals(Integer.valueOf(2), testHabits.get(3).getOrder());
        Assert.assertEquals(Integer.valueOf(3), testHabits.get(2).getOrder());
    }

    /*
    0 -> 0
    1 -> 2
    2 -> 3
    3 -> 4
    4 -> 1
     */
    @Test
    public void changeHabitsOrderTest3() {
        habitService.changeHabitsOrder(testHabits, 4, 1);
        Assert.assertEquals(Integer.valueOf(2), testHabits.get(1).getOrder());
        Assert.assertEquals(Integer.valueOf(1), testHabits.get(4).getOrder());
    }

    /*
    0 -> 1
    1 -> 2
    2 -> 3
    3 -> 0
    4 -> 4
     */
    @Test
    public void changeHabitsOrderTest4() {
        habitService.changeHabitsOrder(testHabits, 3, 0);
        Assert.assertEquals(Integer.valueOf(1), testHabits.get(0).getOrder());
        Assert.assertEquals(Integer.valueOf(0), testHabits.get(3).getOrder());
    }
}
