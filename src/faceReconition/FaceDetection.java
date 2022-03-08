package faceReconition;

import java.io.File;

import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;

public class FaceDetection {
	private String cascaseXML = "xmls//haarcascade_frontalface_alt.xml";
	private CascadeClassifier cascaseClassify = new CascadeClassifier(cascaseXML);

	public Mat[] getFace(Mat img) {
		Mat imgGray = new Mat();
		opencv_imgproc.cvtColor(img, imgGray, opencv_imgproc.CV_BGR2GRAY);
		RectVector rois = new RectVector();
		cascaseClassify.detectMultiScale(imgGray, rois);
		Mat[] result = new Mat[rois.get().length];
		for (int i = 0; i < rois.get().length; i++) {
			Mat face = new Mat(img, rois.get(i));
			opencv_imgproc.resize(face, face, new Size(128, 128));
			result[i] = face;
		}
		return result;
	}

	public RectVector getFaceLocation(Mat image) {
		Mat imgGray = new Mat();
		opencv_imgproc.cvtColor(image, imgGray, opencv_imgproc.CV_BGR2GRAY);
		RectVector rois = new RectVector();
		cascaseClassify.detectMultiScale(imgGray, rois);
		return rois;
	}

	public Mat detectFace(Mat image) {
		RectVector rois = getFaceLocation(image);
		for (Rect roi : rois.get()) {
			opencv_imgproc.rectangle(image, roi, new Scalar(255, 0, 0, 1));
		}
		return image;
	}

	public Mat cutFace(Mat image) {
		RectVector rois = getFaceLocation(image);
		System.out.println(rois.size());
		if (rois.size() != 1) {
			return null;
		}
		Mat result = new Mat(image, rois.get(0));
		opencv_imgproc.cvtColor(result, result, opencv_imgproc.CV_BGR2GRAY);
		return result;
	}

	public void saveImg(Mat image, String location, String name) {
		String path = location + "//" + name + ".jpg";
		opencv_imgcodecs.imwrite(path, image);
	}

}
