package com.booking;

import java.util.List;

public interface BookingProcessor {

	/**
	 * Process input request for meetings. <br>
	 * The first line of the input text represents the company office hours, in
	 * 24 hour clock format, and the remainder of the input represents
	 * individual booking requests. Each booking request is in the following
	 * format.<br>
	 * [request submission time, in the format YYYY-MM-DD HH:MM:SS]
	 * [ARCH:employee id]<br>
	 * [meeting start time, in the format YYYY-MM-DD HH:MM] [ARCH:meeting
	 * duration in hours]
	 * 
	 * @param inputList
	 * @return Booking schedule, with bookings being grouped chronologically by
	 *         day.
	 */
	public String process(List<String> input);

}
