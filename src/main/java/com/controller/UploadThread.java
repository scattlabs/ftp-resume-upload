/**
 * 
 */
package com.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.dao.UploadDao2;
import com.model.PartFile;
import com.model.Upload2;
import com.view.IDM;

/**
 * @author ScattLabs
 *
 */
public class UploadThread extends SwingWorker<Void, Void> {

	static final Logger LOGGER = Logger.getLogger(UploadThread.class.getName());

	private static final int BUFFER_SIZE = 1024000;// 4096
	private static final int PERCENT = 100;// 4096

	private String host = "172.18.2.75";
	private String username = "updown";
	private String password = "admin";

	private Upload2 upload2;
	private int status;
	private IDM idm;
	FTPClient client;
	private File fileUpload;

	public UploadThread(Upload2 upload2, File fileUpload, int status, IDM idm) {
		this.upload2 = upload2;
		this.status = status;
		this.setIdm(idm);
		this.fileUpload = fileUpload;
	}

	// -------------BEGIN SETTER AND GETTER-----------------------//
	public Upload2 getUpload2() {
		return upload2;
	}

	public void setUpload2(Upload2 upload2) {
		this.upload2 = upload2;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public IDM getIdm() {
		return idm;
	}

	public void setIdm(IDM idm) {
		this.idm = idm;
	}

	// -------------END SETTER AND GETTER-----------------------//
	/**
	 * Executed in background thread
	 */
	@Override
	protected Void doInBackground() throws Exception {
		client = new FTPClient();
		try {
			client.connect(host);
			client.login(username, password);
			client.setFileType(FTPClient.BINARY_FILE_TYPE);
			client.enterLocalPassiveMode();
			// InputStream inputStream = partFile.getStream();
			FileInputStream inputStream = new FileInputStream(fileUpload);
			InputStream inputStreamSend;
			try {
				byte[] bytesIn = new byte[BUFFER_SIZE];
				int read = 0;
				long upTotal = 0;
				long filesize = fileUpload.length();
				System.out.println("filesize : " + filesize);
				System.out.println("status : " + getStatus());
				long complate = 0;
				while (upTotal < filesize) {
					if (filesize < BUFFER_SIZE) {
						bytesIn = new byte[(int) filesize];
					} else if ((upTotal + BUFFER_SIZE) > filesize) {
						bytesIn = new byte[(int) (filesize - upTotal)]; // sisa
					}
					if ((read = inputStream.read(bytesIn)) != -1) {
						upTotal += read;
						switch (getStatus()) {
						case 1:
							inputStreamSend = new ByteArrayInputStream(bytesIn);
							client.appendFile(fileUpload.getName(), inputStreamSend);
							break;

						case 2:
							long lastUpload = getLastUpload();
							long partially = lastUpload % BUFFER_SIZE;
							long terminal = lastUpload - partially;
							if (upTotal == terminal) {
								// yang sudah dikirim sebelumnya
								bytesIn = new byte[(int) partially];
								read = inputStream.read(bytesIn);
								upTotal += read;
								// yang belum dikirim
								bytesIn = new byte[BUFFER_SIZE - (int) partially];
								read = inputStream.read(bytesIn);
								upTotal += read;
								System.out.println("masuk pada byte : " + upTotal);
								inputStreamSend = new ByteArrayInputStream(bytesIn);
								client.appendFile(fileUpload.getName(), inputStreamSend);
								setStatus(1); // normal kembali
							}
							break;
						}
						complate = (upTotal * PERCENT) / filesize;
						idm.updateProgress(upTotal, (int) complate, 1);
						// setProgress((int) complate);
						System.out.println(client.getReplyString());
					}
				}
			} catch (FileNotFoundException e1) {
				LOGGER.warning(e1.getMessage());
				// TODO Auto-generated catch block
			} catch (IOException e) {
				LOGGER.warning(e.getMessage());
				// TODO Auto-generated catch block
			}
		} catch (

		Exception e) {
			LOGGER.warning(e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Executed in Swing's event dispatching thread
	 */
	@Override
	protected void done() {
		if (!isCancelled()) {
			JOptionPane.showMessageDialog(null, "File has been uploaded successfully!", "Message",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	// get file in ftp check size
	public long getLastUpload() {
		FTPFile file;
		long size = 0;
		try {
			if (client.mlistFile(fileUpload.getName()) != null) {
				file = client.mlistFile(fileUpload.getName());
				size = file.getSize();
				System.out.println(size);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return size;
	}
}
