package id.ac.uinjkt.bpm.service;

import id.ac.uinjkt.bpm.domain.PeminjamanRepository;
import id.ac.uinjkt.bpm.domain.PeminjamanRuang;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("peminjamanService")
public class PeminjamanService {

    private static final Logger log = LoggerFactory.getLogger(PeminjamanService.class);
    private final PeminjamanRepository repo;

    public PeminjamanService(PeminjamanRepository repo) {
        this.repo = repo;
    }

    /** Service Task "Cek ketersediaan ruangan": bentrok jika sudah ada peminjaman DISETUJUI di ruang+tanggal sama. */
    public void cekKetersediaan(DelegateExecution execution) {
        PeminjamanRuang p = ambil(execution);
        boolean bentrok = repo.existsByRuanganAndTanggalAndStatus(p.getRuangan(), p.getTanggal(), "DISETUJUI");
        execution.setVariable("tersedia", !bentrok);
        log.info("Cek ruang {} tgl {} -> tersedia={}", p.getRuangan(), p.getTanggal(), !bentrok);
    }

    /** Service Task untuk memperbarui status akhir peminjaman. */
    public void updateStatus(DelegateExecution execution, String status) {
        PeminjamanRuang p = ambil(execution);
        p.setStatus(status);
        repo.save(p);
        log.info("Peminjaman #{} -> {}", p.getId(), status);
    }

    private PeminjamanRuang ambil(DelegateExecution execution) {
        Long id = ((Number) execution.getVariable("peminjamanId")).longValue();
        return repo.findById(id).orElseThrow();
    }
}
