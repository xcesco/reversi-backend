package org.abubusoft.reversi.server.repositories.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.fmt.games.reversi.model.GameSnapshot;
import org.abubusoft.reversi.server.JSONMapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

@Converter(autoApply = true)
public class GameSnaphostJpaConverterJson implements AttributeConverter<GameSnapshot, String> {
  private static final Logger logger = LoggerFactory.getLogger(GameSnaphostJpaConverterJson.class);

  private static final ObjectMapper objectMapper= JSONMapperFactory.createMapper();

  @Override
  public String convertToDatabaseColumn(GameSnapshot meta) {
    try {
      return objectMapper.writeValueAsString(meta);
    } catch (JsonProcessingException ex) {
      ex.printStackTrace();
      return null;
      // or throw an error
    }
  }

  @Override
  public GameSnapshot convertToEntityAttribute(String dbData) {
    try {
      return objectMapper.readValue(dbData, GameSnapshot.class);
    } catch (IOException ex) {
      ex.printStackTrace();
      logger.error("Unexpected IOEx decoding json from database: " + dbData);
      return null;
    }
  }

}