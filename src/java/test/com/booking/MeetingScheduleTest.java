package com.booking;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.booking.impl.Meeting;
import com.booking.impl.ScheduleImpl;

public class MeetingScheduleTest {

	ScheduleImpl meetingSchedule = new ScheduleImpl();

	private SimpleDateFormat sdfMeetingTime = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	@Test
	public void whenBookAMeeting() throws ParseException {

		Meeting meeting = Meeting.builder().withSubmission(new Date())
				.withEmployeeId("EMP 001")
				.withStart(sdfMeetingTime.parse("2014-11-01 16:00"))
				.withDuration(1).withOfficeStart("0800").withOfficeEnd("1700")
				.build();

		Boolean ret = meetingSchedule.bookMeeting(meeting);
		Assert.assertTrue(ret);

		String print = meetingSchedule.printSchedule();

		String correctPrint = "2014-11-01\n16:00 17:00 EMP 001\n";

		Assert.assertEquals(print, correctPrint);

	}

	@Test
	public void whenBookInvalidHoursMeeting() throws ParseException {

		Meeting meeting = Meeting.builder().withSubmission(new Date())
				.withEmployeeId("EMP 001")
				.withStart(sdfMeetingTime.parse("2014-11-01 16:00"))
				.withDuration(2).withOfficeStart("0800").withOfficeEnd("1700")
				.build();

		Boolean ret = meetingSchedule.bookMeeting(meeting);
		Assert.assertFalse(ret);

		String print = meetingSchedule.printSchedule();

		String correctPrint = "";

		Assert.assertEquals(print, correctPrint);

	}

	@Test(expected = NullPointerException.class)
	public void whenBookNullMeeting() throws ParseException {

		meetingSchedule.bookMeeting(null);

	}

	@Test
	public void whenBookOverlapMeeting() throws ParseException {

		Meeting meeting = Meeting.builder().withSubmission(new Date())
				.withEmployeeId("EMP 001")
				.withStart(sdfMeetingTime.parse("2014-11-01 16:00"))
				.withDuration(1).withOfficeStart("0800").withOfficeEnd("1700")
				.build();

		Boolean ret = meetingSchedule.bookMeeting(meeting);
		Assert.assertTrue(ret);

		String print = meetingSchedule.printSchedule();

		String correctPrint = "2014-11-01\n16:00 17:00 EMP 001\n";

		Assert.assertEquals(print, correctPrint);

		// Second meeting (overlap)

		Meeting meeting2 = Meeting.builder().withSubmission(new Date())
				.withEmployeeId("EMP 001")
				.withStart(sdfMeetingTime.parse("2014-11-01 16:00"))
				.withDuration(1).withOfficeStart("0800").withOfficeEnd("1700")
				.build();

		Boolean ret2 = meetingSchedule.bookMeeting(meeting2);
		Assert.assertFalse(ret2);

		String print2 = meetingSchedule.printSchedule();

		Assert.assertEquals(print2, correctPrint);

	}

}
