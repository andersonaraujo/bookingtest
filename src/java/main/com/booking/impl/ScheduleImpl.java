package com.booking.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.booking.Schedule;

@Service
public class ScheduleImpl implements Schedule {

	private Map<String, List<Meeting>> meetingsByDay;
	private SimpleDateFormat sdfMeetingDay = new SimpleDateFormat("yyyy-MM-dd");

	public ScheduleImpl() {
		// Keep meetings in a map, where the key is the day. This will speed-up
		// verification time of overlap, because will iterate only in the
		// meetings that happens on the same day.
		meetingsByDay = new HashMap<String, List<Meeting>>();
	}

	@Override
	public Boolean bookMeeting(Meeting currentMeeting) {

		// Confirm the meeting doesn't fall out of office hours
		if (currentMeeting.isInOfficeHours()) {

			String mapKey = sdfMeetingDay.format(currentMeeting.getStartTime());
			List<Meeting> meetingList = meetingsByDay.get(mapKey);

			if (meetingList == null || meetingList.isEmpty()) {
				// In this point still don't have any meeting in the same day.
				// No need to check overlap.
				meetingList = new ArrayList<Meeting>();
				meetingList.add(currentMeeting);
				meetingsByDay.put(mapKey, meetingList);
				return Boolean.TRUE;

			} else {
				// In this point there are meetings in the same day. Need to
				// check overlap.
				if (!currentMeeting.overlapAny(meetingList)) {
					meetingList.add(currentMeeting);
					return Boolean.TRUE;
				}
			}
		}

		return Boolean.FALSE;
	}

	@Override
	public String printSchedule() {
		// Build response
		StringBuilder sb = new StringBuilder();

		List<String> keyList = new ArrayList<String>(meetingsByDay.keySet());
		Collections.sort(keyList);
		Iterator<String> keyIterator = keyList.iterator();
		while (keyIterator.hasNext()) {
			String key = keyIterator.next();
			sb.append(key);
			sb.append("\n");

			List<Meeting> meetingList = meetingsByDay.get(key);
			// Sort by start date/time
			Collections.sort(meetingList, new MeetingStartComparator());
			for (Meeting currentMeeting : meetingList) {
				sb.append(currentMeeting.toString());
				sb.append("\n");
			}

		}

		return sb.toString();
	}
}

/**
 * Performs comparation of Start date.
 *
 */
class MeetingStartComparator implements Comparator<Meeting> {

	@Override
	public int compare(Meeting obj1, Meeting obj2) {
		return obj1.getStartTime().compareTo(obj2.getStartTime());
	}

}
