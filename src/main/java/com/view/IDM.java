/**
 * 
 */
package com.view;

import java.awt.EventQueue;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import org.apache.commons.io.FilenameUtils;

import com.config.DataHolder;
import com.controller.FileUploadController;
import com.controller.UploadController;
import com.controller.UploadThread;
import com.dao.UploadDao2;
import com.model.PartFile;
import com.model.Upload2;

import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

/**
 * @author ScattLabs
 *
 */
public class IDM implements PropertyChangeListener {

	private File file;
	JLabel lblOutputFileName;
	JLabel lblOutputFileSize;
	JLabel lblOutputFileDownload;
	JProgressBar progressBar;
	JLabel lblSizeUpload;

	private JFrame frmIupInternetUpload;
	private Upload2 upload2;
	private UploadDao2 uploadDao2;
	private String logFileName = "";
	private JProgressBar progressBar_1;
	private JProgressBar progressBar_2;
	private JProgressBar progressBar_3;
	private JProgressBar progressBar_4;
	private JLabel lblSizeupload_1;
	private JLabel lblSizeupload_2;
	private JLabel lblSizeupload_3;
	private JLabel lblSizeupload_4;
	private static long total = 0;
	long p1 = 0;
	long p2 = 0;
	long p3 = 0;
	long p4 = 0;
	JLabel lblStatus;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		DataHolder.getInstance().execute();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				String lookAndFeel = javax.swing.UIManager.getSystemLookAndFeelClassName();
				try {
					javax.swing.UIManager.setLookAndFeel(lookAndFeel);
					IDM window = new IDM();
					window.frmIupInternetUpload.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public IDM() {
		initialize();
		uploadDao2 = DataHolder.getInstance().getUploadDao2();
	}

	// ----------------BEGIN SETTER AND GETTER----------------------//
	public Upload2 getUpload2() {
		return upload2;
	}

	public void setUpload2(Upload2 upload2) {
		this.upload2 = upload2;
	}

	public JLabel getLblSizeUpload() {
		return lblSizeUpload;
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public UploadDao2 getUploadDao2() {
		return uploadDao2;
	}

	public void setUploadDao2(UploadDao2 uploadDao2) {
		this.uploadDao2 = uploadDao2;
	}

	public JLabel getLblOutputFileDownload() {
		return lblOutputFileDownload;
	}

	public void setLblOutputFileDownload(JLabel lblOutputFileDownload) {
		this.lblOutputFileDownload = lblOutputFileDownload;
	}

	public String getLogFileName() {
		return logFileName;
	}

	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}

	// ----------------END SETTER AND GETTER----------------------//
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmIupInternetUpload = new JFrame();
		frmIupInternetUpload.setTitle("INTERNET UPLOAD MANAGER");
		frmIupInternetUpload.setBounds(100, 100, 450, 423);
		frmIupInternetUpload.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmIupInternetUpload.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(10, 11, 414, 165);
		frmIupInternetUpload.getContentPane().add(panel);
		panel.setLayout(null);

		JButton btnSelectFile = new JButton("Select File");
		btnSelectFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnSelectedFileActionPerformed(evt);
			}
		});
		btnSelectFile.setBounds(10, 11, 89, 23);
		panel.add(btnSelectFile);

		lblOutputFileName = new JLabel("File Name");
		lblOutputFileName.setBounds(10, 45, 394, 14);
		panel.add(lblOutputFileName);

		JLabel lblFilesize = new JLabel("File Size");
		lblFilesize.setBounds(10, 70, 46, 14);
		panel.add(lblFilesize);

		lblOutputFileSize = new JLabel("filesize");
		lblOutputFileSize.setBounds(119, 70, 285, 14);
		panel.add(lblOutputFileSize);

		JLabel lblDownload = new JLabel("Upload");
		lblDownload.setBounds(10, 95, 46, 14);
		panel.add(lblDownload);

		lblOutputFileDownload = new JLabel("download");
		lblOutputFileDownload.setBounds(232, 95, 172, 14);
		panel.add(lblOutputFileDownload);

		JButton btnUpload = new JButton("Upload");
		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonUploadActionPerformed(e);
			}
		});
		btnUpload.setBounds(10, 131, 89, 23);
		panel.add(btnUpload);

		lblSizeUpload = new JLabel("size upload");
		lblSizeUpload.setBounds(119, 95, 103, 14);
		panel.add(lblSizeUpload);

		lblStatus = new JLabel("");
		lblStatus.setForeground(Color.RED);
		lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblStatus.setBounds(119, 135, 285, 14);
		panel.add(lblStatus);

		progressBar = new JProgressBar();
		progressBar.setBounds(10, 187, 414, 14);
		frmIupInternetUpload.getContentPane().add(progressBar);

		progressBar_1 = new JProgressBar();
		progressBar_1.setBounds(10, 226, 414, 14);
		frmIupInternetUpload.getContentPane().add(progressBar_1);

		progressBar_2 = new JProgressBar();
		progressBar_2.setBounds(10, 251, 414, 14);
		frmIupInternetUpload.getContentPane().add(progressBar_2);

		progressBar_3 = new JProgressBar();
		progressBar_3.setBounds(10, 276, 414, 14);
		frmIupInternetUpload.getContentPane().add(progressBar_3);

		progressBar_4 = new JProgressBar();
		progressBar_4.setBounds(10, 301, 414, 14);
		frmIupInternetUpload.getContentPane().add(progressBar_4);

		lblSizeupload_1 = new JLabel("sizeUpload");
		lblSizeupload_1.setBounds(10, 212, 117, 14);
		frmIupInternetUpload.getContentPane().add(lblSizeupload_1);

		lblSizeupload_2 = new JLabel("sizeUpload");
		lblSizeupload_2.setBounds(10, 237, 117, 14);
		frmIupInternetUpload.getContentPane().add(lblSizeupload_2);

		lblSizeupload_3 = new JLabel("sizeUpload");
		lblSizeupload_3.setBounds(10, 262, 117, 14);
		frmIupInternetUpload.getContentPane().add(lblSizeupload_3);

		lblSizeupload_4 = new JLabel("sizeUpload");
		lblSizeupload_4.setBounds(10, 287, 117, 14);
		frmIupInternetUpload.getContentPane().add(lblSizeupload_4);
		setUploadDao2(DataHolder.getInstance().getUploadDao2());
	}

	private void btnSelectedFileActionPerformed(java.awt.event.ActionEvent evt) {
		JFileChooser fc = new JFileChooser();
		int res = fc.showOpenDialog(null);
		try {
			if (res == JFileChooser.APPROVE_OPTION) {
				file = fc.getSelectedFile();
				lblOutputFileName.setText(file.getAbsolutePath());
				lblOutputFileSize.setText((file.length()) + " Byte");
			} else {
				JOptionPane.showMessageDialog(null, "Pilih Salah Satu File", "Warning...", JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception iOException) {
			System.out.println(iOException.getMessage());
		}

	}

	private void buttonUploadActionPerformed(ActionEvent event) {
		setLogFileName(FilenameUtils.removeExtension(file.getName()) + ".txt");
		progressBar.setValue(0);
		setUpload2(UploadController.getInstance().checkFileUpload(this, file));
		int status = 2;
		if (getUpload2() == null) {
			status = 1;
			setUpload2(new Upload2(0, file.getName(), (int) file.length(), 0, 0,
					FileUploadController.getInstance().createFileLogUpload(getLogFileName())));
			// uploadDao2.save(upload2);
		}
		lblStatus.setText(status == 1 ? "New Upload" : "Resume Upload");
		List<PartFile> partFiles = splitFile();

		for (PartFile partFile : partFiles) {
			UploadThread thread = new UploadThread(upload2, partFile, status, this);
			thread.execute();
		}
	}

	public List<PartFile> splitFile() {
		long fileSize = file.length();
		int streamCurrent = 1, read = 0;
		long stream = 4;
		long mod = 0;
		byte[] bytePart;
		int byteLength = 0;
		List<PartFile> partFiles = new ArrayList<>();
		if (fileSize <= 4000000000000L) {
			mod = fileSize % 4;
			if (mod == 0) {
				byteLength = (int) (fileSize / stream);
			} else {
				fileSize -= mod;
				byteLength = (int) (fileSize / stream);
			}
		} else {
			bytePart = new byte[0];
		}
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			InputStream inputStreamSend = null;
			PartFile partFile = new PartFile();
			while (fileSize > 0) {
				bytePart = new byte[byteLength];
				if (streamCurrent == stream) {
					bytePart = new byte[(int) (byteLength + mod)];
				}
				read = fileInputStream.read(bytePart);
				fileSize -= read;
				assert (read == bytePart.length);
				inputStreamSend = new ByteArrayInputStream(bytePart);
				partFile = new PartFile(streamCurrent, file.getName() + ".part" + streamCurrent, bytePart.length,
						inputStreamSend);
				partFiles.add(partFile);
				streamCurrent++;
			}
			fileInputStream.close();
		} catch (IOException exception) {
			System.out.println("ex :" + exception.getMessage());
			exception.printStackTrace();
		}
		return partFiles;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			System.out.println(progress);
			progressBar.setValue(progress);
			getLblOutputFileDownload().setText(progress + " %");
		}
	}

	public void updateProgress(long sizeFileUpload, int percent, int id) {
		switch (id) {
		case 1:
			progressBar_1.setValue(percent);
			lblSizeupload_1.setText(sizeFileUpload + " Byte");
			p1 = sizeFileUpload;
			calculate();
			break;
		case 2:
			progressBar_2.setValue(percent);
			lblSizeupload_2.setText(sizeFileUpload + " Byte");
			p2 = sizeFileUpload;
			calculate();
			break;
		case 3:
			progressBar_3.setValue(percent);
			lblSizeupload_3.setText(sizeFileUpload + " Byte");
			p3 = sizeFileUpload;
			calculate();
			break;
		case 4:
			progressBar_4.setValue(percent);
			lblSizeupload_4.setText(sizeFileUpload + " Byte");
			p4 = sizeFileUpload;
			calculate();
			break;
		}
	}

	public void calculate() {
		total = 0;
		total = p1 + p2 + p3 + p4;
		lblSizeUpload.setText(total + " Byte");
		int progressCore = (int) ((total * 100) / file.length());
		progressBar.setValue(progressCore);
		lblOutputFileDownload.setText("( " + progressCore + " % )");
		if (progressCore == 100) {
			getUpload2().setUploadStatus(1);
			uploadDao2.save(upload2);
		}
	}
}
