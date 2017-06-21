package asokol.poc.repository;

import asokol.poc.dto.Image;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import lombok.extern.log4j.Log4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by asokol at 6/20/17
 */
@Log4j
@Repository
public class CassandraRepository {

  private final String IMAGE_ID_FIELD_NAME = "id";
  private final String IMAGE_HEIGHT_FIELD_NAME = "height";
  private final String IMAGE_WIDTH_FIELD_NAME = "width";
  private final String IMAGE_RESOLUTION_FIELD_NAME = "resolution";

  @Value("${cassandra.table.images}")
  private String table;

  private Session session;

  public CassandraRepository(Session session) {
    this.session = session;
  }

  /**
   * Get image by {@code imageId}.
   * An example of simple select where query.
   *
   * @param imageId image ID.
   * @return Image related to this image ID.
   */
  public Optional<Image> getImage(String imageId) {
    val query = QueryBuilder.select()
        .from(table)
        .where(QueryBuilder.eq(IMAGE_ID_FIELD_NAME, imageId));
    return Optional.ofNullable(session.execute(query)
        .one())
        .map(this::parseImageFromRow);
  }

  /**
   * Insert {@link Image} into C*.
   * An example of simple insert query.
   *
   * @param image {@link Image} object.
   */
  public void insertImage(Image image) {
    List<String> fieldNames = Arrays.asList(IMAGE_ID_FIELD_NAME,
        IMAGE_HEIGHT_FIELD_NAME,
        IMAGE_WIDTH_FIELD_NAME,
        IMAGE_RESOLUTION_FIELD_NAME
    );
    List<Object> values = Arrays.asList(image.getId(),
        image.getHeight(),
        image.getWidth(),
        image.getResolution()
    );
    val query = QueryBuilder.insertInto(table)
        .values(fieldNames, values);
    session.execute(query);
  }

  /**
   * Insert {@link Image} into C*.
   * An example of insert query with TTL.
   *
   * @param image {@link Image} object.
   * @param ttl   time to live in seconds.
   */
  public void insertWithImageWithTtl(Image image, Integer ttl) {
    List<String> fieldNames = Arrays.asList(IMAGE_ID_FIELD_NAME,
        IMAGE_HEIGHT_FIELD_NAME,
        IMAGE_WIDTH_FIELD_NAME,
        IMAGE_RESOLUTION_FIELD_NAME
    );
    List<Object> values = Arrays.asList(image.getId(),
        image.getHeight(),
        image.getWidth(),
        image.getResolution()
    );
    val query = QueryBuilder.insertInto(table)
        .values(fieldNames, values)
        .using(QueryBuilder.ttl(ttl));
    session.execute(query);
  }

  /**
   * Delete image by its id.
   *
   * @param imageId id of particular image.
   */
  public void deleteImage(String imageId) {
    val query = QueryBuilder.delete()
        .from(table)
        .where(QueryBuilder.eq(IMAGE_ID_FIELD_NAME, imageId));
    session.execute(query);
  }

  private Image parseImageFromRow(Row row) {
    val id = row.getString(IMAGE_ID_FIELD_NAME);
    val height = row.getInt(IMAGE_HEIGHT_FIELD_NAME);
    val width = row.getInt(IMAGE_WIDTH_FIELD_NAME);
    val resolution = row.getInt(IMAGE_RESOLUTION_FIELD_NAME);
    return Image.builder()
        .id(id)
        .height(height)
        .width(width)
        .resolution(resolution)
        .build();
  }

}
