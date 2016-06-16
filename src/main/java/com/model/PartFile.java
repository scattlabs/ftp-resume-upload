/**
 * 
 */
package com.model;

import java.io.InputStream;

/**
 * @author ScattLabs
 *
 */
public class PartFile {
	private int id;
	String fileName;
	long filSize;
	InputStream stream;

	/**
	 * 
	 */
	public PartFile() {
		// TODO Auto-generated constructor stub
	}

	public PartFile(int id, String fileName, long filSize, InputStream stream) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.filSize = filSize;
		this.stream = stream;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getFilSize() {
		return filSize;
	}

	public void setFilSize(long filSize) {
		this.filSize = filSize;
	}

	public InputStream getStream() {
		return stream;
	}

	public void setStream(InputStream stream) {
		this.stream = stream;
	}

}
