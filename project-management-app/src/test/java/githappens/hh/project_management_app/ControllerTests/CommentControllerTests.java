package githappens.hh.project_management_app.ControllerTests;
import org.springframework.test.web.servlet.MockMvc;
import githappens.hh.project_management_app.domain.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import githappens.hh.project_management_app.domain.AppUser;
import githappens.hh.project_management_app.domain.Comment;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CommentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommentRepository commentRepository;
 
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private Comment comment;

}
