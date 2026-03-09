package githappens.hh.project_management_app;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import githappens.hh.project_management_app.domain.UserProject;
import githappens.hh.project_management_app.domain.UserProjectRepository;

@SpringBootApplication
public class ProjectManagementAppApplication {

	// jos pitää tappaa prosessi:
	// netstat -ano | findstr :8080
	// taskkill /PID <PID> /F

	// http://localhost:8080/h2-console
	// url: jdbc:h2:mem:testdb
	// username: sa
	// password: password

	public static void main(String[] args) {
		SpringApplication.run(ProjectManagementAppApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadTestData(
			AppUserRepository userRepository,
			ProjectRepository projectRepository,
			TaskListRepository taskListRepository,
			TaskRepository taskRepository,
			CommentRepository commentRepository,
			UserProjectRepository userProjectRepository) {
		return args -> {
			LocalDateTime now = LocalDateTime.now();

			// USERS

			AppUser admin = new AppUser();
			admin.setUsername("testadmin");
			admin.setFirstName("Admin");
			admin.setLastName("User");
			admin.setEmail("admin@example.com");
			admin.setPasswordHash("Admin@123");
			admin.setRegisteredAt(now);
			admin.setProjects(new ArrayList<>());
			admin.setTasksAssigned(new ArrayList<>());
			admin.setTasksCreated(new ArrayList<>());
			admin.setComments(new ArrayList<>());
			userRepository.save(admin);

			AppUser user = new AppUser();
			user.setUsername("testuser");
			user.setFirstName("Regular");
			user.setLastName("User");
			user.setEmail("user@example.com");
			user.setPasswordHash("User@123");
			user.setRegisteredAt(now);
			user.setProjects(new ArrayList<>());
			user.setTasksAssigned(new ArrayList<>());
			user.setTasksCreated(new ArrayList<>());
			user.setComments(new ArrayList<>());
			userRepository.save(user);

			// PROJECTS, TASKLISTS, TASKS, COMMENTS

			admin = userRepository.findByUsername("testadmin").orElseThrow();
			user = userRepository.findByUsername("testuser").orElseThrow();

			Project p1 = new Project();
			p1.setTitle("Demo Project");
			p1.setDescription("Seeded project");
			p1.setCreatedAt(now);
			projectRepository.save(p1);

			// Link admin as project owner

			UserProject up1 = new UserProject(admin, p1, false);
			userProjectRepository.save(up1);

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

			Project p2 = new Project();
			p2.setTitle("Website Redesign");
			p2.setDescription("Second seeded project");
			p2.setCreatedAt(now.plusHours(1));
			projectRepository.save(p2);

			// Link user as project owner and share with admin

			UserProject up2 = new UserProject(user, p2, false);
			userProjectRepository.save(up2);
			UserProject up3 = new UserProject(admin, p2, true);
			userProjectRepository.save(up3);

			TaskList tl2 = new TaskList();
			tl2.setProject(p2);
			tl2.setTitle("In Progress");
			tl2.setPositionNumber(2);
			tl2.setCreatedAt(now.plusHours(1));
			taskListRepository.save(tl2);

			Task t2 = new Task();
			t2.setTaskList(tl2);
			t2.setAssignedUser(user);
			t2.setTitle("Create homepage mockup");
			t2.setDescription("Prepare updated landing page design");
			t2.setCreatedBy(user);
			t2.setPositionNumber(2);
			t2.setDeadline(now.plusDays(14));
			taskRepository.save(t2);

			Comment c2 = new Comment();
			c2.setCommenter(user);
			c2.setContent("Second seed comment");
			c2.setTask(t2);
			c2.setCreatedAt(now.plusHours(1));
			commentRepository.save(c2);
		};
	}

}
