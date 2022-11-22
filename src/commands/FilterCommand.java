package commands;

import model.FilterType;
import model.Pixel;
import model.ProcessorModelState;

/**
 * Represents a filter command to be applied to an image.
 */
public class FilterCommand extends AbstractCommand implements ImageProcessorCommands {

  String destination;
  FilterType filterType;

  /**
   * Constructs the abstract command.
   *
   * @param name name of the image to run the command on.
   */
  public FilterCommand(String name, String destination, FilterType filterType) {
    super(name);
    this.destination = destination;
    this.filterType = filterType;
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
    if (filterType == FilterType.Blur) {
      model.blur(destination);
    } else if (filterType == FilterType.Sharpen) {
      model.sharpen(destination);
    } else {
      throw new IllegalArgumentException("Invaid filter type");
    }
  }
}
