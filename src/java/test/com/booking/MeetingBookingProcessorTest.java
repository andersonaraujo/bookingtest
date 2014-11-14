package com.booking;

import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.booking.impl.BookingProcessorImpl;

@RunWith(MockitoJUnitRunner.class)
public class MeetingBookingProcessorTest {

	private BookingProcessor meetingBookingProcessor;

	@Mock
	private Schedule schedule;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		meetingBookingProcessor = new BookingProcessorImpl(schedule);
	}

	@Test
	public void whenProcessNullList() {
		String ret = meetingBookingProcessor.process(null);
		Assert.assertNull(ret);
	}

	@Test
	public void whenProcessEmptyist() {
		String ret = meetingBookingProcessor.process(new ArrayList<String>());
		Assert.assertNull(ret);
	}

	@Test
	public void whenParseValidOfficeHours() {
		List<String> inputList = new ArrayList<String>();
		inputList.add("0900 1730");

		meetingBookingProcessor.process(inputList);

	}

	@Test(expected = IllegalArgumentException.class)
	public void whenParseInvalidOfficeHours() {
		List<String> inputList = new ArrayList<String>();
		inputList.add("0900");

		meetingBookingProcessor.process(inputList);
	}

	@Test
	public void whenParseValidSubmissionTime() {
		List<String> inputList = new ArrayList<String>();
		inputList.add("0900 1730");
		inputList.add("2011-03-17 10:17:06 EMP001");
		inputList.add("2011-03-21 09:00 2");

		meetingBookingProcessor.process(inputList);
	}

	@Test(expected = IllegalArgumentException.class)
	public void whenParseInvalidSubmissionTime() {
		List<String> inputList = new ArrayList<String>();
		inputList.add("0900 1730");
		inputList.add("2011-03-17 44:  :99 EMP002");

		meetingBookingProcessor.process(inputList);
	}

	@Test
	public void whenParseValidMeetingTime() {
		List<String> inputList = new ArrayList<String>();
		inputList.add("0900 1730");
		inputList.add("2011-03-17 10:17:06 EMP001");
		inputList.add("2011-03-21 09:00 2");

		meetingBookingProcessor.process(inputList);
	}

	@Test(expected = IllegalArgumentException.class)
	public void whenParseInvalidMeetingTime() {
		List<String> inputList = new ArrayList<String>();
		inputList.add("0900 1730");
		inputList.add("2011-03-17 10:17:06 EMP001");
		inputList.add("2011-03-21 09:   2");

		meetingBookingProcessor.process(inputList);
	}

	@Test
	public void whenValidMeetingList() {

		String correctRerturn = "2011-03-21\n" + "09:00 11:00 EMP001\n"
				+ "2011-03-22\n" + "14:00 16:00 EMP003\n";

		when(schedule.printSchedule()).thenReturn(correctRerturn);

		List<String> inputList = new ArrayList<String>();
		inputList.add("0900 1730");
		inputList.add("2011-03-17 10:17:06 EMP001");
		inputList.add("2011-03-21 09:00 2");
		inputList.add("2011-03-16 09:28:23 EMP003");
		inputList.add("2011-03-22 14:00 2");

		String ret = meetingBookingProcessor.process(inputList);

		Assert.assertEquals(correctRerturn, ret);

		verify(schedule, atMost(1)).printSchedule();
	}

	@Test
	public void whenValidMeetingListOverlap() {
		String correctRerturn = "2011-03-21\n" + "09:00 12:00 EMP001\n";

		when(schedule.printSchedule()).thenReturn(correctRerturn);

		List<String> inputList = new ArrayList<String>();
		inputList.add("0900 1730");
		inputList.add("2011-03-16 10:17:06 EMP001");
		inputList.add("2011-03-21 09:00 3");
		inputList.add("2011-03-17 09:28:23 EMP002");
		inputList.add("2011-03-21 10:00 3");

		String ret = meetingBookingProcessor.process(inputList);

		Assert.assertEquals(correctRerturn, ret);

		verify(schedule, atMost(1)).printSchedule();
	}

}
