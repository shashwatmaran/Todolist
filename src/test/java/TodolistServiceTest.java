import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

public class TodolistServiceTest {

    private TodolistService service;

    @BeforeEach
    public void setUp() {
        service = new TodolistService();
    }

    @Test
    public void testAddTask() {
        Task task1 = service.addTask("Task 1", "Description 1");
        Task task2 = service.addTask("  Task 2  ", "Description 2");

        assertNotNull(task1);
        assertEquals(1, task1.getId());
        assertEquals("Task 1", task1.getTitle());
        assertFalse(task1.isCompleted());
        assertEquals(2, task2.getId());
        assertEquals("Task 2", task2.getTitle());
        assertEquals(2, service.getTaskCount());
    }

    @Test
    public void testAddTaskWithInvalidTitleThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.addTask(null, "Desc"));
        assertThrows(IllegalArgumentException.class, () -> service.addTask("", "Desc"));
        assertThrows(IllegalArgumentException.class, () -> service.addTask("   ", "Desc"));
    }

    @Test
    public void testGetTasks() {
        assertTrue(service.getAllTasks().isEmpty());

        service.addTask("Task 1", "Desc 1");
        service.addTask("Task 2", "Desc 2");

        List<Task> tasks = service.getAllTasks();
        assertEquals(2, tasks.size());

        tasks.clear();
        assertEquals(2, service.getTaskCount());

        Optional<Task> found = service.getTaskById(2);
        assertTrue(found.isPresent());
        assertEquals("Task 2", found.get().getTitle());
        assertFalse(service.getTaskById(999).isPresent());
    }

    @Test
    public void testUpdateTask() {
        service.addTask("Original", "Desc 1");
        service.addTask("Other Task", "Desc 2");

        boolean updated = service.updateTask(1, "  Updated  ", "New Desc", true);

        assertTrue(updated);
        Task task = service.getTaskById(1).get();
        assertEquals("Updated", task.getTitle());
        assertEquals("New Desc", task.getDescription());
        assertTrue(task.isCompleted());

        assertEquals("Other Task", service.getTaskById(2).get().getTitle());
        assertFalse(service.getTaskById(2).get().isCompleted());

        assertFalse(service.updateTask(999, "Title", "Desc", false));
    }

    @Test
    public void testUpdateTaskWithInvalidTitleThrowsException() {
        service.addTask("Task", "Desc");

        assertThrows(IllegalArgumentException.class, () -> service.updateTask(1, null, "Desc", false));
        assertThrows(IllegalArgumentException.class, () -> service.updateTask(1, "", "Desc", false));
    }

    @Test
    public void testDeleteTask() {
        service.addTask("Task 1", "Desc 1");
        service.addTask("Task 2", "Desc 2");
        service.addTask("Task 3", "Desc 3");

        assertTrue(service.deleteTask(2));
        assertEquals(2, service.getTaskCount());
        assertFalse(service.getTaskById(2).isPresent());
        assertTrue(service.getTaskById(1).isPresent());
        assertTrue(service.getTaskById(3).isPresent());

        assertFalse(service.deleteTask(999));

        service.deleteTask(1);
        Task newTask = service.addTask("Task 4", "Desc 4");
        assertEquals(4, newTask.getId());
    }

    @Test
    public void testMarkTaskCompleted() {
        service.addTask("Task", "Desc");

        assertTrue(service.markTaskCompleted(1));
        assertTrue(service.getTaskById(1).get().isCompleted());
        assertFalse(service.markTaskCompleted(999));
    }

    @Test
    public void testGetTasksByStatus() {
        service.addTask("Task 1", "Desc 1");
        service.addTask("Task 2", "Desc 2");
        service.markTaskCompleted(1);

        List<Task> pending = service.getTasksByStatus(false);
        assertEquals(1, pending.size());
        assertEquals("Task 2", pending.get(0).getTitle());

        service.markTaskCompleted(2);
        List<Task> completed = service.getTasksByStatus(true);
        assertEquals(2, completed.size());
    }
}