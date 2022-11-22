package commands;

import model.GrayScale;
import model.Pixel;
import model.ProcessorModelState;

/**
 * Represents a greyscale command.
 */
public class GrayscaleCommand extends AbstractCommand implements ImageProcessorCommands {
  private GrayScale method;
  private String destination;

  /**
   * Constructs a greyscale command.
   *
   * @param method      greyscale method to change the image to greyscale.
   * @param name        the name of the image to be brightened.
   * @param destination the destination name of the image.
   */
  public GrayscaleCommand(GrayScale method, String name, String destination) {
    super(name);
    this.method = method;
    this.destination = destination;
  }

  /**
   * Makes the greyscale command go.
   *
   * @param model the model for the command to act upon.
   */
  @Override
  public void commandGo(ProcessorModelState model) {
    Pixel[][] newImage = model.getCopy(name);
    model.load(destination, newImage);
    model.toGrayScale(method, destination);
  }
}
