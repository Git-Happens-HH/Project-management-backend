package githappens.hh.project_management_app.ControllerTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import githappens.hh.project_management_app.domain.Task;
import githappens.hh.project_management_app.domain.TaskList;
import githappens.hh.project_management_app.domain.TaskListRepository;
import githappens.hh.project_management_app.domain.TaskRepository;
import githappens.hh.project_management_app.security.AuthTokenFilter;
import githappens.hh.project_management_app.service.ProjectRealtimeService;
import githappens.hh.project_management_app.web.TaskRestController;

import org.springframework.http.MediaType;

@WebMvcTest(TaskRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TaskControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskRepository taskRepository;

    @MockitoBean
    private TaskListRepository taskListRepository;

    @MockitoBean
    private AuthTokenFilter authTokenFilter;

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

        mockMvc.perform(get("/api/projects/{projectId}/tasklists/{taskListId}/tasks/{taskId}",
                projectId, taskListId, taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(taskId));
    }

    @Test
    void createTask_savesTask() throws Exception {
        Task newTask = new Task();
        newTask.setTitle("new task");
        newTask.setDescription("Do the thing");

        when(taskListRepository.findById(taskListId)).thenReturn(Optional.of(taskList));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> {
            Task t = inv.getArgument(0);
            t.setTaskId(99L);
            return t;
        });

        mockMvc.perform(post("/api/projects/{projectId}/tasklists/{taskListId}/tasks", projectId, taskListId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(99L))
                .andExpect(jsonPath("$.title").value("new task"));

        verify(taskRepository).save(any(Task.class));
        verify(taskRepository).flush();
        verify(realtimeService, times(1)).broadcastTaskLists(projectId);
    }

    @Test
    void saveEditedTask_updatesEditableFieldsAndBroadcasts() throws Exception {
        Task edits = new Task();
        edits.setTitle("Updated title");
        edits.setDescription("Updated description");
        edits.setDeadline(LocalDateTime.now().plusDays(5));

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskListRepository.findById(taskListId)).thenReturn(Optional.of(taskList));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        mockMvc.perform(post("/api/projects/{projectId}/tasklists/{taskListId}/tasks/{taskId}",
                projectId, taskListId, taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(edits)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated title"))
                .andExpect(jsonPath("$.description").value("Updated description"));

        verify(taskRepository).save(
                argThat(t -> t.getTitle().equals("Updated title") && t.getDescription().equals("Updated description")));
        verify(realtimeService).broadcastTaskLists(projectId);
    }

    @Test
    void deleteTask_deletesAndBroadcasts() throws Exception {
        doNothing().when(taskRepository).deleteById(taskId);

        mockMvc.perform(delete("/api/projects/{projectId}/tasklists/{taskListId}/tasks/{taskId}",
                projectId, taskListId, taskId))
                .andExpect(status().isOk());

        verify(taskRepository).deleteById(taskId);
        verify(taskRepository).flush();
        verify(realtimeService, times(1)).broadcastTaskLists(projectId);
    }
}
