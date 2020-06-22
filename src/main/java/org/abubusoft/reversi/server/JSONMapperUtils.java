package org.abubusoft.reversi.server;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import it.fmt.games.reversi.model.Coordinates;
import org.abubusoft.reversi.server.repositories.support.CoordinateDeserializer;
import org.abubusoft.reversi.server.repositories.support.CoordinateSerializer;

public abstract class JSONMapperUtils {
  private JSONMapperUtils() {

  }

  public static ObjectMapper createMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
    mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    SimpleModule module = new SimpleModule();
    module.addSerializer(Coordinates.class, new CoordinateSerializer());
    module.addDeserializer(Coordinates.class, new CoordinateDeserializer());
    mapper.registerModule(module);

    return mapper;
  }
}
