package commands;

import model.Pixel;
import model.ProcessorModelState;

/**
 * Represents a brighten command, brightening or darkening an image.
 */
public class BrightenCommand extends AbstractCommand implements ImageProcessorCommands {

  private int increment;
  private String destination;

  /**
   * Constructs a brighten command.
   *
   * @param increment   integer to increment the brightness by.
   * @param name        the name of the image to be brightened.
   * @param destination the destination name of the image.
   */
  public BrightenCommand(int increment, String name, String destination) {
    super(name);
    this.destination = destination;
    this.increment = increment;
  }

  /**
   * Makes the brighten command go.
   *
   * @param model the model for the command to act upon.
   */
  @Override
  public void commandGo(ProcessorModelState model) {
    Pixel[][] newImage = model.getCopy(name);
    model.load(destination, newImage);
    model.brighten(increment, destination);
  }
}
