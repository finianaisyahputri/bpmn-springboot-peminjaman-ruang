# BPMN + Spring Boot — Sistem Peminjaman Ruang HIMTI

Tugas: memodelkan proses bisnis dengan **BPMN**, lalu mengeksekusinya dalam aplikasi **Java Jakarta EE (Spring Boot 3)**.

> **Prinsip ABC dalam dokumen ini** (konfirmasi definisi dari dosen/AI): *Accurate* (akurat), *Brief* (ringkas), *Clear* (jelas) — setiap bagian berurutan: **A**wal (tujuan) → **B**adan (langkah/isi) → **C**losing (hasil/bukti).

## Daftar Isi
1. [Penjelasan 5W](#1-penjelasan-5w)
2. [Instalasi, konfigurasi, Hello World](#2-instalasi-konfigurasi-hello-world)
3. [Studi kasus: Peminjaman Ruang](#3-studi-kasus-peminjaman-ruang)
4. [Dokumentasi bukti (screenshot/rekaman)](#4-dokumentasi-bukti)
5. [Panduan GitHub](#5-panduan-github)

---

## 1. Penjelasan 5W

### 1.1 Spring / Spring Boot
| 5W | Jawaban |
|---|---|
| **What** | **Spring** adalah framework Java untuk membangun aplikasi enterprise (inti: *Dependency Injection* dan *Inversion of Control*). **Spring Boot** adalah lapisan di atas Spring yang menyediakan auto-configuration, *starter dependencies*, dan server embedded (Tomcat) sehingga aplikasi bisa jalan dengan `java -jar` tanpa konfigurasi XML panjang. Spring Boot 3 memakai namespace **Jakarta EE** (`jakarta.*`). |
| **Why** | Mengurangi boilerplate dan konfigurasi, mempercepat pengembangan, mudah diuji, ekosistem luas (Data JPA, Security, Web, dll.), dan menjadi standar industri untuk layanan Java. |
| **Who** | Dikembangkan oleh Pivotal/VMware (kini Broadcom); dipakai oleh developer backend/software engineer di berbagai perusahaan. |
| **Where** | Aplikasi web/REST API, microservices, sistem enterprise; berjalan di JVM (lokal, server, container, cloud). |
| **When** | Spring pertama kali dirilis 2003; Spring Boot 1.0 pada 2014; Spring Boot 3 (2022) mewajibkan Java 17+ dan Jakarta EE 9+. Dipakai saat membangun aplikasi Java modern yang perlu cepat dibuat dan mudah dipelihara. |

### 1.2 BPM / BPMN
| 5W | Jawaban |
|---|---|
| **What** | **BPM (Business Process Management)** adalah disiplin untuk menganalisis, memodelkan, menjalankan, dan memperbaiki proses bisnis. **BPMN (Business Process Model and Notation)** adalah notasi grafis standar (OMG, ISO/IEC 19510) untuk menggambarkan proses: event, task, gateway, sequence flow, dan lainnya. File BPMN 2.0 berformat XML sehingga dapat **dieksekusi** oleh process engine. |
| **Why** | Menyamakan pemahaman antara pihak bisnis dan developer, membuat alur kerja eksplisit dan terdokumentasi, serta memungkinkan proses dipantau dan diubah tanpa menulis ulang seluruh kode. |
| **Who** | Dipakai oleh business analyst, process owner, dan software engineer. Standar dikelola oleh Object Management Group (OMG). |
| **Where** | Pada alur persetujuan, pengajuan, onboarding, pemesanan, dan proses lain yang melibatkan beberapa langkah/peran; dimodelkan di modeler (Camunda Modeler / bpmn.io) dan dieksekusi di process engine (di proyek ini: Camunda 7). |
| **When** | BPMN 1.0 dirilis 2004; BPMN 2.0 (2011) menambahkan semantik eksekusi. Dipakai pada tahap analisis kebutuhan dan desain, lalu dieksekusi saat runtime. |

### 1.3 Keterkaitan Spring Boot dan BPM/BPMN
- BPMN menjadi **"peta alur"** proses; Spring Boot menjadi **"mesin/tempat kode"** yang melaksanakan tiap langkah.
- Process engine (Camunda) tertanam di aplikasi Spring Boot lewat *starter*. Setiap *Service Task* di BPMN memanggil **Spring Bean** (`${peminjamanService.cekKetersediaan(execution)}`), sedangkan *User Task* menunggu input manusia.
- **Manfaat BPM dalam pengembangan J2EE/Jakarta:**
  1. **Alur terpisah dari kode** — logika urutan/percabangan ada di diagram, bukan `if-else` yang tersebar di controller/service.
  2. **Komunikasi lebih mudah** — diagram dapat dibaca stakeholder non-teknis, mengurangi salah paham kebutuhan.
  3. **Traceability & monitoring** — status tiap instance proses dapat dilihat (Cockpit), memudahkan audit dan debugging.
  4. **Mudah diubah** — mengganti urutan/menambah langkah cukup mengedit BPMN dengan perubahan kode minimal.
  5. **Manajemen human task & state** — engine mengurus antrian tugas, penugasan, dan persistensi state proses (long-running).
  6. **Dokumentasi hidup** — model = dokumentasi sekaligus kode yang berjalan.

---

## 2. Instalasi, Konfigurasi, Hello World

### 2.1 Tools
| Tool | Versi (disarankan) | Fungsi |
|---|---|---|
| JDK | 17 (LTS) | Runtime & compiler Java |
| Apache Maven | 3.9+ | Build & dependency |
| IDE | IntelliJ IDEA / VS Code / Eclipse (STS) | Menulis kode |
| Camunda Modeler | 5.x | Mendesain/mengedit BPMN |
| Git + akun GitHub | terbaru | Dokumentasi & versi |

### 2.2 Langkah instalasi
1. **JDK 17** — unduh Temurin/OpenJDK 17, pasang, lalu cek: `java -version` dan `javac -version`.
2. **Maven** — unduh dari maven.apache.org, tambahkan `bin` ke PATH, cek: `mvn -v`.
3. **Camunda Modeler** — unduh dari camunda.com/download/modeler, ekstrak, jalankan. Buat diagram baru dengan tipe **Camunda 7 (BPMN)**.
4. **Git** — pasang, lalu `git --version`.
5. **Clone/buka proyek ini** di IDE sebagai proyek Maven.

### 2.3 Konfigurasi (ringkasan)
- `pom.xml`: Spring Boot Starter Web + Data JPA + H2, dan **Camunda Spring Boot Starter (webapp + rest)**.
- `application.yml`: database H2 in-memory, akun admin Camunda (`admin`/`admin`).
- File BPMN diletakkan di `src/main/resources/processes/` → **otomatis di-deploy** saat aplikasi start.

### 2.4 Hello World (tes instalasi)
Proses `hello-world.bpmn`: `Mulai → Cetak Hello World → Selesai`.

```bash
mvn spring-boot:run
```
Lalu buka:
- `http://localhost:8080/hello` → harus tampil: *Hello World! Proses BPMN 'helloWorld' dijalankan (instance …)*
- Log konsol memuat: `>>> Hello World dari BPMN + Spring Boot!`
- `http://localhost:8080/` → login `admin` / `admin` → **Cockpit** → proses `helloWorld` terdaftar.

Jika semua tampil, instalasi dan konfigurasi **berhasil**.

---

## 3. Studi Kasus: Peminjaman Ruang

**Konteks:** himpunan mahasiswa meminjam ruang kegiatan; harus dicek bentrok jadwal, lalu disetujui Sekretaris Umum.

### 3.1 Model BPMN (`peminjaman-ruang.bpmn`)

```
(Pengajuan diterima) → [Cek ketersediaan ruangan] → <Ruangan tersedia?>
      Ya    → [Review & persetujuan Sekum] → <Disetujui?>
                   Ya    → [Set status DISETUJUI]      → (Disetujui)
                   Tidak → [Set status DITOLAK]        → (Ditolak)
      Tidak → [Set status DITOLAK (bentrok)]           → (Ditolak (bentrok))
```

| Elemen BPMN | Jenis | Implementasi |
|---|---|---|
| Cek ketersediaan ruangan | Service Task | `PeminjamanService.cekKetersediaan` |
| Ruangan tersedia? / Disetujui? | Exclusive Gateway | variabel `tersedia` / `setuju` |
| Review & persetujuan Sekum | User Task (grup `sekum`) | `POST /api/peminjaman/{id}/review` |
| Set status … | Service Task | `PeminjamanService.updateStatus` |

### 3.2 Struktur kode
```
src/main/java/id/ac/uinjkt/bpm/
 ├─ BpmApplication.java            # entry point Spring Boot
 ├─ domain/ PeminjamanRuang, PeminjamanRepository   # JPA (Jakarta Persistence)
 ├─ service/ PeminjamanService, HelloWorldDelegate  # logika dipanggil BPMN
 └─ web/ PeminjamanController, HelloController       # REST API
src/main/resources/processes/ hello-world.bpmn, peminjaman-ruang.bpmn
```

### 3.3 Cara menguji (skenario)
```bash
# 1) Ajukan peminjaman -> proses berjalan sampai User Task
curl -X POST localhost:8080/api/peminjaman -H "Content-Type: application/json" \
  -d '{"namaPeminjam":"Ipii","kegiatan":"Rapat Kerja HIMTI","ruangan":"Lab 1","tanggal":"2026-11-10"}'

# 2) Sekum menyetujui
curl -X POST "localhost:8080/api/peminjaman/1/review?setuju=true"

# 3) Cek hasil
curl localhost:8080/api/peminjaman        # status = DISETUJUI

# 4) Uji bentrok: ajukan lagi ruang+tanggal yang sama
curl -X POST localhost:8080/api/peminjaman -H "Content-Type: application/json" \
  -d '{"namaPeminjam":"Budi","kegiatan":"Seminar","ruangan":"Lab 1","tanggal":"2026-11-10"}'
curl localhost:8080/api/peminjaman        # peminjaman #2 = DITOLAK_BENTROK
```
Pantau juga di **Cockpit** (instance proses) dan **H2 Console** (`/h2-console`, JDBC `jdbc:h2:mem:bpmdb`, user `sa`).

---

## 4. Dokumentasi Bukti
Simpan di `docs/screenshots/` dengan penamaan berurutan (mendukung prinsip runtun):

| No | File | Isi |
|---|---|---|
| 1 | `01-java-version.png` | hasil `java -version` & `mvn -v` |
| 2 | `02-camunda-modeler.png` | Camunda Modeler terbuka |
| 3 | `03-hello-bpmn.png` | diagram hello-world di Modeler |
| 4 | `04-hello-browser.png` | `/hello` di browser |
| 5 | `05-hello-log.png` | log konsol Hello World |
| 6 | `06-cockpit-deploy.png` | Cockpit: proses ter-deploy |
| 7 | `07-model-peminjaman.png` | diagram peminjaman-ruang di Modeler |
| 8 | `08-kode-service.png` | kode `PeminjamanService` |
| 9 | `09-postman-ajukan.png` | request pengajuan |
| 10 | `10-cockpit-usertask.png` | instance menunggu User Task |
| 11 | `11-review-setuju.png` | review & hasil DISETUJUI |
| 12 | `12-uji-bentrok.png` | hasil DITOLAK_BENTROK |

Untuk **rekaman**: rekam layar (OBS / Win+G) dari instalasi sampai skenario uji, simpan link video di bagian ini.

---

## 5. Panduan GitHub
```bash
cd bpmn-springboot-peminjaman-ruang
git init
git add .
git commit -m "feat: BPMN peminjaman ruang dengan Spring Boot dan Camunda"
git branch -M main
git remote add origin https://github.com/<username>/bpmn-springboot-peminjaman-ruang.git
git push -u origin main
```
Lalu di GitHub: **Settings → Collaborators → Add people** → undang dosen (email dari soal tugas), atau jadikan repo **Public** dan kirim tautannya.

Saran commit berurutan agar riwayat runtun: `docs: 5W`, `chore: setup project`, `feat: hello world`, `feat: model BPMN peminjaman`, `feat: service & controller`, `docs: screenshots`.
