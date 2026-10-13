package id.ac.uinjkt.bpm.web;

import id.ac.uinjkt.bpm.domain.PeminjamanRepository;
import id.ac.uinjkt.bpm.domain.PeminjamanRuang;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/peminjaman")
public class PeminjamanController {

    public record PengajuanRequest(String namaPeminjam, String kegiatan, String ruangan, LocalDate tanggal) {}

    private final PeminjamanRepository repo;
    private final RuntimeService runtimeService;
    private final TaskService taskService;

    public PeminjamanController(PeminjamanRepository repo, RuntimeService runtimeService, TaskService taskService) {
        this.repo = repo;
        this.runtimeService = runtimeService;
        this.taskService = taskService;
    }

    /** Langkah 1: ajukan peminjaman -> memulai proses BPMN. */
    @PostMapping
    public Map<String, Object> ajukan(@RequestBody PengajuanRequest req) {
        PeminjamanRuang p = new PeminjamanRuang();
        p.setNamaPeminjam(req.namaPeminjam());
        p.setKegiatan(req.kegiatan());
        p.setRuangan(req.ruangan());
        p.setTanggal(req.tanggal());
        p = repo.save(p);

        ProcessInstance pi = runtimeService.startProcessInstanceByKey(
                "peminjamanRuang", String.valueOf(p.getId()), Map.of("peminjamanId", p.getId()));
        p.setProcessInstanceId(pi.getId());
        repo.save(p);

        return Map.of("id", p.getId(), "processInstanceId", pi.getId(), "status", p.getStatus());
    }

    /** Langkah 2: Sekum menyetujui/menolak (menyelesaikan User Task). */
    @PostMapping("/{id}/review")
    public ResponseEntity<String> review(@PathVariable Long id, @RequestParam boolean setuju) {
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey(String.valueOf(id))
                .taskDefinitionKey("Task_Review")
                .singleResult();
        if (task == null) {
            return ResponseEntity.badRequest().body("Tidak ada task review untuk peminjaman #" + id);
        }
        taskService.complete(task.getId(), Map.of("setuju", setuju));
        return ResponseEntity.ok("Review selesai. setuju=" + setuju);
    }

    @GetMapping
    public List<PeminjamanRuang> daftar() {
        return repo.findAll();
    }
}
