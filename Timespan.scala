import java.util.Date

object TimeHelpers {
  case class TimeSpanBuilder(val len: Long) {
    def seconds = TimeSpan(TimeHelpers.seconds(len))
    //so seconds and second are the same, support both
    def second = seconds
    def minutes = TimeSpan(TimeHelpers.minutes(len))
    def minute = minutes
    def hours = TimeSpan(TimeHelpers.hours(len))
    def hour = hours
    def days = TimeSpan(TimeHelpers.days(len))
    def day = days
    def weeks = TimeSpan(TimeHelpers.weeks(len))
    def week = weeks
  }

  def seconds(in: Long): Long = in * 1000L
  def minutes(in: Long): Long = seconds(in) * 60L
  def hours(in: Long): Long = minutes(in) * 60L
  def days(in: Long): Long = hours(in) * 24L
  def weeks(in: Long): Long = days(in) * 7L

  /*
   * it allow to call 1.days so 1 will convert to TimeSpanBuilder
   * instead of (new TimeSpanBuilder(1)).days
   */
  implicit def longToTimeSpanBuilder(in: Long): TimeSpanBuilder =
  TimeSpanBuilder(in)

  implicit def intToTimeSpanBuilder(in: Int): TimeSpanBuilder =
  TimeSpanBuilder(in)

  def millis = System.currentTimeMillis

  //Ordered for compare; case class will have millis field
  case class TimeSpan(millis: Long) extends Ordered[TimeSpan] {
    def later = new Date(millis + TimeHelpers.millis)
    def ago = new Date(TimeHelpers.millis - millis)
    def +(in: TimeSpan) = TimeSpan(this.millis + in.millis)
    def -(in: TimeSpan) = TimeSpan(this.millis - in.millis)

    def compare(other: TimeSpan) = millis compare other.millis
  }

  object TimeSpan {
    implicit def tsToMillis(in: TimeSpan): Long = in.millis
  }

  class DateMath(d: Date) {
    def +(ts: TimeSpan) = new Date(d.getTime + ts.millis)
    def -(ts: TimeSpan) = new Date(d.getTime - ts.millis)
  }

  /* it will call when Date + TimeSpan 
   * so Date first convert to DateMath object
   */
  implicit def dateToDM(d: Date) = new DateMath(d)
}
