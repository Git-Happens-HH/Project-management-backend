package githappens.hh.project_management_app.ControllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;

import githappens.hh.project_management_app.domain.Task;
import githappens.hh.project_management_app.domain.TaskList;
import githappens.hh.project_management_app.domain.TaskListRepository;
import githappens.hh.project_management_app.domain.TaskRepository;
import githappens.hh.project_management_app.service.ProjectRealtimeService;
import githappens.hh.project_management_app.web.TaskRestController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskRestController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private TaskListRepository taskListRepository;

    @MockitoBean
    private ProjectRealtimeService realtimeService;

    private final Long projectId = 1L;
    private final Long taskListId = 2L;
    private final Long taskId = 3L;

    private TaskList taskList;
    private Task task;

    @BeforeEach
    void setUp() {
        taskList = new TaskList();
        taskList.setTaskListId(taskListId);

        task = new Task();
        task.setTaskId(taskId);
        task.setTitle("Original title");
        task.setDescription("Original Description");
        task.setDeadline(LocalDateTime.now().plusDays(1));
        task.setTaskList(taskList);
    }


    @Test
    void getTasksForTaskList_returnsTasksFromRepository() throws Exception {
        when(taskRepository.findByTaskList_TaskListId(taskListId)).thenReturn(List.of(task));

        mockMvc.perform(get("/api/projects/{projectId}/tasklists/{taskListId}/tasks", projectId, taskListId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].taskId").value(taskId))
                .andExpect(jsonPath("$[0].title").value("Original title"));

        verify(taskRepository).findByTaskList_TaskListId(taskListId);
    }

    @Test
    void getTasksForTaskList_returnsEmptyListWhenNoneFound() throws Exception {
        when(taskRepository.findByTaskList_TaskListId(taskListId)).thenReturn(List.of());

        mockMvc.perform(get("/api/projects/{projectId}/tasklists/{taskListId}/tasks", projectId, taskListId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getTaskById_returnsTaskWhenFound() throws Exception {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        mockMvc.perform(get("/api/projects/{projectId}/tasklists/{taskListId}/tasks",
                        projectId, taskListId, taskId))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.taskId").value(taskId));
    }
}
