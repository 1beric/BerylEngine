package betrayal.util;

public class Date {

	private int m_Month;
	private int m_Day;

	public Date() {
		m_Month = java.time.LocalDate.now().getMonthValue();
		m_Day = java.time.LocalDate.now().getDayOfMonth();
	}

	public Date(int month, int day) {
		m_Month = month;
		m_Day = day;
	}

	public int month() {
		return m_Month;
	}

	public int day() {
		return m_Day;
	}

	public int daysUntil(Date date) {
		int months = (date.month() + 12 - month()) % 12;
		int days = (date.day() + 31 - day()) % 31;
		return months * 31 + days;
	}

	public String toString() {
		return month() + "/" + day();
	}

}
