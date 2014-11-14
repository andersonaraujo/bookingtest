package com.booking.controler;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.booking.BatchExecutor;

@Controller
public class FileUploadController {

	@Autowired
	BatchExecutor batchExecutor;

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public @ResponseBody String provideUploadInfo() {
		return "You can upload a booking file by posting to this same URL.";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String handleFileUpload(
			@RequestParam("file") MultipartFile muultipartFile) throws IOException {

		// Create temp file.
		File file = File.createTempFile("booking", ".txt");

		// Delete temp file when program exits.
		file.deleteOnExit();

		muultipartFile.transferTo(file);

		String ret = batchExecutor.execute(file);

		return ret;

	}

}
