package model;

import java.util.Map;

/**
 * Represents a ProcessorModel.
 */
public interface ProcessorModelState {
  /**
   * Will load the given image into the images field.
   *
   * @param name  the name of the image we will load.
   * @param image the image itself that we are loading.
   */
  public void load(String name, Pixel[][] image);

  /**
   * Flips an image either vertically or horizontally.
   *
   * @param name     the name of the image to be flipped.
   * @param vertical boolean representing if we are flipping vertically or horizontally.
   */
  public void flip(String name, boolean vertical);

  /**
   * Brightens or darkens an image by the given name by the given amount.
   *
   * @param value the amount to be brightened or darkened by.
   * @param name  the name of the image to be changed.
   */
  public void brighten(int value, String name);

  /**
   * Creates a completely new copy of the image.
   *
   * @param name the name of the image to be copied.
   * @return a 2d Pixel array that is a copy of the original 2d Pixel array.
   */
  public Pixel[][] getCopy(String name);

  /**
   * Converts the image with the given name to GrayScale using the given method.
   *
   * @param type the type of GrayScale method to be used.
   * @param name the name of the image to be converted to grayscale.
   */
  public void toGrayScale(GrayScale type, String name);

  /**
   * Saves the image wth the given name to the given path as a PPM file.
   *
   * @param path the path to be saved to.
   * @param name the name of the image to be saved.
   */
  public void save(String path, String name);

  /**
   * Blurs an image.
   * @param name the name of image to be blurred.
   */
  public void blur(String name);

  /**
   * Sharpens an image with a sharpening kernel.
   * @param name the name of the image to be sharpened.
   */
  public void sharpen(String name);

  /**
   * Turns an image to grayscale.
   * @param name the name of the image to be made grayscale.
   */
  public void transformationGrayscale(String name);

  /**
   * Turns an image to sepia.
   * @param name the name of the image to be turned sepia.
   */
  public void sepia(String name);

  public Map<Integer, Integer> getRedCount(String imageName);
  public Map<Integer, Integer> getGreenCount(String imageName);
  public Map<Integer, Integer> getBlueCount(String imageName);
  public Map<Integer, Integer> getIntensityCount(String imageName);
  public boolean containsKey(String key);
}
