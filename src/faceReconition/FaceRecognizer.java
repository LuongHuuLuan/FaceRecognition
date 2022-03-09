package faceReconition;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

public class FaceRecognizer {
	private LBPHFaceRecognizer recognizer;
	private CascadeClassifier classify;

	public FaceRecognizer() {
		recognizer = LBPHFaceRecognizer.create();
		recognizer.read("model//model.yml");
		classify = new CascadeClassifier("xmls//haarcascade_frontalface_alt.xml");
	}

	public Mat recognizerImage(Mat img) {
		Mat imgGray = new Mat();
		try {
			opencv_imgproc.cvtColor(img, imgGray, opencv_imgproc.CV_BGRA2GRAY);
		} catch (Exception e) {
			imgGray = img;
		}
		RectVector faces = new RectVector();
		classify.detectMultiScale(imgGray, faces);
		for (Rect roi : faces.get()) {
			Mat face = new Mat(imgGray, roi);
			opencv_imgproc.resize(face, face, new Size(128, 128));
			IntPointer label = new IntPointer(1);
			DoublePointer confidence = new DoublePointer(1);
			recognizer.predict(face, label, confidence);
			int prediction = label.get(0);
			opencv_imgproc.rectangle(img, roi, new Scalar(0, 255, 0, 1));
			opencv_imgproc.putText(img, prediction + "," + confidence.get(0), roi.tl(),
					opencv_imgproc.CV_FONT_HERSHEY_PLAIN, 2, new Scalar(0, 255, 0, 1));
		}
		return img;
	}

	public Mat recognizerImage(String path) {
		Mat img = opencv_imgcodecs.imread(path);
		Mat imgGray = new Mat();
		opencv_imgproc.cvtColor(img, imgGray, opencv_imgproc.CV_BGRA2GRAY);
		RectVector faces = new RectVector();
		classify.detectMultiScale(imgGray, faces);
		for (Rect roi : faces.get()) {
			Mat face = new Mat(imgGray, roi);
			opencv_imgproc.resize(face, face, new Size(128, 128));
			IntPointer label = new IntPointer(1);
			DoublePointer confidence = new DoublePointer(1);
			recognizer.predict(face, label, confidence);
			int prediction = label.get(0);
			opencv_imgproc.rectangle(img, roi, new Scalar(0, 255, 0, 1));
			opencv_imgproc.putText(img, prediction + "," + confidence.get(0), roi.tl(),
					opencv_imgproc.CV_FONT_HERSHEY_SIMPLEX, 2, new Scalar(0, 255, 0, 1));
		}
		return img;
	}
}
