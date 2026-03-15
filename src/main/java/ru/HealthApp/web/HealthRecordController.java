package ru.HealthApp.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.HealthApp.dto.HealthRecordRequestDTO;
import ru.HealthApp.dto.HealthRecordResponseDTO;
import ru.HealthApp.security.UserPrincipal;
import ru.HealthApp.service.HealthRecordService;
import ru.HealthApp.service.validators.AccessGuard;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/health-records")
@RequiredArgsConstructor
public class HealthRecordController {

    private final HealthRecordService healthRecordService;

    @PostMapping("/{targetId}")
    public ResponseEntity<HealthRecordResponseDTO> create(
            @Valid @RequestBody HealthRecordRequestDTO recordRequestDTO,
            @AuthenticationPrincipal UserPrincipal author,
            @PathVariable Long targetId
    ) {

        Long authorId = author.userId();
        HealthRecordResponseDTO response = healthRecordService.createRecord(authorId, targetId, recordRequestDTO);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<HealthRecordResponseDTO>> getHistory(
            @PathVariable Long userId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam Long actorId) {
        
        List<HealthRecordResponseDTO> history = healthRecordService.getHistory(actorId, userId, type, from, to);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<List<HealthRecordResponseDTO>> getFamilyDashboard(
            @RequestParam Long actorId) {
        
        List<HealthRecordResponseDTO> dashboard = healthRecordService.getFamilyDashboard(actorId);
        return ResponseEntity.ok(dashboard);
    }

    @PutMapping("/{recordId}")
    public ResponseEntity<HealthRecordResponseDTO> updateRecord(
            @PathVariable Long recordId,
            @RequestBody HealthRecordRequestDTO request,
            @RequestParam Long actorId) {
        
        HealthRecordResponseDTO response = healthRecordService.updateRecord(actorId, recordId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{recordId}")
    public ResponseEntity<Void> deleteRecord(
            @PathVariable Long recordId,
            @RequestParam Long actorId) {
        
        healthRecordService.deleteRecord(actorId, recordId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/metrics")
    public ResponseEntity<List<String>> getDefaultMetrics() {
        List<String> metrics = healthRecordService.getDefaultMetrics()
                .stream()
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(metrics);
    }
}
