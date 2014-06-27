package com.eim.facerecognition;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import android.util.Log;

public class EyeDetector {
	
	/** Global variables */
	
	private static final String TAG = "EyeDetector";
	
    private static String eyes_cascade_name = "haarcascade_eye_tree_eyeglasses.xml";
    private static CascadeClassifier eyes_cascade = new CascadeClassifier();
    private static String window_name = "Capture - Face detection";

	private Point eyeLeft;
	private Point eyeRight;
	
	public EyeDetector() { 
		
		if (!eyes_cascade.load(eyes_cascade_name)) {
			Log.w(TAG,"Error loading eyes cascade");
	    }
	}
	
	/**
	 * An accurate alignment of your image data is especially important in 
	 * tasks like emotion detection, were you need as much detail as possible.
	 * 
	 * @param src the source image
	 * @param src the destination image
	 * @param eyeLeft is the position of the left eye
	 * @param eyeRight is the position of the right eye
	 * @param offs is the percent of the image you want to keep next 
	 * 					to the eyes (horizontal, vertical direction)
	 * @param dsize the size of the output image
	 */
	public void cropFace(Mat src, Mat dst, Point offs) {

		Size dsize = dst.size();

		Log.d(TAG,"Crop Face");
		// Compute offsets in original image
		int offsetH = (int) Math.floor(((float) (offs.x)) * dsize.width);
		int offsetV = (int) Math.floor(((float) (offs.y)) * dsize.height);

		// Get the direction
		Point eyeDir = new Point(eyeRight.x - eyeLeft.x, eyeRight.y - eyeLeft.y);

		// Calc rotation angle in radians
		double rotation = -Math.atan2((float) (eyeDir.y), (float) (eyeDir.x));

		// Distance between them

		double dist = Math.sqrt(eyeDir.x * eyeDir.x + eyeDir.y * eyeDir.y);

		// Calculate the reference eye-width
		double reference = dsize.width - 2.0 * offsetH;

		// Scale factor
		double scale = ((float) (dist)) / ((float) (reference));

		// Rotate original around the left eye
		ScaleRotateTranslate(src, rotation, eyeLeft, null, -1);

		// crop the rotated image
		Point crop = new Point(eyeLeft.x - scale * offsetH, eyeLeft.x - scale
				* offsetV);

		Size cropSize = new Size(dsize.width * scale, dsize.height * scale);
		
		dst = crop(src, crop, cropSize);
		
		Imgproc.resize(dst, dst, dsize);

	}

	/**
	 * N.B. We suppose the argument is a gray image 
	 * @param img
	 */
	public void detectEye(Mat img) {
	
		/*
		Mat gray = new Mat();
		Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGRA2GRAY);
	    Imgproc.equalizeHist(gray, gray);
		*/
		
		MatOfRect eyes = new MatOfRect();
        Rect[] eyesArray = eyes.toArray();
        Rect face = new Rect(new Point(0,0), new Point(img.width(),img.height()));
        
        eyes_cascade.detectMultiScale(img, eyes, 1.1, 2, 0, new Size(30, 30), new Size());

        int len = eyesArray.length;
        
        // The input is a face. I should recognize two eyes.
        
        Log.i(TAG,"Detected eye(s) = " + len);
        
        if(len!=2)
        	return;
        
        Point[] mEyes = new Point[len];
        
        for (int j = 0; j < len; j++) {
           mEyes[j] = new Point(face.x + eyesArray[j].x + eyesArray[j].width * 0.5, 
        		   face.y + eyesArray[j].y + eyesArray[j].height * 0.5);

        }
        
        // Return the right place
        
        if (mEyes[0].x < mEyes[1].x) {
        	eyeLeft = mEyes[0];
        	eyeRight = mEyes[1];
        }
        else {
        	eyeRight = mEyes[0];
        	eyeLeft = mEyes[1];
        }
	}
	
	private void rotate(Mat src, Mat dst, double angle) {
		Point pt = new Point(src.cols() / 2., src.rows() / 2.);
		Mat r = Imgproc.getRotationMatrix2D(pt, angle, 1.0);
		Imgproc.warpAffine(src, dst, r, new Size(src.cols(), src.rows()));
	}

	private Mat crop(Mat uncropped, Point p, Size s) {
		Rect roi = new Rect(p, s);
		Mat cropped = new Mat(uncropped, roi);
		return cropped;
	}

	private void ScaleRotateTranslate(Mat img, double angle, Point center,
			Point nCenter, double scale /* resample = Image.BICUBIC ? */) {

		if (scale == -1 || center == null) {
			rotate(img, img, angle);
			return;
		}

		double x, nx, y, ny;
		x = nx = center.x;
		y = ny = center.y;

		if (nCenter != null) {
			nx = nCenter.x;
			ny = nCenter.y;
		}

		double sx, sy;
		sx = sy = 1.0;

		if (0.0 <= scale && scale <= 1.0) {
			sx = sy = scale;
		}

		double cos = Math.cos(angle);
		double sin = Math.sin(angle);

		double a, b, c, d, e, f;

		a = cos / sx;
		b = sin / sx;
		c = x - (nx * a) - (ny * b);
		d = -sin / sy;
		e = cos / sy;
		f = y - (nx * d) - (ny * e);

		// Matrix
		// [ a d ]
		// [ b e ]
		// [ c f ]

		Mat transMat = new Mat(2, 3, CvType.CV_64F);
		transMat.put(0, 0, a);
		transMat.put(1, 0, b);
		transMat.put(2, 0, c);
		transMat.put(0, 1, d);
		transMat.put(1, 1, e);
		transMat.put(2, 1, f);

		Core.transform(img, img, transMat /*, resample*/);
		
	}
	
	
}
