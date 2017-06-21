package com.pers.yothin.test;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class FaceDetector {    public static void main(String[] args) {

    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    System.out.println("\nRunning FaceDetector");

    CascadeClassifier faceDetector = new CascadeClassifier("D:/Program Files/opencv/sources/data/haarcascades/haarcascade_frontalface_default.xml");
    Mat image = Imgcodecs.imread("F:/11.jpg");

    MatOfRect faceDetections = new MatOfRect();
    faceDetector.detectMultiScale(image, faceDetections);

    System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
    for (Rect rect : faceDetections.toArray()) {
    	Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),new Scalar(0, 255, 0));
    }

    String filename = "F:/11.png";
    System.out.println(String.format("Writing %s", filename));
    Imgcodecs.imwrite(filename, image);
}
}
