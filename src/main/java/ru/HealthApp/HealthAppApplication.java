package ru.HealthApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HealthAppApplication {
	// TODO: Создание происходит через аккаунт. Далее создаётся либо доктор либо юзер
	// TODO: Просмотреть все методы создания в контроллерах, методы запросов, логику сервисов, логику доступа и тд и тд

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
