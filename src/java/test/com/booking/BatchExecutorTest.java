package com.booking;

import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BatchExecutorTest {

	private BatchExecutor batchExecutor;

	@Mock
	private BookingProcessor processor;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		batchExecutor = new BatchExecutor(processor);
	}

	@Test
	public void whenProcessEmptyFile() {
		List<String> inputList = new ArrayList<String>();
		when(processor.process(inputList)).thenReturn("EMPTY SCHEDULE");

		File file = this.createEmptyTempFile();
		String ret = batchExecutor.execute(file);

		Assert.assertEquals("EMPTY SCHEDULE", ret);

		verify(processor, atMost(1)).process(inputList);
	}

	@Test
	public void whenProcessFilledFile() {
		List<String> inputList = new ArrayList<String>();
		inputList.add("0900 1730");
		inputList.add("2011-03-17 10:17:06 EMP001");
		inputList.add("2011-03-21 09:00 2");

		when(processor.process(inputList)).thenReturn("NEW SCHEDULE");

		File file = this.createEmptyTempFile();
		this.writeToFile(file, inputList);

		String ret = batchExecutor.execute(file);

		Assert.assertEquals("NEW SCHEDULE", ret);

		verify(processor, atMost(1)).process(inputList);
	}

	@Test(expected = NullPointerException.class)
	public void whenProcessNullFile() {

		batchExecutor.execute(null);

	}

	/**
	 * Create temp empty File for testing.
	 * 
	 * @return File
	 */
	private File createEmptyTempFile() {
		try {
			// Create temp file.
			File temp = File.createTempFile("testBatchExecutor", ".txt");

			// Delete temp file when program exits.
			temp.deleteOnExit();

			return temp;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Write test data to File.
	 * 
	 * @return File
	 */
	private void writeToFile(File file, List<String> list) {
		try {

			// Write to temp file
			BufferedWriter out = new BufferedWriter(new FileWriter(file));

			for (String s : list) {
				out.write(s);
				out.newLine();
			}

			out.close();

		} catch (IOException e) {
			e.printStackTrace();

		}
	}

}
