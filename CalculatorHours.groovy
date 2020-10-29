import java.util.regex.Pattern
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.DayOfWeek
import java.time.Month
import java.time.temporal.ChronoUnit
import java.time.format.DateTimeFormatter
class CalculatorHoursTest extends GroovyTestCase{

	void testNull() {
    def listCases =[
      [dateBeforeString: "2020-10-02 18:30:56", dateAfterString: "2020-10-02 19:28:31", result: "0:29"],
      [dateBeforeString: "2020-10-08 11:36:49", dateAfterString: "2020-10-08 11:51:41", result: "0:14"],
      [dateBeforeString: "2020-10-08 12:00:18", dateAfterString: "2020-10-08 13:13:40", result: "1:12"],
      [dateBeforeString: "2020-10-08 13:13:40", dateAfterString: "2020-10-08 14:01:29", result: "0:47"],
      [dateBeforeString: "2020-10-16 18:49:39", dateAfterString: "2020-10-17 11:10:37", result: "0:10"],
      [dateBeforeString: "2020-10-10 18:49:39", dateAfterString: "2020-10-12 11:10:37", result: "2:10"],
      [dateBeforeString: "2020-10-16 21:00:00", dateAfterString: "2020-10-17 11:10:37", result: "0:0"],
      [dateBeforeString: "2020-10-16 21:00:00", dateAfterString: "2020-10-19 11:10:37", result: "2:10"],
    ]
    listCases.each(){
      CalculatorHours obj = new CalculatorHours(dateBeforeString: it.dateBeforeString, dateAfterString: it.dateAfterString)
		  assert obj.calculateHoursFromString() == it.result
    }
	}

}

class CalculatorHours {

  String dateBeforeString
  String dateAfterString

  def calculateHoursFromString(){
    LocalDate dateBefore = LocalDate.parse(this.dateBeforeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    LocalDate dateAfter = LocalDate.parse(this.dateAfterString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
    def daysWork = 0
    def hoursWork = 0
    (0..noOfDaysBetween).each { day ->
        def dayWithPlus = dateBefore.plusDays(day)
        //println dayWithPlus.getDayOfWeek().dump()
        def dayOfWeek = dayWithPlus.getDayOfWeek()
        if(dayWithPlus.getDayOfWeek() != DayOfWeek.SATURDAY && dayWithPlus.getDayOfWeek() != DayOfWeek.SUNDAY){
            daysWork++;
            hoursWork = hoursWork + 10;
        }
    }

    // Check first day
    LocalDateTime dateBeforeTime = LocalDateTime.parse(dateBeforeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    String initDay = dateBefore.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    LocalDateTime dayComplete = LocalDateTime.parse("${initDay} 19:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    long minutes = 0
    if(dateBeforeTime.getHour() > 9 && dateBeforeTime.getHour() < 19 && dateBefore.getDayOfWeek() != DayOfWeek.SUNDAY && dateBefore.getDayOfWeek() != DayOfWeek.SATURDAY){
        hoursWork = hoursWork - 10
        minutes = ChronoUnit.MINUTES.between(dateBeforeTime, dayComplete);
    }
    else if (dateBeforeTime.getHour() >= 19){
        hoursWork = hoursWork - 10
        minutes = 0
    }
    //println "HOla"
    //println minutes
    //println dateBeforeTime.getHour()
    //println dateBeforeTime.getMinute()

    //daysWork = daysWork - 1
    //hoursWork = hoursWork - 10;
    //

    // Check  last Day
    LocalDateTime dateAfterTime = LocalDateTime.parse(dateAfterString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    String finalDay = dateAfterTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    LocalDateTime dayCompleteFinal = LocalDateTime.parse("${finalDay} 09:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    long minutesFinal = 0

    if(dateAfterTime.getHour() > 9 && dateAfterTime.getHour() < 19 && dateAfter.getDayOfWeek() != DayOfWeek.SUNDAY && dateAfter.getDayOfWeek() != DayOfWeek.SATURDAY){
        hoursWork = hoursWork - 10
        minutesFinal = ChronoUnit.MINUTES.between(dayCompleteFinal, dateAfterTime);
    }
    else if (dateBeforeTime.getHour() >= 19){
        //minutesFinal = 10*60
    }
    //println "Minutos Finales"
    //println minutesFinal
    // Sum final Minutes


    def minutosTotal = minutes + minutesFinal
    int hoursFinal = minutosTotal / 60
    int minutesSub = minutosTotal % 60

    //println "days: ${daysWork}"
    //println "hours Without one and last: ${hoursWork}"
    println "Total hours: ${hoursWork +hoursFinal} with ${minutesSub} minutes"
    "${hoursWork +hoursFinal}:${minutesSub}"
  }
}