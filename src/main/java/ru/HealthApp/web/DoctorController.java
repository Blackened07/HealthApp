package ru.HealthApp.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.HealthApp.mapper.HealthRecordMapper;
import ru.HealthApp.service.AccountService;
import ru.HealthApp.service.DoctorService;
import ru.HealthApp.service.validators.AccessGuard;

@RestController
@RequestMapping("api/v1/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final AccountService accountService;
    private final DoctorService doctorService;
    private final AccessGuard accessGuard;
    private final HealthRecordMapper mapper;


    //getFamilies

    //getПриём
}
