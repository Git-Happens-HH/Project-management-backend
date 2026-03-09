package githappens.hh.project_management_app;

import java.time.LocalDateTime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import githappens.hh.project_management_app.domain.AppUser;
import githappens.hh.project_management_app.domain.AppUserRepository;
import githappens.hh.project_management_app.domain.Project;
import githappens.hh.project_management_app.domain.ProjectRepository;
import githappens.hh.project_management_app.domain.TaskList;
import githappens.hh.project_management_app.domain.TaskListRepository;
import githappens.hh.project_management_app.domain.Task;
import githappens.hh.project_management_app.domain.TaskRepository;
import githappens.hh.project_management_app.domain.Comment;
import githappens.hh.project_management_app.domain.CommentRepository;

@SpringBootApplication
public class ProjectManagementAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectManagementAppApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadTestData(
			AppUserRepository userRepository,
			ProjectRepository projectRepository,
			TaskListRepository taskListRepository,
			TaskRepository taskRepository,
			CommentRepository commentRepository
	) {
		return args -> {
			LocalDateTime now = LocalDateTime.now();

			// USERS
			if (userRepository.findByUsername("admin").isEmpty()) {
				AppUser admin = new AppUser();
				admin.setUsername("admin");
				admin.setFirstName("Admin");
				admin.setLastName("User");
				admin.setEmail("admin@example.com");
				admin.setPasswordHash("Admin@123");
				admin.setRegisteredAt(now);
				userRepository.save(admin);
			}

			if (userRepository.findByUsername("user").isEmpty()) {
				AppUser user = new AppUser();
				user.setUsername("user");
				user.setFirstName("Regular");
				user.setLastName("User");
				user.setEmail("user@example.com");
				user.setPasswordHash("User@123");
				user.setRegisteredAt(now);
				userRepository.save(user);
			}

			// PROJECTS, TASKLISTS, TASKS, COMMENTS
			if (projectRepository.count() == 0) {
				AppUser admin = userRepository.findByUsername("admin").orElseThrow();

				Project p1 = new Project();
				p1.setTitle("Demo Project");
				p1.setDescription("Seeded project");
				p1.setCreatedAt(now);
				projectRepository.save(p1);

				TaskList tl1 = new TaskList();
				tl1.setProject(p1);
				tl1.setTitle("Backlog");
				tl1.setPositionNumber(1);
				tl1.setCreatedAt(now);
				taskListRepository.save(tl1);

				Task t1 = new Task();
				t1.setTaskList(tl1);
				t1.setAssignedUser(admin);
				t1.setTitle("Initial task");
				t1.setDescription("This task was created by seed data");
				t1.setCreatedBy(admin);
				t1.setPositionNumber(1);
				t1.setDeadline(now.plusDays(7));
				taskRepository.save(t1);

				Comment c1 = new Comment();
				c1.setCommenter(admin);
				c1.setContent("Seed comment");
				c1.setTask(t1);
				c1.setCreatedAt(now);
				commentRepository.save(c1);
			}
		};
	}

}

// jos pitää tappaa prosessi:
// netstat -ano | findstr :8080
// taskkill /PID <PID> /F

// http://localhost:8080/h2-console 
// url: jdbc:h2:mem:testdb 
// username: sa
// password: password

