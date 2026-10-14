package nl.justid.skdb;

import org.springframework.boot.SpringApplication;

public class TestSkdbRegistryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(SkdbRegistryServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
