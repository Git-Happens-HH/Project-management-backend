package githappens.hh.project_management_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectManagementAppApplication {

// jos pitää tappaa prosessi:
// netstat -ano | findstr :8080
// taskkill /PID <PID> /F

// http://localhost:8080/h2-console // url: jdbc:h2:mem:testdb username: sa

	public static void main(String[] args) {
		SpringApplication.run(ProjectManagementAppApplication.class, args);
	}

}
