package com.booking.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.booking.Schedule;
import com.booking.BookingProcessor;

@Component
public class BookingProcessorImpl implements BookingProcessor {

	@Autowired
	private Schedule schedule;

	private SimpleDateFormat sdfSubmissionDate = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdfMeetingTime = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	public BookingProcessorImpl() {
	}

	public BookingProcessorImpl(Schedule schedule) {
		this.schedule = schedule;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.booking.Processor#process(java.util.List)
	 */
	@Override
	public String process(List<String> input) {

		// if inputLIst is null, no meetings to schedule
		if (input == null || input.isEmpty())
			return null;

		List<Meeting> meetingInputList = new ArrayList<Meeting>();

		// Parses input List in RequestMeetings
		try {
			Iterator<String> iterator = input.iterator();

			String firstLine = iterator.next();
			// The first line of the input text represents the company
			// office hours, in 24 hour clock format
			String officeStartTime = firstLine.substring(0, 4);
			String officeEndTime = firstLine.substring(5, 9);

			while (iterator.hasNext()) {

				String line = iterator.next();

				// [request submission time, in the format YYYY-MM-DD HH:MM:SS]
				// [ARCH:employee id]
				String reqSubmission = line.substring(0, 19);
				Date reqSubmissionDate = sdfSubmissionDate.parse(reqSubmission);
				String employeeId = line.substring(20);

				String secondLine = iterator.next();

				// [meeting start time, in the format YYYY-MM-DD HH:MM]
				// [ARCH:meeting duration in hours]
				String meetingStart = secondLine.substring(0, 16);
				Date meetingStartDate = sdfMeetingTime.parse(meetingStart);
				String meetingDuration = secondLine.substring(17);
				Integer meetingDurationInt = Integer.parseInt(meetingDuration);

				Meeting meeting = Meeting.builder()
						.withSubmission(reqSubmissionDate)
						.withEmployeeId(employeeId).withStart(meetingStartDate)
						.withDuration(meetingDurationInt)
						.withOfficeStart(officeStartTime)
						.withOfficeEnd(officeEndTime).build();

				meetingInputList.add(meeting);
			}

		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}

		// Sort meeting requests by the submission date (chronological order)
		Collections.sort(meetingInputList, new MeetingSubmissionCompartor());

		// Starts booking meetings
		for (Meeting currentMeeting : meetingInputList) {

			this.schedule.bookMeeting(currentMeeting);

		}

		// At this point, meetings are booked!

		// Prints out Schedule
		return this.schedule.printSchedule();

	}

	/**
	 * Performs comparation of Submission date.
	 *
	 */
	class MeetingSubmissionCompartor implements Comparator<Meeting> {

		@Override
		public int compare(Meeting obj1, Meeting obj2) {
			return obj1.getsubmission().compareTo(obj2.getsubmission());
		}

	}

}
