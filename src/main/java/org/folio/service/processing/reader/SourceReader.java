package org.folio.service.processing.reader;

import java.util.List;

import org.folio.rest.jaxrs.model.RawRecordsDto.ContentType;


/**
 * The root interface for traversing and partitioning elements of a source records.
 */
public interface SourceReader {

  /**
   * Returns the next list of source records in the iteration.
   *
   * @return the next element in the iteration
   */
  List<String> next();

  /**
   * Returns {@code true} if the iteration has more elements.
   * (In other words, returns {@code true} if {@link #next} would
   * return an element rather than throwing an exception.)
   *
   * @return {@code true} if the iteration has more elements
   */
  boolean hasNext();

  /**
   * Describes type of records and format of record representation.
   *
   * @return content type.
   */
  ContentType getContentType();

}
