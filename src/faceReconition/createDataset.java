package faceReconition;

import java.io.File;

import javax.swing.JOptionPane;

import org.bytedeco.opencv.global.opencv_highgui;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

public class createDataset {

	CascadeClassifier classify;

	private VideoCapture videoCap = new VideoCapture(0);
	private boolean cap = false;

	public createDataset() {
		classify = new CascadeClassifier("xmls//haarcascade_frontalface_alt.xml");
	}

	public boolean create(String path, String modelName) {
		File pathRoot = new File(path);
		if (pathRoot.exists()) {
			File modelFoler = new File("dataset\\" + modelName);
			modelFoler.mkdir();
			for (File people : pathRoot.listFiles()) {
				String peopleName = people.getName();
				File peopleFolder = new File("dataset\\" + modelName + "\\" + peopleName);
				peopleFolder.mkdir();
				System.out.println("dataset\\" + modelName + "\\" + peopleName);
				for (File img : people.listFiles()) {
					Mat mat = opencv_imgcodecs.imread(img.getAbsolutePath(), opencv_imgcodecs.IMREAD_GRAYSCALE);
					RectVector faces = new RectVector();
					classify.detectMultiScale(mat, faces);
					for (Rect roi : faces.get()) {
						Mat face = new Mat(mat, roi);
						opencv_imgproc.resize(face, face, new Size(128, 128));
						String faceName = img.getName().split("\\.")[0];
						opencv_imgcodecs.imwrite(peopleFolder.getAbsolutePath() + "\\" + faceName + ".png", face);
						System.out.println(peopleFolder.getAbsolutePath() + "\\" + faceName + ".png");
					}
					opencv_highgui.imshow("tracking", mat);
					opencv_highgui.waitKey(1);
				}
			}
			System.out.println("xong");
		}
		return true;
	}

	public void getFaceOnCamera(String id, String name) {
		Mat img = new Mat();
		Mat imgGray = new Mat();
		while (true) {
			videoCap.read(img);
			opencv_imgproc.cvtColor(img, imgGray, opencv_imgproc.CV_BGR2GRAY);
			RectVector rois = new RectVector();
			classify.detectMultiScale(imgGray, rois);
			for (Rect roi : rois.get()) {
				// capture
				if (cap == true) {
					String folder = "dataset//myDataset//" + id + "_" + name;
					File dest = new File(folder);
					dest.mkdir();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					new Thread(new Runnable() {
						@Override
						public void run() {
							int count = 0;
							while (count < 21) {
								System.out.println(count);
								videoCap.read(img);
								RectVector roi1s = new RectVector();
								classify.detectMultiScale(imgGray, roi1s);
								for (Rect roi : roi1s.get()) {
									Mat face = new Mat(imgGray, roi);
									opencv_imgproc.resize(face, face, new Size(128, 128));
									opencv_imgcodecs.imwrite(folder+"//"+count+".png", imgGray);
									count++;
									try {
										Thread.sleep(500);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
							System.out.println("xong");
						}
					}).start();
					cap = false;
				}
				opencv_imgproc.rectangle(img, roi, new Scalar(0, 255, 0, 1));
			}
			opencv_highgui.imshow("faceDetection", img);
			int wait = opencv_highgui.waitKey(1);
			if (wait == 27) {
				opencv_highgui.destroyAllWindows();
				break;
			}
			if (wait == 'c') {
				System.out.println("capture...");
				cap = true;
			}
		}
	}

	public static void main(String[] args) {
		new createDataset().create("E://mydataset//train//", "myDataset");
//		new createDataset().getFaceOnCamera("4", "Luan");
	}
}
