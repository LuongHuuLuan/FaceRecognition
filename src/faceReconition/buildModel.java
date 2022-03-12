package faceReconition;

import static org.bytedeco.opencv.global.opencv_core.CV_32SC1;

import java.io.File;
import java.nio.IntBuffer;

import org.bytedeco.opencv.global.opencv_highgui;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;

public class buildModel {

	private FaceRecognizer recognizer = LBPHFaceRecognizer.create();

	public boolean build(String path, String modelName) {
		FaceManager.deleteAll();
		File pathRoot = new File(path);
		int size = countImgs(path);
		MatVector images = new MatVector(size);
		Mat labels = new Mat(size, 1, CV_32SC1);
		IntBuffer labelsBuf = labels.createBuffer();
		int counter = 0;
		for (File people : pathRoot.listFiles()) {
			String id = people.getName().split("_")[0];
			int label = Integer.parseInt(id);
			String name = people.getName().split("_")[1];
			FaceManager.insertFace(label, name);
			for (File img : people.listFiles()) {
				Mat mat = opencv_imgcodecs.imread(img.getAbsolutePath(), opencv_imgcodecs.IMREAD_GRAYSCALE);
				opencv_imgproc.resize(mat, mat, new Size(128, 128));
				images.put(counter, mat);
				labelsBuf.put(counter, label);
				counter++;
				opencv_highgui.imshow("tracking", mat);
				opencv_highgui.waitKey(1);
			}
		}
		recognizer.train(images, labels);
		recognizer.save("model//" + modelName + ".yml");
		return true;
	}

	public int countImgs(String path) {
		File root = new File(path);
		int counter = 0;
		for (File folder : root.listFiles()) {
			int imgs = folder.listFiles().length;
			counter += imgs;
		}
		return counter;
	}

	public static void main(String[] args) {
		new buildModel().build("dataset//", "model");
	}
}
