package id.ac.uinjkt.bpm.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class PeminjamanRuang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String namaPeminjam;
    private String kegiatan;
    private String ruangan;
    private LocalDate tanggal;
    private String status = "DIAJUKAN";
    private String processInstanceId;

    public Long getId() { return id; }
    public String getNamaPeminjam() { return namaPeminjam; }
    public void setNamaPeminjam(String v) { this.namaPeminjam = v; }
    public String getKegiatan() { return kegiatan; }
    public void setKegiatan(String v) { this.kegiatan = v; }
    public String getRuangan() { return ruangan; }
    public void setRuangan(String v) { this.ruangan = v; }
    public LocalDate getTanggal() { return tanggal; }
    public void setTanggal(LocalDate v) { this.tanggal = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
    public String getProcessInstanceId() { return processInstanceId; }
    public void setProcessInstanceId(String v) { this.processInstanceId = v; }
}
