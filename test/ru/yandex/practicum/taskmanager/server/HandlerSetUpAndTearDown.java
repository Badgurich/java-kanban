package ru.yandex.practicum.taskmanager.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.taskmanager.managers.TaskManager;
import ru.yandex.practicum.taskmanager.servers.HttpTaskServer;
import ru.yandex.practicum.taskmanager.util.Managers;
import ru.yandex.practicum.taskmanager.util.json.DurationAdapter;
import ru.yandex.practicum.taskmanager.util.json.LocalDateTimeAdapter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class HandlerSetUpAndTearDown {
		TaskManager tm = Managers.getDefault();
		HttpTaskServer server = new HttpTaskServer(tm);
		Gson gson = new GsonBuilder()
						.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
						.registerTypeAdapter(Duration.class, new DurationAdapter())
						.create();

		@BeforeEach
		void setUp() throws IOException {
				tm.removeAllTasks();
				tm.removeAllSubtasks();
				tm.removeAllEpics();
				server.start();
		}

		@AfterEach
		void tearDown() {
				server.stop();
		}

}
