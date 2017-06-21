package asokol.poc.exception;

import org.springframework.data.crossstore.ChangeSetPersister;

/**
 * Created by asokol at 6/20/17
 */
public class ImageNotFoundException extends ChangeSetPersister.NotFoundException {

  private String imageId;

  public ImageNotFoundException(String imageId) {
    this.imageId = imageId;
  }

  public String getImageId() {
    return imageId;
  }
}
