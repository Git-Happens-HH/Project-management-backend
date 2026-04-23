package githappens.hh.project_management_app.RepositoryTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import githappens.hh.project_management_app.domain.Project;
import githappens.hh.project_management_app.domain.ProjectRepository;
import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@SpringBootTest
public class ProjectRepositoryTests {

    @Autowired
    private ProjectRepository projectRepository;

    // CREATE a new project test
    @Test
    public void createNewProject() {
        Project project1 = new Project("Test Project1", "Testing project creation1", LocalDateTime.now(), false);
        projectRepository.save(project1);
        assertThat(project1.getProjectId()).isNotNull();
        assertThat(project1.getTitle()).isEqualTo("Test Project1");
        assertThat(project1.getDescription()).isEqualTo("Testing project creation1");
        assertThat(project1.getIsShared()).isFalse();
    }

    // SEARCH project by title test
    @Test
    public void findByTitleShouldReturnProject() {
        Project project2 = new Project("Test Project2", "Testing project search2", LocalDateTime.now(), false);
        projectRepository.save(project2);
        Optional<Project> found1 = projectRepository.findByTitle("Test Project2");
        assertThat(found1).isPresent();
        assertThat(found1.get().getTitle()).isEqualTo("Test Project2");
        assertThat(found1.get().getDescription()).isEqualTo("Testing project search2");
        assertThat(found1.get().getIsShared()).isFalse();
    }

    // SEARCH project by id test
    @Test
    public void findByIdShouldReturnProject() {
        Project project3 = new Project("Test Project3", "Testing project search by id3", LocalDateTime.now(), false);
        projectRepository.save(project3);
        Long projectId = project3.getProjectId();
        Optional<Project> found2 = projectRepository.findById(projectId);
        assertThat(found2).isPresent();
        assertThat(found2.get().getProjectId()).isEqualTo(projectId);
        assertThat(found2.get().getTitle()).isEqualTo("Test Project3");
        assertThat(found2.get().getDescription()).isEqualTo("Testing project search by id3");
        assertThat(found2.get().getIsShared()).isFalse();
    }

    // DELETE project by Id test
    @Test
    public void deleteProjectById() {
        Project project4 = new Project("Test Project4", "Testing project deletion4", LocalDateTime.now(), false);
        projectRepository.save(project4);
        Long projectId = project4.getProjectId();
        projectRepository.deleteById(projectId);
        Optional<Project> deleted = projectRepository.findById(projectId);
        assertThat(deleted).isEmpty();
    }

    // CUSTOM QUERY: FIND BY TITLE ignore case
    @Test
    public void findByTitleContainingIgnoreCaseShouldReturnProjects() {
        Project project1 = new Project("Test Project One", "Description1", LocalDateTime.now(), false);
        Project project2 = new Project("Another Test Project", "Description2", LocalDateTime.now(), false);
        projectRepository.save(project1);
        projectRepository.save(project2);

        List<Project> found = (List<Project>) projectRepository.findByTitleContainingIgnoreCase("test");
        assertThat(found).hasSize(2);
        assertThat(found).extracting(Project::getTitle).contains("Test Project One", "Another Test Project");
    }

    // CUSTOM QUERY: FIND ALL BY ORDER BY TITLE ASC
    @Test
    public void findAllByOrderByTitleAscShouldReturnProjectsInOrder() {
        Project project1 = new Project("Z Project", "Description Z", LocalDateTime.now(), false);
        Project project2 = new Project("A Project", "Description A", LocalDateTime.now(), false);
        Project project3 = new Project("M Project", "Description M", LocalDateTime.now(), false);
        projectRepository.save(project1);
        projectRepository.save(project2);
        projectRepository.save(project3);

        List<Project> found = (List<Project>) projectRepository.findAllByOrderByTitleAsc();
        assertThat(found).hasSize(3);
        assertThat(found.get(0).getTitle()).isEqualTo("A Project");
        assertThat(found.get(1).getTitle()).isEqualTo("M Project");
        assertThat(found.get(2).getTitle()).isEqualTo("Z Project");
    }
}