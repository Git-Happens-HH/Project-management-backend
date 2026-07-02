package githappens.hh.project_management_app.ControllerTests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import githappens.hh.project_management_app.domain.Project;

@WebMvcTest
public class ProjectControllerTests {

    @Autowired
    private Project project;

    @Autowired
    private MockMvc mockmvc;
    
}
