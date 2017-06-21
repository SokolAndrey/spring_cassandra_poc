package asokol.poc.controller;

import asokol.poc.dto.Image;
import asokol.poc.exception.ImageNotFoundException;
import asokol.poc.repository.CassandraRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by asokol at 6/20/17
 */
@Log4j
@RestController
public class Controller {

  private final CassandraRepository repository;

  @Autowired
  public Controller(CassandraRepository repository) {
    this.repository = repository;
  }

  @GetMapping("/getImage/{imageId}")
  public Image getImages(@PathVariable String imageId) throws ImageNotFoundException {
    return repository.getImage(imageId)
        .orElseThrow(() -> new ImageNotFoundException(imageId));
  }

  @PostMapping("/insertImage")
  public void getImages(@RequestBody Image image) {
    repository.insertImage(image);
  }

  @PostMapping("/insertImageWithTtl/{ttl}")
  public void getImages(@RequestBody Image image, @PathVariable Integer ttl) {
    repository.insertWithImageWithTtl(image, ttl);
  }

  @PutMapping("/deleteImage/{imageId}")
  public void deleteImage(@PathVariable String imageId) {
    repository.deleteImage(imageId);
  }


  @ExceptionHandler(ImageNotFoundException.class)
  public ResponseEntity<String> imageNotFoundExceptionHandler(ImageNotFoundException ex) {
    log.error("No image has been found by ID: " + ex.getImageId());
    // TODO(asokol): 6/20/17 show Jesus when there is no image.
    return new ResponseEntity<>("POPA: There is no such image", HttpStatus.NOT_FOUND);
  }

}
