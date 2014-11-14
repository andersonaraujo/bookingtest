package com.booking;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.booking.impl.Meeting;

public class MeetingTest {

	private SimpleDateFormat sdfMeetingTime = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	@Before
	public void init() {
	}

	@Test
	public void whenMeetingIsInOfficeHours() throws ParseException {

		Date startDate = sdfMeetingTime.parse("2014-11-01 08:00");
		Integer duration = 2;

		Meeting meeting = Meeting.builder().withSubmission(new Date())
				.withEmployeeId("EMP 001").withStart(startDate)
				.withDuration(duration).withOfficeStart("0800")
				.withOfficeEnd("1700").build();

		Boolean ret = meeting.isInOfficeHours();

		Assert.assertTrue(ret);

	}

	@Test
	public void whenMeetingIsInNotOfficeHours() throws ParseException {

		Date startDate = sdfMeetingTime.parse("2014-11-01 18:00");
		Integer duration = 2;

		Meeting meeting = Meeting.builder().withSubmission(new Date())
				.withEmployeeId("EMP 001").withStart(startDate)
				.withDuration(duration).withOfficeStart("0800")
				.withOfficeEnd("1700").build();

		Boolean ret = meeting.isInOfficeHours();

		Assert.assertFalse(ret);

	}

	@Test
	public void whenPartOfAMeetingIsInNotOfficeHours() throws ParseException {

		Date startDate = sdfMeetingTime.parse("2014-11-01 16:00");
		Integer duration = 2;

		Meeting meeting = Meeting.builder().withSubmission(new Date())
				.withEmployeeId("EMP 001").withStart(startDate)
				.withDuration(duration).withOfficeStart("0800")
				.withOfficeEnd("1700").build();

		Boolean ret = meeting.isInOfficeHours();

		Assert.assertFalse(ret);

	}

	@Test
	public void whenMeetingNotOverlap() throws ParseException {

		Meeting meeting = Meeting.builder().withSubmission(new Date())
				.withEmployeeId("EMP 001")
				.withStart(sdfMeetingTime.parse("2014-11-01 16:00"))
				.withDuration(1).withOfficeStart("0800").withOfficeEnd("1700")
				.build();

		Meeting meeting2 = Meeting.builder().withSubmission(new Date())
				.withEmployeeId("EMP 002")
				.withStart(sdfMeetingTime.parse("2014-11-01 12:00"))
				.withDuration(1).withOfficeStart("0800").withOfficeEnd("1700")
				.build();

		List<Meeting> meetingList = new ArrayList<Meeting>();
		meetingList.add(meeting2);

		Boolean ret = meeting.overlapAny(meetingList);

		Assert.assertFalse(ret);

	}

	@Test
	public void whenMeetingOverlap() throws ParseException {

		Meeting meeting = Meeting.builder().withSubmission(new Date())
				.withEmployeeId("EMP 001")
				.withStart(sdfMeetingTime.parse("2014-11-01 16:00"))
				.withDuration(1).withOfficeStart("0800").withOfficeEnd("1700")
				.build();

		Meeting meeting2 = Meeting.builder().withSubmission(new Date())
				.withEmployeeId("EMP 002")
				.withStart(sdfMeetingTime.parse("2014-11-01 15:00"))
				.withDuration(2).withOfficeStart("0800").withOfficeEnd("1700")
				.build();

		List<Meeting> meetingList = new ArrayList<Meeting>();
		meetingList.add(meeting2);

		Boolean ret = meeting.overlapAny(meetingList);

		Assert.assertTrue(ret);

	}

	@Test
	public void whenMeetingOverlapAgain() throws ParseException {

		Meeting meeting = Meeting.builder().withSubmission(new Date())
				.withEmployeeId("EMP 001")
				.withStart(sdfMeetingTime.parse("2014-11-01 16:00"))
				.withDuration(2).withOfficeStart("0800").withOfficeEnd("1700")
				.build();

		Meeting meeting2 = Meeting.builder().withSubmission(new Date())
				.withEmployeeId("EMP 002")
				.withStart(sdfMeetingTime.parse("2014-11-01 17:00"))
				.withDuration(1).withOfficeStart("0800").withOfficeEnd("1700")
				.build();

		List<Meeting> meetingList = new ArrayList<Meeting>();
		meetingList.add(meeting2);

		Boolean ret = meeting.overlapAny(meetingList);

		Assert.assertTrue(ret);

	}

	@Test(expected = IllegalArgumentException.class)
	public void whenMeetingHasInvalidDuration() throws ParseException {

		Meeting.builder().withSubmission(new Date())
				.withEmployeeId("EMP 001")
				.withStart(sdfMeetingTime.parse("2014-11-01 16:00"))
				.withDuration(-2).withOfficeStart("0800").withOfficeEnd("1700")
				.build();

	}

}
