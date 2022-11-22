package model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.imageio.ImageIO;

/**
 * Represents an ImageProcessorModel that can apply different transformations to Images.
 */
public class ImageProcessorModel implements ProcessorModelState {
  private HashMap<String, Pixel[][]> images;

  /**
   * Constructs the ImageProcessor.
   */
  public ImageProcessorModel() {
    this.images = new HashMap<String, Pixel[][]>();
  }

  /**
   * Will load the given image into the images field.
   *
   * @param name  the name of the image we will load.
   * @param image the image itself that we are loading.
   */
  public void load(String name, Pixel[][] image) {
    this.images.put(name, image);
  }

  /**
   * Flips an image either vertically or horizontally.
   *
   * @param name     the name of the image to be flipped.
   * @param vertical boolean representing if we are flipping vertically or horizontally.
   */
  public void flip(String name, boolean vertical) {
    if (vertical) {
      this.verticalFlip(name);
    } else {
      this.horizontalFlip(name);
    }
  }

  /**
   * Flips an image horizontally.
   *
   * @param name the name of the image to be flipped.
   */
  private void horizontalFlip(String name) {
    this.checkPreCondition(name);
    Pixel[][] image = images.get(name);
    int height = image.length;
    int width = image[0].length;

    Pixel[][] dup = new Pixel[height][width];

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        Pixel pixel = image[row][col];

        dup[row][width - (col + 1)] = new Pixel(pixel);
      }
    }

    this.images.put(name, dup);
  }

  /**
   * Checks if the given name is in the images hashmap.
   *
   * @param name name you're checking.
   * @throws IllegalArgumentException name not in hashmap.
   */
  private void checkPreCondition(String name) throws IllegalArgumentException {
    if (!this.images.containsKey(name) || name == null) {
      throw new IllegalArgumentException("Image not loaded yet: " + name);
    }
  }

  /**
   * Flips an image vertically.
   *
   * @param name the name of the image to be flipped.
   */
  private void verticalFlip(String name) {
    this.checkPreCondition(name);
    Pixel[][] image = images.get(name);
    int height = image.length;
    int width = image[0].length;

    Pixel[][] dup = new Pixel[height][width];

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        Pixel pixel = image[row][col];

        dup[height - (row + 1)][col] = new Pixel(pixel);
      }
    }

    this.images.put(name, dup);
  }

  /**
   * Brightens or darkens an image by the given name by the given amount.
   *
   * @param value the amount to be brightened or darkened by.
   * @param name  the name of the image to be changed.
   */
  public void brighten(int value, String name) {
    this.checkPreCondition(name);

    Pixel[][] image = images.get(name);
    int height = image.length;
    int width = image[0].length;

    Pixel[][] dupe = new Pixel[height][width];

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        dupe[row][col] = new Pixel(image[row][col]);
        dupe[row][col].changeBrightness(value);
      }
    }
    this.images.put(name, dupe);
  }

  /**
   * Creates a completely new copy of the image.
   *
   * @param name the name of the image to be copied.
   * @return a 2d Pixel array that is a copy of the original 2d Pixel array.
   */
  public Pixel[][] getCopy(String name) {
    this.checkPreCondition(name);

    Pixel[][] image = images.get(name);
    int height = image.length;
    int width = image[0].length;

    Pixel[][] output = new Pixel[height][width];

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        output[row][col] = new Pixel(image[row][col]);
      }
    }

    return output;
  }

  /**
   * Converts the image with the given name to GrayScale using the given method.
   *
   * @param type the type of GrayScale method to be used.
   * @param name the name of the image to be converted to grayscale.
   */
  public void toGrayScale(GrayScale type, String name) {
    this.checkPreCondition(name);

    Pixel[][] image = images.get(name);
    int height = image.length;
    int width = image[0].length;

    Pixel[][] dupe = new Pixel[height][width];

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        dupe[row][col] = new Pixel(image[row][col]);
        dupe[row][col].toGrayScale(type);
      }
    }

    this.images.put(name, dupe);
  }

  /**
   * Saves the image wth the given name to the given path as a PPM file.
   *
   * @param path the path to be saved to.
   * @param name the name of the image to be saved.
   */
  public void save(String path, String name) {
    if (path.endsWith(".jpeg") || path.endsWith(".jpg")) {
      this.saveAny(path, name, "JPEG");
    } else if (path.endsWith(".png")) {
      this.saveAny(path, name, "PNG");
    } else if (path.endsWith(".ppm")) {
      this.savePPM(path, name);
    } else if (path.endsWith(".bmp")) {
      this.saveAny(path, name, "BMP");
    } else {
      throw new IllegalArgumentException("Invalid file format use .ppm, .jpeg, .jpg, .bmp or .png");
    }
  }

  /**
   * Saves the image with the given name to the given path as the given filetype.
   *
   * @param path   the path to save the file to.
   * @param name   the name of the image to save to a file.
   * @param format the format of the file to be saved as.
   */
  private void saveAny(String path, String name, String format) {
    this.checkPreCondition(name);
    Pixel[][] currImage = this.images.get(name);

    BufferedImage image = ImageUtil.convertToBufferedFromImage(currImage);

    try {
      ImageIO.write(image, format, new File(path));
    } catch (IOException e) {
      throw new IllegalStateException("Error writing file: " + path + "imagename: " + name);
    }
  }

  private void savePPM(String path, String name) {
    this.checkPreCondition(name);

    Pixel[][] image = this.images.get(name);
    int width = image[0].length;
    int height = image.length;

    String imageData = "P3" + "\n"
            + String.valueOf(width) + " " + String.valueOf(height) + "\n"
            + "255" + "\n"
            + this.toString(name);
    ImageUtil.savePPM(path, imageData);
  }

  /**
   * Converts the image with the given name to PPM format, as string.
   *
   * @param name name of the image to be saved.
   * @return a string in ppm format.
   */
  private String toString(String name) {
    this.checkPreCondition(name);

    Pixel[][] image = images.get(name);
    int height = image.length;
    int width = image[0].length;

    StringBuilder str = new StringBuilder();

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        str.append(image[row][col].getRed());
        str.append("\n");
        str.append(image[row][col].getGreen());
        str.append("\n");
        str.append(image[row][col].getBlue());
        str.append("\n");
      }
    }

    return str.toString();
  }

  /**
   * Applies a filter to the image with the given name.
   *
   * @param kernel the kernel filter applieid to the image.
   * @param name   the name of the image to be filtered.
   */
  private void filter(double[][] kernel, String name) {
    this.checkPreCondition(name);
    Pixel[][] image = images.get(name);
    int height = image.length;
    int width = image[0].length;

    Pixel[][] newImage = new Pixel[height][width];

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        newImage[row][col] = this.filterHelp(row, col, kernel, image);
      }
    }

    this.images.put(name, newImage);
  }

  private Pixel filterHelp(int pixelRow, int pixelCol, double[][] kernel,
                           Pixel[][] image) {
    int size = kernel.length;
    int red = 0;
    int green = 0;
    int blue = 0;

    for (int row = 0 - size / 2; row <= size / 2; row++) {
      for (int col = 0 - size / 2; col <= size / 2; col++) {
        if (!(pixelRow + row < 0 || pixelCol + col < 0 || pixelRow + row >= image.length
                || pixelCol + col >= image[0].length)) {

          red += image[pixelRow + row][pixelCol + col].getRed()
                  * kernel[row + size / 2][col + size / 2];
          green += image[pixelRow + row][pixelCol + col].getGreen()
                  * kernel[row + size / 2][col + size / 2];
          blue += image[pixelRow + row][pixelCol + col].getBlue()
                  * kernel[row + size / 2][col + size / 2];
        }
      }
    }

    return new Pixel(red, green, blue);
  }

  /**
   * Blurs the image with the given name with a set kernel filter.
   *
   * @param name the name of the image to be blurred.
   */
  @Override
  public void blur(String name) {
    System.out.println("blur");
    double[][] blurKernel =
            new double[][]{new double[]{.0625, .125, .0625}, new double[]{.125, .25, .125},
                           new double[]{.0625, .125, .0625}};
    this.filter(blurKernel, name);
  }

  /**
   * Sharpens the image with the given name with a set kernel filter.
   *
   * @param name the name of the image to be sharpened.
   */
  @Override
  public void sharpen(String name) {
    System.out.println("sharpen");
    double[][] sharpenKernel =
            new double[][]{ new double[]{-.125, -.125, -.125, -.125, -.125},
                            new double[]{-.125, .25, .25, .25, -.125},
                            new double[]{-.125, .25, 1, .25, -.125},
                            new double[]{-.125, .25, .25, .25, -.125},
                            new double[]{-.125, -.125, -.125, -.125, -.125}};
    this.filter(sharpenKernel, name);
  }

  private void colorTransformation(double[] transformation, String name) {
    this.checkPreCondition(name);
    Pixel[][] image = images.get(name);
    int height = image.length;
    int width = image[0].length;

    Pixel[][] newImage = new Pixel[height][width];

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        Pixel p = image[row][col].applyTransformation(transformation);
        newImage[row][col] = p;
      }
    }
    this.images.put(name, newImage);
  }

  /**
   * Turns an image to greyscale using a colorTransformation.
   *
   * @param name the name of the image to be greyscaled.
   */
  @Override
  public void transformationGrayscale(String name) {
    double[] transformation =
            new double[]{.2126, .7152, .0722, .2126, .7152, .0722, .2126, .7152, .0722};
    this.colorTransformation(transformation, name);
  }

  /**
   * Turns an image to sepia coloring using a colorTransformation.
   *
   * @param name the name of the image to be turned to sepia.
   */
  @Override
  public void sepia(String name) {
    double[] transformation =
            new double[]{.393, .769, .189, .349, .686, .168, .272, .534, .131};
    this.colorTransformation(transformation, name);
  }

  public Map<Integer, Integer> getRedCount(String imageName) {
    Objects.requireNonNull(imageName);

    Map<Integer, Integer> redCount = new HashMap<>();
    redCount.put(-1, 0);

    Pixel[][] image = this.images.get(imageName);
    for (int r = 0; r < image.length; r++) {
      for (int c = 0; c < image[0].length; c++) {
        int x = redCount.getOrDefault(image[r][c].getRed(), 0) + 1;
        redCount.put(-1, Math.max(redCount.get(-1), x));
        redCount.put(image[r][c].getRed(), x);
      }
    }

    return redCount;
  }

  public Map<Integer, Integer> getGreenCount(String imageName) {
    Objects.requireNonNull(imageName);

    Map<Integer, Integer> greenCount = new HashMap<>();
    greenCount.put(-1, 0);

    Pixel[][] image = this.images.get(imageName);
    for (int r = 0; r < image.length; r++) {
      for (int c = 0; c < image[0].length; c++) {
        int x = greenCount.getOrDefault(image[r][c].getGreen(), 0) + 1;
        greenCount.put(-1, Math.max(x, greenCount.get(-1)));
        greenCount.put(image[r][c].getGreen(), x);
      }
    }

    return greenCount;
  }

  public Map<Integer, Integer> getBlueCount(String imageName) {
    Objects.requireNonNull(imageName);

    Map<Integer, Integer> blueCount = new HashMap<>();
    blueCount.put(-1, 0);

    Pixel[][] image = this.images.get(imageName);
    for (int r = 0; r < image.length; r++) {
      for (int c = 0; c < image[0].length; c++) {
        int x = blueCount.getOrDefault(image[r][c].getBlue(), 0) + 1;
        blueCount.put(-1, Math.max(x, blueCount.get(-1)));
        blueCount.put(image[r][c].getBlue(), x);
      }
    }

    return blueCount;
  }

  public Map<Integer, Integer> getIntensityCount(String imageName) {
    Objects.requireNonNull(imageName);

    Map<Integer, Integer> intensityCount = new HashMap<>();
    intensityCount.put(-1, 0);

    Pixel[][] image = this.images.get(imageName);
    for (int r = 0; r < image.length; r++) {
      for (int c = 0; c < image[0].length; c++) {
        int x = intensityCount.getOrDefault((int)image[r][c].getIntensity(), 0) + 1;
        intensityCount.put(-1, Math.max(x, intensityCount.get(-1)));
        intensityCount.put((int)image[r][c].getIntensity(), x);
      }
    }
    return intensityCount;
  }

  @Override
  public boolean containsKey(String key) {
    return this.images.containsKey(key);
  }
}
