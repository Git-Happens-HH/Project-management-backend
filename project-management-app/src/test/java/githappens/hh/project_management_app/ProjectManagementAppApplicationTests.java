package githappens.hh.project_management_app;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import githappens.hh.project_management_app.web.AppUserRestController;
import githappens.hh.project_management_app.web.AuthController;
import githappens.hh.project_management_app.web.ProjectRestController;
import githappens.hh.project_management_app.web.TaskListRestController;
import githappens.hh.project_management_app.web.TaskRestController;

@SpringBootTest
class ProjectManagementAppApplicationTests {

	@Autowired 
	private AppUserRestController appUserRestController;

	@Autowired 
	private AuthController authController;

	@Autowired 
	private ProjectRestController projectRestController;

	@Autowired 
	private TaskRestController taskRestController;

	@Autowired 
	private TaskListRestController taskListRestController;

	@Test
	void contextLoads() {
		assertThat(appUserRestController).isNotNull();
		assertThat(authController).isNotNull();
		assertThat(projectRestController).isNotNull();
		assertThat(taskRestController).isNotNull();
		assertThat(taskListRestController).isNotNull();
	}

}
