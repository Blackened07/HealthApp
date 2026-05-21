package ru.HealthApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HealthAppApplication {

	//TODO: Ниже описано то как можно проверять роли для методов! В принципале содержится вся инфа, Пре авторайз сверяет инфу из принципала с указанной у аннотации

	/**
	 *  @PreAuthorize("hasRole('DOCTOR')")
	 *  @PostMapping("/prescriptions")
	 *  public void writePrescription(...)
	 *
	 */
	public static void main(String[] args) {
		SpringApplication.run(HealthAppApplication.class, args);
	}

}
