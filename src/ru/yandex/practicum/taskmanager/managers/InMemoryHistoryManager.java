package ru.yandex.practicum.taskmanager.managers;

import ru.yandex.practicum.taskmanager.tasktypes.Task;
import ru.yandex.practicum.taskmanager.util.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

		private final HashMap<Integer, Node<Task>> taskMap;
		public Node<Task> head;
		public Node<Task> tail;

		public InMemoryHistoryManager() {
				this.taskMap = new HashMap<>();
		}

		public List<Task> getTasks() {
				List<Task> tasks = new ArrayList<>();
				Node<Task> currentNode = head;
				while (!(currentNode == null)) {
						tasks.add(currentNode.task);
						currentNode = currentNode.next;
				}
				return tasks;
		}

		private void removeNode(Node<Task> node) {
				if (!(node == null)) {
						final Node<Task> next = node.next;
						final Node<Task> previous = node.previous;
						node.task = null;
						if (head == node && tail == node) {
								head = null;
								tail = null;
						} else if (head == node) {
								head = next;
								head.previous = null;
						} else if (tail == node) {
								tail = previous;
								tail.next = null;
						} else {
								previous.next = next;
								next.previous = previous;
						}
				}
				taskMap.values().remove(node);
		}

		private void linkLast(Task task) {
				final Node<Task> oldTail = tail;
				final Node<Task> newNode = new Node<>(oldTail, task, null);
				tail = newNode;
				taskMap.put(task.getTaskId(), newNode);
				if (oldTail == null) head = newNode;
				else oldTail.next = newNode;
		}

		@Override
		public void add(Task task) {
				if (!(task == null)) {
						remove(task.getTaskId());
						linkLast(task);
				}
		}

		@Override
		public void remove(int id) {
				removeNode(taskMap.get(id));
		}

		@Override
		public List<Task> getHistory() {
				return getTasks();
		}
}