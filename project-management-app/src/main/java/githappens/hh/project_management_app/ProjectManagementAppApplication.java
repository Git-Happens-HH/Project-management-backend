package githappens.hh.project_management_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CommandLineRunner loadTestData(
			AppUserRepository userRepository,
			ProjectRepository projectRepository,
			TaskListRepository taskListRepository,
			TaskRepository taskRepository,
			CommentRepository commentRepository,
			PasswordEncoder passwordEncoder
	) {
		return args -> {
			// USERS
			if (!userRepository.findByUserName("admin").isPresent()) {
				AppUser admin = new AppUser();
				admin.setUserName("admin");
				admin.setPasswordHash(passwordEncoder.encode("admin123"));
				admin.setEmail("admin@example.com");
				userRepository.save(admin);
			}

			if (!userRepository.findByUserName("user").isPresent()) {
				AppUser user = new AppUser();
				user.setUserName("user");
				user.setPasswordHash(passwordEncoder.encode("user123"));
				user.setEmail("user@example.com");
				userRepository.save(user);
			}

			// PROJECTS, TASKLISTS, TASKS, COMMENTS
			if (projectRepository.count() == 0) {
				AppUser admin = userRepository.findByUserName("admin").orElseThrow();

				Project p1 = new Project();
				p1.setTitle("Demo Project");
				p1.setDescription("Seeded project");
				projectRepository.save(p1);

				TaskList tl1 = new TaskList();
				tl1.setProject(p1);
				tl1.setTitle("Backlog");
				taskListRepository.save(tl1);

				Task t1 = new Task();
				t1.setTaskList(tl1);
				t1.setAssignedUser(admin);
				t1.setTitle("Initial task");
				t1.setDescription("This task was created by seed data");
				taskRepository.save(t1);

				Comment c1 = new Comment();
				c1.setCommenter(admin);
				c1.setContent("Seed comment");
				c1.setTask(t1);
				commentRepository.save(c1);
			}
		};
	}

}
