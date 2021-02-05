package com.mii.TimesheetClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TimesheetClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimesheetClientApplication.class, args);
                System.out.println("===== STARTED (PORT 8070) =====");
	}

}
