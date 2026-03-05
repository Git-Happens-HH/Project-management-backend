package githappens.hh.project_management_app.config;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import githappens.hh.project_management_app.domain.AppUser;
import githappens.hh.project_management_app.domain.AppUserRepository;
import githappens.hh.project_management_app.domain.Comment;
import githappens.hh.project_management_app.domain.CommentRepository;
import githappens.hh.project_management_app.domain.Project;
import githappens.hh.project_management_app.domain.ProjectRepository;
import githappens.hh.project_management_app.domain.Task;
import githappens.hh.project_management_app.domain.TaskRepository;
import githappens.hh.project_management_app.domain.TaskList;
import githappens.hh.project_management_app.domain.TaskListRepository;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner seedData(AppUserRepository userRepo,
                                      ProjectRepository projectRepo,
                                      TaskListRepository taskListRepo,
                                      TaskRepository taskRepo,
                                      CommentRepository commentRepo) {
        return args -> {
            if (userRepo.count() > 0) return; // avoid seeding if data exists

            AppUser u = new AppUser();
            u.setUserName("testuser");
            u.setFirstName("Test");
            u.setLastName("User");
            u.setEmail("test@example.com");
            u.setPasswordHash("changeme");
            u.setRegisteredAt(LocalDateTime.now());
            u = userRepo.save(u);

            Project p = new Project();
            p.setTitle("Demo Project");
            p.setDescription("Seeded demo project");
            p.setCreatedAt(LocalDateTime.now());
            p = projectRepo.save(p);

            TaskList tl = new TaskList();
            tl.setProject(p);
            tl.setTitle("Backlog");
            tl.setPositionNumber(1);
            tl.setCreatedAt(LocalDateTime.now());
            tl = taskListRepo.save(tl);

            Task t = new Task();
            t.setTaskList(tl);
            t.setAssignedUser(u);
            t.setTitle("Seed Task");
            t.setDescription("This task was created by DataSeeder");
            t = taskRepo.save(t);

            Comment c = new Comment();
            c.setCommenter(u);
            c.setContent("Seed comment");
            c.setTask(t);
            c.setCreatedAt(LocalDateTime.now());
            commentRepo.save(c);
        };
    }
}
