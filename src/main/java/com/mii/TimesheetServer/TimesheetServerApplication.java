package com.mii.TimesheetServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TimesheetServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimesheetServerApplication.class, args);
                System.out.println("===== STARTED (PORT 8090) ======");
	}

}
