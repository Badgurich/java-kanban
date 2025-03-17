package ru.yandex.practicum.taskmanager.util;

public class Node<Task> {
    public Task task;
    public Node<Task> next;
    public Node<Task> previous;

    public Node(Node<Task> previous, Task task, Node<Task> next) {
        this.task = task;
        this.next = next;
        this.previous = previous;
    }
}