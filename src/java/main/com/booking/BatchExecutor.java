package com.booking;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BatchExecutor {

	@Autowired
	private BookingProcessor processor;

	public BatchExecutor() {

	}

	public BatchExecutor(BookingProcessor processor) {
		this.processor = processor;
	}

	/**
	 * Reads the input data from a File and call the processor to process data.
	 * 
	 * @param file
	 *            File to read data.
	 * @return Processor's return
	 */
	public String execute(File file) {

		List<String> inputLines = new ArrayList<String>();

		// Read lines from File
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				inputLines.add(line);
			}
			br.close();

		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(e);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}

		// Call processor
		String ret = processor.process(inputLines);

		return ret;
	}

}
