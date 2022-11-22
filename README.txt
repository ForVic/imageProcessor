Classes:

Commands
1. AbstractCommand - the abstract class to represent a command since we noticed that every command,
contained the same field of name.
2. BrightenCommand - the command that brightens an image.
3. FlipCommand - the command that flips the image, both vertically and horizontally based on
a parameter.
4. GrayscaleCommand - the command that turns the image Grayscale based on the parameter.
5. ImageProcessorCommands - the interface representing commands.
6. LoadCommand - the command that loads an image.
7. SaveCommand - the command that saves an image.
8. ColorTransformationCommand - the command to apply color transformations, the current valid ones
are sepia and grayscale.
9. FilterCommand - the command to apply a filter to an image, current valid ones are blur and
sharpen.

Controllers
1. IIPC (Interface controller) - represents the controller interface.
2. ImageProcessorController - the controller for the image processor allowing you to play the game
with ProcessorModelStates.

Models
1. ImageProcessorModel - the model for the image processor, it stores the images in the model,
and manipulates the images in the required ways.
2. ProcessorModelState - the interface representing the ProcessorModels.

Misc.
1. GrayScale - an enum representing the possible methods for turning an image grayscale.
2. Pixel - represents a pixel in an image, the image is contained in the model.
3. ImageUtil - used to read and write to ppm files.
4. ColorTransformationType - an enum representing the possible types of color transformations.
5. FilterType - an enum representing the possible types of filters to apply to an image.

Image Citation:
https://unsplash.com/photos/uI0zA_C-y1c
Using unsplash free image license.

Design Changes:
No major or notable design changes were made between this and last homework other than a few small
abstractions that became necessary as we implemented the features of this homework.