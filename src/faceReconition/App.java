package faceReconition;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.bytedeco.javacv.Java2DFrameUtils;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

public class App extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel labelCamera, labelImg;
	private GridBagConstraints gbc;
	private JButton btnStartCamera, btnCloseCamera, btnTrain, btnTakePhoto, btnRecognizerVideo, btnRecognizerImg,
			btnRecoginerCamera, btnClose;
	private Dimension dimForBtn = new Dimension(80, 80);
	private Dimension dimForLabel = new Dimension(400, 400);
	private FaceDetection faceDetection = new FaceDetection();
	private boolean enableCamera = false, takePhoto = false, recognizerCamera = false;
	private VideoCapture videoCap = new VideoCapture(0);
	private static Mat image = new Mat();
	private FaceRecognizer faceRecognizer = new FaceRecognizer();

	public App() {
		initComponets();

		setVisible(true);
		setSize(900, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	public void initComponets() {
		setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel labelTile = new JLabel("Face recognition");
		labelTile.setHorizontalAlignment(JLabel.CENTER);
		labelTile.setFont(new Font("Arial", Font.BOLD, 36));
		labelTile.setForeground(Color.BLUE);
		addGbcComponent(labelTile, 0, 0, 2, 1);

		labelCamera = new JLabel();
		labelCamera.setIcon(loadIcon("iconApp//labelCamera.png", 400, 400));
		labelCamera.setPreferredSize(dimForLabel);
		labelCamera.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		addGbcComponent(labelCamera, 0, 1, 1, 1);

		labelImg = new JLabel();
		labelImg.setIcon(loadIcon("iconApp//labelImg.png", 400, 400));
		labelImg.setPreferredSize(dimForLabel);
		labelImg.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		addGbcComponent(labelImg, 1, 1, 1, 1);

		JPanel btns = new JPanel();

		btnStartCamera = new JButton(loadIcon("iconApp//play.jpg", 80, 80));
		btnStartCamera.setPreferredSize(dimForBtn);
		btnStartCamera.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				enableCamera = true;
				btnStartCamera.setEnabled(false);
				btnCloseCamera.setEnabled(true);
				startCamera();
			}
		});
		btns.add(btnStartCamera);

		btnCloseCamera = new JButton(loadIcon("iconApp//pause.jpg", 80, 80));
		btnCloseCamera.setPreferredSize(dimForBtn);
		btnCloseCamera.setEnabled(false);
		btnCloseCamera.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				enableCamera = false;
				btnStartCamera.setEnabled(true);
				btnRecoginerCamera.setEnabled(true);
				btnCloseCamera.setEnabled(false);
			}
		});
		btns.add(btnCloseCamera);

		btnTakePhoto = new JButton(loadIcon("iconApp//camera.jpg", 80, 80));
		btnTakePhoto.setPreferredSize(dimForBtn);
		btnTakePhoto.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				takePhoto = true;
				btnTakePhoto.setEnabled(false);
				takePhoto();
			}
		});
		btns.add(btnTakePhoto);

		btnTrain = new JButton(loadIcon("iconApp//train.jpg", 80, 80));
		btnTrain.setPreferredSize(dimForBtn);
		btnTrain.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				train();
			}
		});
		btns.add(btnTrain);

		btnRecognizerImg = new JButton(loadIcon("iconApp//recognizerImg.png", 80, 80));
		btnRecognizerImg.setPreferredSize(dimForBtn);
		btnRecognizerImg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				recognizerImage();
			}
		});
		btns.add(btnRecognizerImg);

		btnRecognizerVideo = new JButton(loadIcon("iconApp//recognizerVideo.png", 80, 80));
		btnRecognizerVideo.setPreferredSize(dimForBtn);
		btns.add(btnRecognizerVideo);

		btnRecoginerCamera = new JButton(loadIcon("iconApp//recognizerCamera.png", 80, 80));
		btnRecoginerCamera.setPreferredSize(dimForBtn);
		btnRecoginerCamera.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enableCamera = true;
				btnRecoginerCamera.setEnabled(false);
				btnCloseCamera.setEnabled(true);
				recognizerCamera();
			}
		});
		btns.add(btnRecoginerCamera);

		btnClose = new JButton(loadIcon("iconApp//close.jpg", 80, 80));
		btnClose.setPreferredSize(dimForBtn);
		btnClose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btns.add(btnClose);

		addGbcComponent(btns, 0, 2, 2, 1);

	}

	public void startCamera() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (enableCamera == false) {
						labelCamera.setIcon(loadIcon("iconApp//labelCamera.png", 400, 400));
						break;
					}
					videoCap.read(image);
					ImageIcon icon = new ImageIcon(Java2DFrameUtils.toBufferedImage(faceDetection.detectFace(image)));
					labelCamera.setIcon(icon);
					labelCamera.repaint();
				}
			}
		}).start();
	}

	public void takePhoto() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String id_ten = "";
				File folderRoot = null;
				if (!enableCamera) {
					takePhoto = false;
					btnTakePhoto.setEnabled(true);
					JOptionPane.showMessageDialog(getContentPane(), "Vui long mo camera");
				} else {
					id_ten = JOptionPane.showInputDialog("Nhap id_HoTen");
					folderRoot = new File("dataset//myDataset//" + id_ten);
				}
				if (id_ten != null && enableCamera) {
					if (!folderRoot.exists())
						folderRoot.mkdir();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					int count = 1;
					while (true) {
						if (count == 21) {
							btnTakePhoto.setEnabled(true);
							takePhoto = false;
						}
						if (takePhoto == false) {
							labelImg.setIcon(loadIcon("iconApp//labelImg.png", 400, 400));
							labelImg.repaint();
							break;
						}
						Mat face = faceDetection.cutFace(image);
						if (face != null) {
							ImageIcon icon = new ImageIcon(Java2DFrameUtils.toBufferedImage(face).getScaledInstance(400,
									400, Image.SCALE_SMOOTH));
							faceDetection.saveImg(face, folderRoot.getAbsolutePath(), count + "");
							labelImg.setIcon(icon);
							labelImg.repaint();
							count++;
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				} else {
					takePhoto = false;
					btnTakePhoto.setEnabled(true);
				}
			}
		}).start();
	}

	public void train() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				btnTrain.setEnabled(false);
				buildModel train = new buildModel();
				boolean finish = train.build("dataset//myDataset", "model");
				if (finish) {
					JOptionPane.showMessageDialog(getContentPane(), "Train finish");
					btnTrain.setEnabled(true);
				}
			}

		}).start();
	}

	public void recognizerImage() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter imgExt = new FileNameExtensionFilter("hinh anh", "jpg", "png");
				fileChooser.setFileFilter(imgExt);
				fileChooser.setMultiSelectionEnabled(false);

				int x = fileChooser.showDialog(getContentPane(), "chon");
				if (x == JFileChooser.APPROVE_OPTION) {
					File f = fileChooser.getSelectedFile();
					BufferedImage image = null;
					try {
						image = ImageIO.read(f);
					} catch (IOException e) {
						e.printStackTrace();
					}
					Mat input = Java2DFrameUtils.toMat(image);
					Mat output = faceRecognizer.recognizerImage(input);
//					int h = 400;
//					int w = (int) (400 * ((double) output.cols() / (double) output.rows()));
					int w = 400;
					int h = (int) (400 * ((double) output.rows() / (double) output.cols()));
					ImageIcon icon = new ImageIcon(
							Java2DFrameUtils.toBufferedImage(output).getScaledInstance(w, h, Image.SCALE_SMOOTH));
					labelImg.setIcon(icon);
					labelImg.repaint();
				}
			}
		}).start();
	}

	public void recognizerCamera() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (enableCamera == false) {
						labelCamera.setIcon(loadIcon("iconApp//labelCamera.png", 400, 400));
						break;
					}
					videoCap.read(image);
					ImageIcon icon = new ImageIcon(
							Java2DFrameUtils.toBufferedImage(faceRecognizer.recognizerImage(image)));
					labelCamera.setIcon(icon);
					labelCamera.repaint();
				}
			}
		}).start();
	}

	public ImageIcon loadIcon(String path, int w, int h) {
		Image bufferImg = null;
		Image img = null;
		try {
			bufferImg = ImageIO.read(new File(path));
			img = bufferImg.getScaledInstance(w, h, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ImageIcon(img);
	}

	public void addGbcComponent(Component c, int x, int y, int w, int h) {
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		add(c, gbc);
	}

	public static void main(String[] args) {
		new App();
	}

}
