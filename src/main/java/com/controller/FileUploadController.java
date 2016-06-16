/**
 * 
 */
package com.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * @author ScattLabs
 *
 */
public class FileUploadController {

	static FileUploadController fuc;

	public static FileUploadController getInstance() {
		if (fuc == null) {
			fuc = new FileUploadController();
		}
		return fuc;
	}

	public boolean createFolderUpload(String updownFolder) {
		boolean createFolder = false;
		File file = new File(updownFolder);
		try {
			if (!file.exists()) {
				if (file.mkdirs()) {
					return true;
				} else {
					return false;
				}
			} else {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return createFolder;
	}

	public String createFileLogUpload(String fileName) {
		String homePath = System.getProperty("user.home");
		String updownFolder = homePath + "\\soki\\updown\\";
		File file = new File(updownFolder + "\\" + fileName);

		try {
			if (createFolderUpload(updownFolder)) {
				if (file.createNewFile()) {
					System.out.println("File is created!");
					Properties prop = new Properties();
					OutputStream output = null;
					try {
						output = new FileOutputStream(file);
						// set the properties value
						prop.setProperty("part8", "0");
						prop.setProperty("part7", "0");
						prop.setProperty("part6", "0");
						prop.setProperty("part5", "0");
						prop.setProperty("part4", "0");
						prop.setProperty("part3", "0");
						prop.setProperty("part2", "0");
						prop.setProperty("part1", "0");
						// save properties to project root folder
						prop.store(output, null);
					} catch (IOException io) {
						io.printStackTrace();
					} finally {
						if (output != null) {
							try {
								output.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				} else {
					System.out.println("File already exists.");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}

	public long getLastSizePartUpload(String fileName, String stringPart) {
		String homePath = System.getProperty("user.home");
		String updownFolder = homePath + "\\soki\\updown\\";
		File file = new File(updownFolder + "\\" + fileName);
		long size = 0;
		InputStream input = null;
		Properties prop = new Properties();
		try {
			input = new FileInputStream(file);
			// load a properties file
			prop.load(input);

			// get the property value and print it out
			size = Long.parseLong(prop.getProperty(stringPart));
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return size;
	}

	public void setLastSizePartUpload(String fileName, String stringPart, long size) {
		Properties prop = new Properties();
		OutputStream output = null;
		FileInputStream in = null;
		try {
			in = new FileInputStream(fileName);
			prop.load(in);
			in.close();

			output = new FileOutputStream(fileName);
			// set the properties value
			prop.setProperty(stringPart, size + "");
			// save properties to project root folder
			prop.store(output, null);
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
