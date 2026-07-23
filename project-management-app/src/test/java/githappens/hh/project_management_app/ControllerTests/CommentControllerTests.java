package githappens.hh.project_management_app.ControllerTests;
import org.springframework.test.web.servlet.MockMvc;
import githappens.hh.project_management_app.domain.CommentRepository;
import githappens.hh.project_management_app.domain.TaskRepository;
import githappens.hh.project_management_app.domain.TaskListRepository;
import githappens.hh.project_management_app.domain.ProjectRepository;
import githappens.hh.project_management_app.domain.AppUserRepository;
import githappens.hh.project_management_app.web.CommentRestController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import githappens.hh.project_management_app.domain.AppUser;
import githappens.hh.project_management_app.domain.Comment;
import githappens.hh.project_management_app.domain.Task;
import githappens.hh.project_management_app.domain.TaskList;
import githappens.hh.project_management_app.domain.Project;
import githappens.hh.project_management_app.security.JwtUtil;
import githappens.hh.project_management_app.web.AppUserDetailsServiceImpl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import org.springframework.http.MediaType;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@WebMvcTest(CommentRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CommentControllerTests {

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private AppUserDetailsServiceImpl appUserDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommentRepository commentRepository;

    @MockitoBean
    private TaskRepository taskRepository;

    @MockitoBean
    private TaskListRepository taskListRepository;

    @MockitoBean
    private ProjectRepository projectRepository;

    @MockitoBean
    private AppUserRepository appUserRepository;
 
    @Autowired
    private ObjectMapper objectMapper;
    
    private Comment comment;
    private AppUser commenter;
    private Task task;
    private TaskList taskList;
    private Project project;

    private final Long projectId = 1L;
    private final Long taskListId = 1L;
    private final Long taskId = 1L;
    private final Long commentId = 1L;


    @BeforeEach
    void setup() {
        project = new Project();
        project.setProjectId(projectId);
        project.setTitle("Test Project");
        project.setDescription("Project for comment tests");
        project.setCreatedAt(LocalDateTime.now());
        project.setIsShared(false);

        taskList = new TaskList();
        taskList.setTaskListId(taskListId);
        taskList.setProject(project); 
        taskList.setTitle("Test Task List");
        taskList.setCreatedAt(LocalDateTime.now());

        commenter = new AppUser();
        commenter.setAppUserId(1L);
        commenter.setFirstName("Test");
        commenter.setLastName("User");
        commenter.setUsername("testuser");
        commenter.setEmail("test@example.com");
        commenter.setPasswordHash("Password1!");

        task = new Task();
        task.setTaskId(taskId);
        task.setTaskList(taskList);
        task.setTitle("Test Task");
        task.setDescription("Task for comment tests");
        task.setCreatedBy(commenter);
        task.setAssignedUser(commenter);
        task.setDeadline(LocalDateTime.now().plusDays(1));

        comment = new Comment();
        comment.setCommentId(commentId);
        comment.setCommenter(commenter);
        comment.setTask(task);
        comment.setContent("This is a comment");
        comment.setCreatedAt(LocalDateTime.now());
    }

    // Should return a tasks comments
    @Test
    void shouldReturnTasksComments() throws Exception {

        when(taskRepository.findById(taskId))
        .thenReturn(Optional.of(task));

        when(commentRepository.findByTaskOrderByCreatedAtDesc(task))
        .thenReturn(List.of(comment));

        mockMvc.perform(get("/api/projects/{projectId}/tasklists/{taskListId}/tasks/{taskId}/comments", 
        projectId, taskListId, taskId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].content").value("This is a comment"));
    }

    @Test
    void shouldReturnEmptyListWhenNoComments() throws Exception {
        when(commentRepository.findByTaskOrderByCreatedAtDesc(task))
        .thenReturn(List.of());

        mockMvc.perform(get("/api/projects/{projectId}/tasklists/{taskListId}/tasks/{taskId}/comments", 
        projectId, taskListId, taskId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty());
    }

    // Should return a comment by commentId
    @Test
    void shouldReturnSpecificComment() throws Exception {

        when(commentRepository.findById(1L))
        .thenReturn(Optional.of(comment));

        // Test GET /api/projects/1/tasklists/1/tasks/1/comments/1
        mockMvc.perform(get("/api/projects/{projectId}/tasklists/{taskListId}/tasks/{taskId}/comments/{commentId}",
        projectId, taskListId, taskId, commentId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.commentId").value(1L));
    }

    // CREATE comment test
    @Test
    void shouldCreateComment() throws Exception {

        when(taskRepository.findById(taskId))
        .thenReturn(Optional.of(task));

        when(commentRepository.save(any((Comment.class)))).thenReturn(comment);

        // Test POST /api/projects/1/tasklists/1/tasks/1/comments
        mockMvc.perform(post("/api/projects/{projectId}/tasklists/{taskListId}/tasks/{taskId}/comments",
        projectId, taskListId, taskId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(comment)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.commentId").value(1L));

        verify(commentRepository).save(any(Comment.class));
}

    @Test
    void shouldDeleteComment() throws Exception {

        mockMvc.perform(delete("/api/projects/{projectId}/tasklists/{taskListId}/tasks/{taskId}/comments/{commentId}",
        projectId, taskListId, taskId, commentId))
        .andExpect(status().isOk());

        verify(commentRepository).deleteById(commentId);
    }

}

