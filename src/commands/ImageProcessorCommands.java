package commands;

import model.ProcessorModelState;

/**
 * Represents a command for an ImageProcessor.
 */
public interface ImageProcessorCommands {

  /**
   * Makes the command go.
   *
   * @param model model for the command to act upon.
   */
  void commandGo(ProcessorModelState model);
}
