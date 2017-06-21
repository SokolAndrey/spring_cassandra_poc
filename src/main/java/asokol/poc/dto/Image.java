package asokol.poc.dto;

import lombok.Builder;
import lombok.Value;

/**
 * Created by asokol at 6/20/17
 */
@Value
@Builder
public class Image {
  String id;
  Integer height;
  Integer width;
  Integer resolution;
}
