package faceReconition;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.opencv.global.opencv_highgui;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.opencv.highgui.HighGui;

public class reconizer_faces_video {
	private FaceRecognizer recognizer;
	private CascadeClassifier classify;
	private VideoCapture videoCapture;

	public reconizer_faces_video(String model) {
		recognizer = LBPHFaceRecognizer.create();
		recognizer.read(model);
		classify = new CascadeClassifier("xmls//haarcascade_frontalface_alt.xml");
		videoCapture = new VideoCapture(0);
	}

	public void recognizer() {
		Mat img;
		while (true) {
			img = new Mat();
			videoCapture.read(img);
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
				double con = confidence.get(0);
				String name = predict(prediction, con);
				opencv_imgproc.rectangle(img, roi, new Scalar(0, 255, 0, 1));
				opencv_imgproc.putText(img, name, roi.tl(), opencv_imgproc.CV_FONT_HERSHEY_SIMPLEX, 1,
						new Scalar(255, 0, 0, 1));
			}

			opencv_highgui.imshow("Frame", img);
			int end = opencv_highgui.waitKey(1);
			if (end == 27) {
				HighGui.destroyAllWindows();
				break;
			}

		}
	}

	public String predict(int label, double confidence) {
		if (confidence < 100) {
			if (label == 1) {
				return "Son Tung" + ", " + confidence;
			} else if (label == 2) {
				return "Tran Thanh" + ", " + confidence;
			} else if (label == 3) {
				return "Phung" + ", " + confidence;
			} else if (label == 4) {
				return "Luan" + ", " + confidence;
			}
		}
		return "Deo quen" + ", " + confidence;
	}

	public static void main(String[] args) {
		new reconizer_faces_video("model//model1.yml").recognizer();
	}
}
