package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

	@Test
	public void contextLoads() {
		// Test to ensure the application context loads successfully
	}

	@Test
	public void main_ShouldSetupPostgresAndRunApplication() {
		// Mocking Postgres setup method
		Postgres mockedPostgres = Mockito.mock(Postgres.class);
		Mockito.doNothing().when(mockedPostgres).setup();

		// Mocking SpringApplication.run
		SpringApplication mockedSpringApplication = Mockito.mock(SpringApplication.class);
		Mockito.doNothing().when(mockedSpringApplication).run(VulnadoApplication.class, new String[]{});

		// Calling the main method
		VulnadoApplication.main(new String[]{});

		// Verifying that Postgres.setup() was called
		Mockito.verify(mockedPostgres).setup();

		// Verifying that SpringApplication.run() was called
		Mockito.verify(mockedSpringApplication).run(VulnadoApplication.class, new String[]{});
	}
}
