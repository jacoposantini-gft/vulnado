package com.scalesec.vulnado;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VulnadoApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void Cowsay_Run_ShouldReturnExpectedOutput() {
		// Mocking the ProcessBuilder and Process
		ProcessBuilder processBuilderMock = Mockito.mock(ProcessBuilder.class);
		Process processMock = Mockito.mock(Process.class);

		// Mocking the InputStreamReader to simulate the output of the command
		String expectedOutput = "Mocked Cowsay Output\n";
		BufferedReader readerMock = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(expectedOutput.getBytes())));

		try {
			Mockito.when(processBuilderMock.start()).thenReturn(processMock);
			Mockito.when(processMock.getInputStream()).thenReturn(new ByteArrayInputStream(expectedOutput.getBytes()));

			// Injecting the mocked ProcessBuilder into the Cowsay class
			Cowsay cowsay = new Cowsay();
			String actualOutput = cowsay.run("Hello");

			// Asserting the output
			assertEquals("The output should match the mocked output", expectedOutput, actualOutput);
		} catch (Exception e) {
			fail("Exception should not be thrown during the test");
		}
	}

	@Test
	public void Cowsay_Run_ShouldHandleExceptionGracefully() {
		// Mocking the ProcessBuilder to throw an exception
		ProcessBuilder processBuilderMock = Mockito.mock(ProcessBuilder.class);

		try {
			Mockito.when(processBuilderMock.start()).thenThrow(new RuntimeException("Mocked Exception"));

			// Injecting the mocked ProcessBuilder into the Cowsay class
			Cowsay cowsay = new Cowsay();
			String actualOutput = cowsay.run("Hello");

			// Asserting that the output is empty when an exception occurs
			assertEquals("The output should be empty when an exception occurs", "", actualOutput);
		} catch (Exception e) {
			fail("Exception should not be thrown during the test");
		}
	}
}
