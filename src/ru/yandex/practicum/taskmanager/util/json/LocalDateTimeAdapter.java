package ru.yandex.practicum.taskmanager.util.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
		private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

		@Override
		public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
				if (localDateTime != null) {
						jsonWriter.value(localDateTime.format(dtf));
				} else {
						jsonWriter.value("null");
				}
		}

		@Override
		public LocalDateTime read(final JsonReader jsonReader) throws IOException {
				if (jsonReader.peek() == JsonToken.NULL) {
						jsonReader.nextNull();
						return null;
				} else {
						String dateStr = jsonReader.nextString();
						if ("null".equals(dateStr)) {
								return null;
						}
						return LocalDateTime.parse(dateStr, dtf);
				}
		}
}