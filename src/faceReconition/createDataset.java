package faceReconition;

import java.io.File;

import org.bytedeco.opencv.global.opencv_highgui;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;

public class createDataset {

	CascadeClassifier classify;

	public createDataset() {
		classify = new CascadeClassifier("xmls//haarcascade_frontalface_alt.xml");
	}

	public boolean create(String path) {
		File pathRoot = new File(path);
		if (pathRoot.exists()) {
			File modelFoler = new File("dataset\\");
			if (!modelFoler.exists())
				modelFoler.mkdir();
			for (File people : pathRoot.listFiles()) {
				String peopleName = people.getName();
				File peopleFolder = new File("dataset\\" + peopleName);
				if (!peopleFolder.exists())
					peopleFolder.mkdir();
				System.out.println("dataset\\" + peopleName);
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

	public static void main(String[] args) {
		new createDataset().create("E://mydataset//train//");
	}
}
