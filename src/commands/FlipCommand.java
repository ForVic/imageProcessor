package commands;

import model.Pixel;
import model.ProcessorModelState;

/**
 * Represents a flip command.
 */
public class FlipCommand extends AbstractCommand implements ImageProcessorCommands {

  private boolean vertical;
  private String destination;

  /**
   * Constructs a flip command.
   *
   * @param name        represents the name of the image to be flipped.
   * @param destination destination name of the image to be flipped.
   * @param vertical    whether it is being flipped vertical or not.
   */
  public FlipCommand(String name, String destination, boolean vertical) {
    super(name);
    this.vertical = vertical;
    this.destination = destination;
  }

  /**
   * Makes the flip command go.
   *
   * @param model model for the command to act upon.
   */
  @Override
  public void commandGo(ProcessorModelState model) {
    Pixel[][] newImage = model.getCopy(name);
    model.load(destination, newImage);
    model.flip(destination, vertical);
  }
}
