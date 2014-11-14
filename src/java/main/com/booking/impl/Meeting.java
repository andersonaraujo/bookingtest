package com.booking.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Meeting {

	private Date submission;
	private String employeeId;
	private Date startTime;
	private Date endTime;
	private Date officeStart;
	private Date officeEnd;
	@SuppressWarnings("unused")
	private Integer durationHours;
	private SimpleDateFormat sdfMeetingHourMin = new SimpleDateFormat("HH:mm");

	private Meeting(Date submission, String employeeId, Date startTime,
			Integer durationHours, String officeStart, String officeEnd) {

		if (durationHours <= 0)
			throw new IllegalArgumentException("Invalid duration => "
					+ durationHours);

		this.submission = submission;
		this.employeeId = employeeId;
		this.startTime = startTime;
		this.durationHours = durationHours;

		Calendar endTimeCal = Calendar.getInstance();
		endTimeCal.setTime(startTime);
		endTimeCal.add(Calendar.HOUR_OF_DAY, durationHours);
		this.endTime = endTimeCal.getTime();

		Calendar officeStartCal = Calendar.getInstance();
		officeStartCal.setTime(this.startTime);
		officeStartCal.set(Calendar.HOUR_OF_DAY,
				Integer.parseInt(officeStart.substring(0, 2)));
		officeStartCal.set(Calendar.MINUTE,
				Integer.parseInt(officeStart.substring(2, 4)));
		officeStartCal.set(Calendar.SECOND, 0);
		officeStartCal.set(Calendar.MILLISECOND, 0);
		this.officeStart = officeStartCal.getTime();

		Calendar officeEndCal = Calendar.getInstance();
		officeEndCal.setTime(this.startTime);
		officeEndCal.set(Calendar.HOUR_OF_DAY,
				Integer.parseInt(officeEnd.substring(0, 2)));
		officeEndCal.set(Calendar.MINUTE,
				Integer.parseInt(officeEnd.substring(2, 4)));
		officeEndCal.set(Calendar.SECOND, 0);
		officeEndCal.set(Calendar.MILLISECOND, 0);
		this.officeEnd = officeEndCal.getTime();
	}

	public Boolean isInOfficeHours() {
		return ((this.startTime.after(this.officeStart) || this.startTime
				.equals(this.officeStart)) && (this.endTime
				.before(this.officeEnd) || this.endTime.equals(this.officeEnd)));

	}

	public Date getsubmission() {
		return this.submission;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	private Date getEndTime() {
		return this.endTime;
	}

	@Override
	public String toString() {
		return sdfMeetingHourMin.format(startTime) + " "
				+ sdfMeetingHourMin.format(endTime) + " " + employeeId;
	}

	public boolean overlapAny(List<Meeting> meetingList) {
		if (meetingList == null | meetingList.isEmpty())
			return Boolean.FALSE;

		for (Meeting meeting : meetingList) {
			if (this.startTime.before(meeting.getEndTime())
					&& this.endTime.after(meeting.getStartTime())) {
				return Boolean.TRUE;
			}
		}

		return Boolean.FALSE;
	}

	public static class Builder {

		private Date submission;
		private String employeeId;
		private Date startTime;
		private Integer durationHours;
		private String officeStart;
		private String officeEnd;

		public Builder withSubmission(final Date submission) {
			this.submission = submission;
			return this;
		}

		public Builder withEmployeeId(final String employeeId) {
			this.employeeId = employeeId;
			return this;
		}

		public Builder withStart(final Date startTime) {
			this.startTime = startTime;
			return this;
		}

		public Builder withDuration(final Integer durationHours) {
			this.durationHours = durationHours;
			return this;
		}

		public Builder withOfficeStart(final String officeStart) {
			this.officeStart = officeStart;
			return this;
		}

		public Builder withOfficeEnd(final String officeEnd) {
			this.officeEnd = officeEnd;
			return this;
		}

		public Meeting build() {
			return new Meeting(submission, employeeId, startTime,
					durationHours, officeStart, officeEnd);

		}
	}

	public static Builder builder() {
		return new Builder();
	}

}
