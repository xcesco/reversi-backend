package org.abubusoft.reversi.server;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.fmt.games.reversi.model.Coordinates;
import org.abubusoft.reversi.server.repositories.support.CoordinateDeserializer;
import org.abubusoft.reversi.server.repositories.support.CoordinateSerializer;

public abstract class JSONMapperFactory {
  private JSONMapperFactory() {

  }

  public static ObjectMapper createMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
    mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    SimpleModule module = new SimpleModule();
    module.addSerializer(Coordinates.class, new CoordinateSerializer());
    module.addDeserializer(Coordinates.class, new CoordinateDeserializer());
    mapper.registerModule(module);

    mapper.registerModule(new Jdk8Module());
    mapper.registerModule(new JavaTimeModule());

    return mapper;
  }
}
