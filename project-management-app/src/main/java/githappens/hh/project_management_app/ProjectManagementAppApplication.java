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
import org.springframework.security.crypto.password.PasswordEncoder;

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
			UserProjectRepository userProjectRepository,
			PasswordEncoder passwordEncoder) {
		return args -> {
			LocalDateTime now = LocalDateTime.now();

			// USERS

			AppUser user1 = new AppUser();
			user1.setUsername("jukka-poika42");
			user1.setFirstName("Jukka");
			user1.setLastName("Javalainen");
			user1.setEmail("jukkis@example.com");
			user1.setPasswordHash(passwordEncoder.encode("Salasana@123"));
			user1.setRegisteredAt(now);
			user1.setProjects(new ArrayList<>());
			user1.setTasksAssigned(new ArrayList<>());
			user1.setTasksCreated(new ArrayList<>());
			user1.setComments(new ArrayList<>());
			userRepository.save(user1);

			AppUser user2 = new AppUser();
			user2.setUsername("p-python");
			user2.setFirstName("Paula");
			user2.setLastName("Python");
			user2.setEmail("paula.python@example.com");
			user2.setPasswordHash(passwordEncoder.encode("Salasana@123"));
			user2.setRegisteredAt(now);
			user2.setProjects(new ArrayList<>());
			user2.setTasksAssigned(new ArrayList<>());
			user2.setTasksCreated(new ArrayList<>());
			user2.setComments(new ArrayList<>());
			userRepository.save(user2);

			// PROJECTS, TASKLISTS, TASKS, COMMENTS

			user1 = userRepository.findByUsername("jukka-poika42").orElseThrow();
			user2 = userRepository.findByUsername("p-python").orElseThrow();

			Project p1 = new Project();
			p1.setTitle("Test Project: The Six Seven App Creation Team");
			p1.setDescription("Random description");
			p1.setCreatedAt(now);
			p1.setIsShared(false);
			projectRepository.save(p1);

			// UserProject: Join table entry linking user to project with ownership tracking
			// Parameters: (user, project, sharedToThisUser)
			// sharedToThisUser = false means this user OWNS the project
			// sharedToThisUser = true means the project is SHARED with this user (not owner)

			UserProject up1 = new UserProject(user1, p1, false);
			userProjectRepository.save(up1);

			TaskList tl1 = new TaskList();
			tl1.setProject(p1);
			tl1.setTitle("Backlog of the super cool test project");
			tl1.setPositionNumber(1);
			tl1.setCreatedAt(now);
			taskListRepository.save(tl1);

			Task t1 = new Task();
			t1.setTaskList(tl1);
			t1.setAssignedUser(user1);
			t1.setTitle("Initial task");
			t1.setDescription("This task was created by test data");
			t1.setCreatedBy(user1);
			t1.setPositionNumber(1);
			t1.setDeadline(now.plusDays(7));
			taskRepository.save(t1);

			Comment c1 = new Comment();
			c1.setCommenter(user1);
			c1.setContent("This is a comment in test project 1");
			c1.setTask(t1);
			c1.setCreatedAt(now);
			commentRepository.save(c1);

			Project p2 = new Project();
			p2.setTitle("Website Redesign: Make YouTube great again");
			p2.setDescription("Second test project description");
			p2.setCreatedAt(now.plusHours(1));
			p2.setIsShared(true);
			projectRepository.save(p2);

			// Multiple users can access the same project with different roles:
			// up2: 'user' owns this project (sharedToThisUser = false)
			// up3: 'admin' has it shared with them (sharedToThisUser = true)
			// This allows collaboration while tracking who owns vs who has shared access

			UserProject up2 = new UserProject(user2, p2, false);
			userProjectRepository.save(up2);
			UserProject up3 = new UserProject(user1, p2, true);
			userProjectRepository.save(up3);

			TaskList tl2 = new TaskList();
			tl2.setProject(p2);
			tl2.setTitle("In Progress");
			tl2.setPositionNumber(2);
			tl2.setCreatedAt(now.plusHours(1));
			taskListRepository.save(tl2);

			Task t2 = new Task();
			t2.setTaskList(tl2);
			t2.setAssignedUser(user2);
			t2.setTitle("Create homepage mockup");
			t2.setDescription("Prepare updated landing page design");
			t2.setCreatedBy(user2);
			t2.setPositionNumber(2);
			t2.setDeadline(now.plusDays(14));
			taskRepository.save(t2);

			Comment c2 = new Comment();
			c2.setCommenter(user2);
			c2.setContent("Second seed comment");
			c2.setTask(t2);
			c2.setCreatedAt(now.plusHours(1));
			commentRepository.save(c2);
		};
	}

}
