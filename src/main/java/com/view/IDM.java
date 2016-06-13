/**
 * 
 */
package com.view;

import java.awt.EventQueue;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import com.config.DataHolder;
import com.controller.UploadController;
import com.controller.UploadThread;
import com.dao.UploadDao2;
import com.model.Upload2;

import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.event.ActionEvent;

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

	private JFrame frame;
	private Upload2 upload2;
	private UploadDao2 uploadDao2;

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
					window.frame.setVisible(true);
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

	// ----------------END SETTER AND GETTER----------------------//
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 423);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(10, 11, 414, 165);
		frame.getContentPane().add(panel);
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

		progressBar = new JProgressBar();
		progressBar.setBounds(10, 187, 414, 14);
		frame.getContentPane().add(progressBar);
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
		progressBar.setValue(0);
		Upload2 upload2 = UploadController.getInstance().checkFileUpload(this, file);
		int status = 2;
		if (upload2 == null) {
			status = 1;
			upload2 = new Upload2(0, file.getName(), (int) file.length(), 0, 0);
		}
		UploadThread thread = new UploadThread(upload2, getUploadDao2(), file, status, this);
		// thread.addPropertyChangeListener(this);
		thread.execute();
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

	public void updateProgress(long sizeFileUpload, int percent) {
		getProgressBar().setValue(percent);
		getLblSizeUpload().setText(sizeFileUpload + " Byte");
		getLblOutputFileDownload().setText(percent + " %");
	}

}
