package commands;

import model.ColorTransformationType;
import model.Pixel;
import model.ProcessorModelState;

/**
 * Represents a color transformation command to be applied to an image.
 */
public class ColorTransformationCommand extends AbstractCommand implements ImageProcessorCommands {

  ColorTransformationType type;
  String destination;

  /**
   * Constructs the abstract command.
   *
   * @param name name of the image to run the command on.
   */
  public ColorTransformationCommand(String name, String destination, ColorTransformationType type) {
    super(name);
    this.type = type;
    this.destination = destination;
  }

  /**
   * Makes the model go.
   *
   * @param model the model for the command to act upon.
   */
  @Override
  public void commandGo(ProcessorModelState model) {
    Pixel[][] newImage = model.getCopy(name);
    model.load(destination, newImage);
    if (this.type == ColorTransformationType.Gray) {
      model.transformationGrayscale(destination);
    } else if (this.type == ColorTransformationType.Sepia) {
      model.sepia(destination);
    } else {
      throw new IllegalArgumentException("Invalid type for color transformation");
    }
  }
}

