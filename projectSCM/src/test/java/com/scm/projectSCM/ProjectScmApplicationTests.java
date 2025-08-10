package com.scm.projectSCM;

import com.scm.projectSCM.services.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProjectScmApplicationTests {
	@Autowired
	EmailService emailService;

	@Test
	void contextLoads() {
	}
}
