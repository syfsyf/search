package org.syfsyf.search.jrpc;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.j256.ormlite.dao.ForeignCollection;

/**
 * Deserialize always to null value.
 * @author syfsyf@gmail.com
 *
 */
@SuppressWarnings("rawtypes")
public class NullForeignCollectionDeserializer extends JsonDeserializer<ForeignCollection>{

	@Override
	public ForeignCollection deserialize(JsonParser jp,
			DeserializationContext ctxt) throws IOException,
			JsonProcessingException {
		return null;
	}
}
