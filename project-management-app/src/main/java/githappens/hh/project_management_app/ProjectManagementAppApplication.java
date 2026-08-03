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
import githappens.hh.project_management_app.domain.EnumProjectRole;
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

	// EnumProjectRole roleOwner = EnumProjectRole.owner;
	// EnumProjectRole roleMember = EnumProjectRole.member;
	// LocalDateTime now = LocalDateTime.now();

	public static void main(String[] args) {
		SpringApplication.run(ProjectManagementAppApplication.class, args);
	}

	// @Bean
	// public CommandLineRunner loadTestData(
	// 		AppUserRepository userRepository,
	// 		ProjectRepository projectRepository,
	// 		TaskListRepository taskListRepository,
	// 		TaskRepository taskRepository,
	// 		CommentRepository commentRepository,
	// 		UserProjectRepository userProjectRepository,
	// 		PasswordEncoder passwordEncoder) {
	// 	return args -> {
	// 		LocalDateTime now = LocalDateTime.now();

	// 		// USERS

	// 		AppUser user1 = new AppUser();
	// 		user1.setUsername("jukka-poika42");
	// 		user1.setFirstName("Jukka");
	// 		user1.setLastName("Javalainen");
	// 		user1.setEmail("jukkis@example.com");
	// 		user1.setPasswordHash(passwordEncoder.encode("Salasana@123"));
	// 		user1.setRegisteredAt(now);
	// 		user1.setTasksAssigned(new ArrayList<>());
	// 		user1.setTasksCreated(new ArrayList<>());
	// 		user1.setComments(new ArrayList<>());
	// 		userRepository.save(user1);

	// 		AppUser user2 = new AppUser();
	// 		user2.setUsername("p-python");
	// 		user2.setFirstName("Paula");
	// 		user2.setLastName("Python");
	// 		user2.setEmail("paula.python@example.com");
	// 		user2.setPasswordHash(passwordEncoder.encode("Salasana@123"));
	// 		user2.setRegisteredAt(now);
	// 		user2.setTasksAssigned(new ArrayList<>());
	// 		user2.setTasksCreated(new ArrayList<>());
	// 		user2.setComments(new ArrayList<>());
	// 		userRepository.save(user2);

	// 		// PROJECTS, TASKLISTS, TASKS, COMMENTS

	// 		jukka = userRepository.findByUsername("jukka-poika42").orElseThrow();
	// 		paula = userRepository.findByUsername("p-python").orElseThrow();
	// 		heikki = userRepository.findByUsername("heikki-hacker").orElseThrow();

	// 		Project p1 = new Project();
	// 		p1.setTitle("Test Project: The Six Seven App Creation Team");
	// 		p1.setDescription("Random description");
	// 		p1.setCreatedAt(now);
	// 		projectRepository.save(p1);

	// 		// UserProject: liitostaulun rivi, joka yhdistää käyttäjän projektiin
	// 		// ja tallentaa roolin (owner / member)

	// 		UserProject up1 = new UserProject(user1, p1, roleOwner, now);
	// 		userProjectRepository.save(up1);

	// 		// Add Heikki as a member of project 1 even though Jukka owns it
	// 		UserProject up2 = new UserProject(heikki, p1, roleMember, now);
	// 		userProjectRepository.save(up2);

	// 		TaskList tl1 = new TaskList();
	// 		tl1.setProject(p1);
	// 		tl1.setTitle("Backlog of the super cool test project");
	// 		tl1.setCreatedAt(now);
	// 		taskListRepository.save(tl1);

	// 		Task t1 = new Task();
	// 		t1.setTaskList(tl1);
	// 		t1.setAssignedUser(jukka);
	// 		t1.setTitle("Initial task");
	// 		t1.setDescription("This task was created by test data");
	// 		t1.setCreatedBy(jukka);
	// 		t1.setDeadline(now.plusDays(7));
	// 		taskRepository.save(t1);

	// 		Comment c1 = new Comment();
	// 		c1.setCommenter(jukka);
	// 		c1.setContent("This is a comment in test project 1");
	// 		c1.setTask(t1);
	// 		c1.setCreatedAt(now);
	// 		commentRepository.save(c1);

	// 		Project p2 = new Project();
	// 		p2.setTitle("Website Redesign: Make YouTube great again");
	// 		p2.setDescription("Second test project description");
	// 		p2.setCreatedAt(now.plusHours(1));
	// 		projectRepository.save(p2);

	// 		// Useampi käyttäjä voi kuulua samaan projektiin eri roolein:
	// 		// up3: jukka omistaa tämän projektin (rooli: owner)
	// 		// up4: paulalla on jäsenoikeus tähän projektiin (rooli: member)

	// 		UserProject up2 = new UserProject(user1, p2, roleOwner, now);
	// 		userProjectRepository.save(up2);
	// 		UserProject up3 = new UserProject(user2, p2, roleMember, now);
	// 		userProjectRepository.save(up3);
	// 		UserProject up4 = new UserProject(paula, p2, roleMember, now);
	// 		userProjectRepository.save(up4);

	// 		Project p3 = new Project();
	// 		p3.setTitle("Team Collaboration Project: We are so back");
	// 		p3.setDescription("It's so over");
	// 		p3.setCreatedAt(now.plusHours(2));
	// 		projectRepository.save(p3);

	// 		UserProject up5 = new UserProject(heikki, p3, roleOwner, now);
	// 		userProjectRepository.save(up5);
	// 		UserProject up6 = new UserProject(paula, p3, roleMember, now);
	// 		userProjectRepository.save(up6);

	// 		TaskList tl2 = new TaskList();
	// 		tl2.setProject(p2);
	// 		tl2.setTitle("In Progress");
	// 		tl2.setCreatedAt(now.plusHours(1));
	// 		taskListRepository.save(tl2);

	// 		Task t2 = new Task();
	// 		t2.setTaskList(tl2);
	// 		t2.setAssignedUser(paula);
	// 		t2.setTitle("Create homepage mockup");
	// 		t2.setDescription("Prepare updated landing page design");
	// 		t2.setCreatedBy(paula);
	// 		t2.setDeadline(now.plusDays(14));
	// 		taskRepository.save(t2);

	// 		Comment c2 = new Comment();
	// 		c2.setCommenter(paula);
	// 		c2.setContent("Second seed comment");
	// 		c2.setTask(t2);
	// 		c2.setCreatedAt(now.plusHours(1));
	// 		commentRepository.save(c2);
	// 	};
	// }

}