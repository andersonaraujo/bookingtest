package com.booking;

import com.booking.impl.Meeting;

public interface Schedule {

	/**
	 * Book a meeting.
	 * 
	 * @param currentMeeting
	 * @return True if the meeting is booked, False otherwise.
	 */
	public Boolean bookMeeting(Meeting currentMeeting);

	/**
	 * Prints the schedule.
	 * 
	 * @return Schedule printed.
	 */
	public String printSchedule();

}