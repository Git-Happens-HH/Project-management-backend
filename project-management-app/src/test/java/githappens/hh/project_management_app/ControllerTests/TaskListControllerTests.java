package githappens.hh.project_management_app.ControllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;

import githappens.hh.project_management_app.domain.Project;
import githappens.hh.project_management_app.domain.ProjectRepository;
import githappens.hh.project_management_app.domain.Task;
import githappens.hh.project_management_app.domain.TaskList;
import githappens.hh.project_management_app.domain.TaskListRepository;
import githappens.hh.project_management_app.domain.TaskRepository;
import githappens.hh.project_management_app.security.AuthTokenFilter;
import githappens.hh.project_management_app.service.ProjectRealtimeService;
import githappens.hh.project_management_app.web.TaskListRestController;
import githappens.hh.project_management_app.web.TaskRestController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskListRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TaskListControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskListRepository taskListRepository;

    @MockitoBean
    private ProjectRepository projectRepository;

    @MockitoBean
    private ProjectRealtimeService realtimeService;

    @MockitoBean
    private AuthTokenFilter authTokenFilter;

    private final Long projectId = 1L;
    private final Long taskListId = 2L;

    private Project project;
    private TaskList taskList;

    @BeforeEach
    void setUp() {
        project = new Project("Project title", "Project description", null);
        project.setProjectId(projectId);

        taskList = new TaskList();
        taskList.setTaskListId(taskListId);
        taskList.setProject(project);
    }

    @Test
    void getTaskListsForProject_returnsListsFromRepository() throws Exception {
        when(taskListRepository.findByProject_ProjectId(projectId)).thenReturn(List.of(taskList));

        mockMvc.perform(get("/api/projects/{projectId}/tasklists", projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].taskListId").value(taskListId));

        verify(taskListRepository).findByProject_ProjectId(projectId);
    }

    @Test
    void getTaskListById_returnsTaskListWhenFound() throws Exception {
        when(taskListRepository.findById(taskListId)).thenReturn(Optional.of(taskList));

        mockMvc.perform(get("/api/projects/{projectId}/tasklists/{taskListId}", projectId, taskListId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskListId").value(taskListId));
    }

    @Test
    void createTaskList_savesTaskListAndBroadcasts() throws Exception {
        TaskList newTaskList = new TaskList();
        newTaskList.setTitle("New list");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(taskListRepository.save(any(TaskList.class))).thenAnswer(inv -> {
            TaskList tl = inv.getArgument(0);
            tl.setTaskListId(99L);
            return tl;
        });

        mockMvc.perform(post("/api/projects/{projectId}/tasklists", projectId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTaskList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskListId").value(99));

        verify(taskListRepository).save(argThat(tl -> tl.getProject() != null
                && tl.getProject().getProjectId().equals(projectId)));
        verify(taskListRepository).flush();
        verify(realtimeService, times(1)).broadcastTaskLists(projectId);
    }

    @Test
    void deleteTaskList_deletesAndBroadcasts() throws Exception {
        doNothing().when(taskListRepository).deleteById(taskListId);

        mockMvc.perform(delete("/api/projects/{project}/tasklists/{taskListId}", projectId, taskListId))
        .andExpect(status().isOk());

        verify(taskListRepository).deleteById(taskListId);
        verify(taskListRepository).flush();
        verify(realtimeService, times(1)).broadcastTaskLists(projectId);
    }

}
