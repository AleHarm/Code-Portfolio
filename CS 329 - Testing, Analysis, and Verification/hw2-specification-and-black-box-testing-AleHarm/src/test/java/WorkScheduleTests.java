import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.Console;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import misc.Misc;

public class WorkScheduleTests {

  // /**
  // * @requires
  // * size >=0
  // * size != null
  // *
  // * @ensures
  // * schedule != null
  // * schedule.hours.size = size
  // * schedule.hours != null
  // * forall hours in schedule, hour.numberOfEmployees != null &&
  // hour.numberOfEmployees = 0
  // * forall hours in schedule, hour.workingEmployees != null
  // * forall hours in schedule, forall employees in hour.workingEmployees,
  // workingEmployees[i] = ""
  // */
  // public WorkSchedule(int size) {}

  // /**
  // * @requires
  // * schedule != null
  // *
  // * @ensures
  // * old(schedule.hours[time]) = schedule.hours[time]
  // * hour[time] != null
  // * time >= 0
  // * time < schedule.hours.size
  // * time != null
  // */
  // public Hour readSchedule(int time) {
  // }

  // /**
  // *
  // * @requires
  // * schedule != null
  // *
  // * @ensures
  // * nemployee >= 0
  // * nemployee < TotalExployees
  // * nemployee != null
  // * starttime != null
  // * starttime >= 0
  // * starttime < schedule.hours.size
  // * endtime != null
  // * endtime >= 0
  // * endtime >= starttime
  // * endtime < schedule.hours.size
  // * forall i where starttime <= i <= endtime,
  // schedule.hours[i].numberOfEmployees = nemployee
  // *
  // */
  // public void setRequiredNumber(int nemployee, int starttime, int endtime) {
  // }

  // /**
  // * @requires
  // * schedule != null
  // *
  // * @ensures
  // * nemployee != null
  // * starttime != null
  // * starttime >= 0
  // * starttime < schedule.hours.size
  // * endtime != null
  // * endtime >= 0
  // * endtime >= starttime
  // * endtime < schedule.hours.size
  // * forall i where starttime <= i <= endtime, there exists some j such that
  // schedule.hours[i].workingEmployees[j] = employee
  // * forall i in schedule.hours where starttime <= i <= endtime such that
  // schedule.hours[i] != null
  // *
  // *
  // */
  // public boolean addWorkingPeriod(String employee, int starttime, int endtime)
  // {
  // }

  // /**
  // * @requires
  // * schedule != null
  // *
  // * @ensures
  // * starttime != null
  // * starttime >= 0
  // * starttime < schedule.hours.size
  // * endtime != null
  // * endtime >= 0
  // * endtime >= starttime
  // * endtime < schedule.hours.size
  // * old(schedule) = schedule
  // * forall i in schedule.hours where starttime <= i <= endtime such that
  // schedule.hours[i] != null
  // *
  // */
  // public String[] workingEmployees(int starttime, int endtime) {
  // }

  // /**
  // * returns the closest time starting from currenttime for which the required
  // * amount of employees has not yet been scheduled.
  // *
  // * @required
  // * schedule != null
  // *
  // * @ensures
  // * currenttime != null
  // * currenttime >= 0
  // * currenttime < schedule.hours.size
  // * forall i in schedule.hours where currenttime <= i <
  // * schedule.hours.size such that schedule.hours[i] != null
  // * returnVal != null
  // */
  // public int nextIncomplete(int currenttime) {
  // }

  static int MAX_HOURS = 12;
  static WorkSchedule ws = null;
  int validTime = 2;
  int validTime2 = 4;
  int invalidTime = 14;
  int invalidTime2 = 16;

  @BeforeAll
  @DisplayName("Setup")
  static void Setup() {

    ws = new WorkSchedule(MAX_HOURS);
  }

  //
  // readSchedule() Testing
  //

  @Test
  @DisplayName("readSchedule normal inputs succeeds")
  void ReadScheduleValidTimeSuceeds() {

    if (ws.readSchedule(validTime) == null) {

      fail();
    }
  }

  @Test
  @DisplayName("readSchedule time out of bounds returns error")
  void ReadScheduleInvalidTimeReturnsError() {

    try {

      ws.readSchedule(invalidTime);
      fail();
    } catch (Exception e) {

    }
  }

  //
  // nextIncomplete() testing
  //

  @Test
  @DisplayName("nextIncomplete normal inputs succeeds")
  void NextIncompleteValidTimeSucceeds() {

    assertEquals(2, ws.nextIncomplete(validTime));
    // REturning -1 when expected 2, maybe spec is not specific enough
  }

  @Test
  @DisplayName("nextIncomplete time out of bounds returns error")
  void NextIncompleteInvalidTimeReturnsError() {

    try {

      ws.nextIncomplete(invalidTime);
      fail();
    } catch (Error e) {

    }
  }

  //
  // workingEmployees() testing
  //

  @Test
  @DisplayName("workingEmployees normal inputs succeeds")
  void WorkingEmployeesValidTimeSucceeds() {

    assertNotNull(ws.workingEmployees(validTime, validTime2));
  }

  @Test
  @DisplayName("workingEmployees time out of bounds returns error")
  void WorkingEmployeesInvalidTimeReturnsError() {

    try {

      ws.workingEmployees(invalidTime, invalidTime2);
      fail();
    } catch (Exception e) {

    }
  }
}
