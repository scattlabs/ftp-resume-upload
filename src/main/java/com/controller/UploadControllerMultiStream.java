/**
 * 
 */
package com.controller;

import java.io.File;
import java.util.List;

import com.config.DataHolder;
import com.dao.UploadDao2;
import com.model.Upload2;
import com.view.IDMMultiStream;

/**
 * @author ScattLabs
 *
 */
public class UploadControllerMultiStream {

	static UploadControllerMultiStream uc;

	private UploadDao2 uploadDao2;
	private Upload2 upload2;

	public static UploadControllerMultiStream getInstance() {
		if (uc == null) {
			uc = new UploadControllerMultiStream();
			uc.setUploadDao2(DataHolder.getInstance().getUploadDao2());
		}
		return uc;
	}

	// -------------BEGIN SETTER AND GETTER--------------//
	public UploadDao2 getUploadDao2() {
		return uploadDao2;
	}

	public void setUploadDao2(UploadDao2 uploadDao2) {
		this.uploadDao2 = uploadDao2;
	}

	public Upload2 getUpload2() {
		return upload2;
	}

	public void setUpload2(Upload2 upload2) {
		this.upload2 = upload2;
	}

	// -------------END SETTER AND GETTER--------------//
	public Upload2 checkFileUpload(IDMMultiStream idm, File file) {
		List<Upload2> upload2s = uploadDao2.list("");
		for (Upload2 upload2 : upload2s) {
			if (upload2.getFileName().equals(file.getName()) && upload2.getFileSize() == (int) file.length()
					&& upload2.getUploadStatus() == 0) {
				// MELANJUTKAN
				setUpload2(upload2);
				break;
			}
			/*
			 * else if (upload2.getFileName().equals(file.getName()) &&
			 * upload2.getFileSize() == (int) file.length() &&
			 * upload2.getUploadStatus() == 1) {
			 * System.out.println(FilenameUtils.getName(file.getName()));
			 * setUpload2(new Upload2(0,
			 * FilenameUtils.removeExtension(file.getName()) + "1.txt", (int)
			 * file.length(), 0, 0, "")); }
			 */
		}
		return getUpload2();
	}
}
