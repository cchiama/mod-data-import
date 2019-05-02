package org.folio.service.processing.reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class MarcJsonReader implements SourceReader {

  private static final Logger LOGGER = LoggerFactory.getLogger(MarcJsonReader.class);
  private JsonReader reader;
  private int chunkSize;

  public MarcJsonReader(File file, int chunkSize) {
    this.chunkSize = chunkSize;
    try {
      this.reader = new JsonReader(new InputStreamReader(FileUtils.openInputStream(file)));
    } catch (IOException e) {
      LOGGER.error("Cannot initialize reader", e);
      throw new RecordsReaderException(e);
    }
  }

  @Override
  public List<String> next() {
    RecordsBuffer recordsBuffer = new RecordsBuffer(this.chunkSize);
    try {
      Gson gson = new GsonBuilder().create();
      if (reader.peek().equals(JsonToken.BEGIN_ARRAY)) {
        reader.beginArray();
      }
      while (reader.hasNext()) {
        JsonObject record = gson.fromJson(reader, JsonObject.class);
        recordsBuffer.add(record.toString());
        if (recordsBuffer.isFull()) {
          return recordsBuffer.getRecords();
        }
      }
    } catch (IOException e) {
      LOGGER.error("Error reading next record", e);
      throw new RecordsReaderException(e);
    }
    return recordsBuffer.getRecords();
  }

  @Override
  public boolean hasNext() {
    try {
      boolean value = reader.hasNext();
      if(!value) {
        reader.close();
      }
      return value;
    } catch (IOException e) {
      LOGGER.error("Error checking for the next record", e);
      throw new RecordsReaderException(e);
    }
  }
}
