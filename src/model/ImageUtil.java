package model;

import controller.ImageProcessorController;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.ImageIO;

/**
 * This class contains utility methods to read a PPM image from file and simply print its contents.
 * Feel free to change this method as required.
 */
public class ImageUtil {

  public static BufferedImage convertToBufferedFromImage(Pixel[][] currImage) {
    if (currImage == null) {
      throw new IllegalArgumentException("Null image when converting to BufferedImage");
    }
    int width = currImage[0].length;
    int height = currImage.length;

    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        int currRed = currImage[row][col].getRed();
        int currGreen = currImage[row][col].getGreen();
        int currBlue = currImage[row][col].getBlue();
        if (currBlue > 255 || currRed > 255 || currGreen > 255) {
          currBlue = Math.min(255, currBlue);
          currRed = Math.min(255, currRed);
          currGreen = Math.min(255, currGreen);
        }

        if (currBlue < 0 || currRed < 0 || currGreen < 0) {
          currBlue = Math.max(0, currBlue);
          currRed = Math.max(0, currRed);
          currGreen = Math.max(0, currGreen);
        }

        Color your_color = new Color(currRed, currGreen, currBlue);
        String hex_string = Integer.toHexString(your_color.getRGB()).substring(2);

        int hex = Integer.parseInt(hex_string, 16);

        image.setRGB(col, row, hex);
      }
    }

    return image;
  }

  /**
   * Saves the data to a ppm file at the given path.
   *
   * @param filePath the string representing the local file path.
   * @param data     the ppm image as a string.
   */
  public static void savePPM(String filePath, String data) {
    Path fileDest = Paths.get(filePath);
    byte[] bytes = data.getBytes();
    try {
      Files.write(fileDest, bytes);
    } catch (IOException exception) {
      throw new IllegalStateException("Error writing to file.");
    }
  }

  /**
   * Reads the file with the given file name and returns the associated image.
   *
   * @param filename the filename to be read.
   * @return the array of pixels of the image.
   */
  public static Pixel[][] readFile(String filename) {
    if (filename.endsWith(".ppm")) {
      return ImageUtil.readPPM(filename);
    } else if (filename.endsWith(".bmp") || filename.endsWith(".jpeg") ||
            filename.endsWith(".png") || filename.endsWith(".jpg")) {
      return ImageUtil.readJPEGPNGBMP(filename);
    } else {
      throw new IllegalArgumentException("Invalid filename");
    }
  }

  private static Pixel[][] readJPEGPNGBMP(String filename) {
    try {
      BufferedImage image = ImageIO.read(new File(filename));
      int height = image.getHeight();
      int width = image.getWidth();

      Pixel[][] outputImage = new Pixel[height][width];

      for (int row = 0; row < height; row++) {
        for (int col = 0; col < width; col++) {

          int color = (image.getRGB(col, row));
          int blue = (color & 0xff);
          int green = (color & 0xff00) >> 8;
          int red = (color & 0xff0000) >> 16;

          outputImage[row][col] = new Pixel(red, green, blue);
        }
      }
      return outputImage;
    } catch (IOException e) {
      throw new IllegalStateException("Error reading file: " + filename);
    }
  }

  /**
   * Read an image file in the PPM format and print the colors.
   *
   * @param filename the path of the file.
   */
  private static Pixel[][] readPPM(String filename) {
    Scanner sc;

    try {
      sc = new Scanner(new FileInputStream(filename));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File " + filename + " not found!");
    }
    StringBuilder builder = new StringBuilder();
    //read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s + System.lineSeparator());
      }
    }

    //now set up the scanner to read from the string we just built
    sc = new Scanner(builder.toString());

    String token;

    token = sc.next();
    if (!token.equals("P3")) {
      System.out.println("Invalid PPM file: plain RAW file should begin with P3");
    }
    int width = sc.nextInt();
    System.out.println("Width of image: " + width);
    int height = sc.nextInt();
    System.out.println("Height of image: " + height);
    int maxValue = sc.nextInt();
    System.out.println("Maximum value of a color in this file (usually 255): " + maxValue);

    Pixel[][] image = new Pixel[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int r = sc.nextInt();
        int g = sc.nextInt();
        int b = sc.nextInt();

        image[i][j] = new Pixel(r, g, b);
      }
    }

    return image;
  }

  /**
   * Runs when run.
   *
   * @param args String[] of arguments.
   */
  public static void main(String[] args) {

    ImageProcessorModel model;
    ImageProcessorController controller;
    if (Arrays.asList(args).contains("-file")) {
      String filename = args[1];
      try {
        Readable x = new FileReader(new File(filename));
        model = new ImageProcessorModel();
        controller = new ImageProcessorController(model, x);
        controller.setFileReadTrue();
      } catch (IOException e) {
        throw new IllegalStateException("Error reading script: " + filename);
      }
    } else {
      model = new ImageProcessorModel();
      controller = new ImageProcessorController(model);
    }
    controller.startProgram();
  }
}

