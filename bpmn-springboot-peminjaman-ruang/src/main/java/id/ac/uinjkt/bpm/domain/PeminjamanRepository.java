package id.ac.uinjkt.bpm.domain;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeminjamanRepository extends JpaRepository<PeminjamanRuang, Long> {
    boolean existsByRuanganAndTanggalAndStatus(String ruangan, LocalDate tanggal, String status);
}
