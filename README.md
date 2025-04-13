# TP6DPBO2025C1
## Janji
Saya Nashwa Nadria Futi dengan NIM 2308130 mengerjakan soal Tes Praktikum 6 dalam mata kuliah Desain Pemrograman Berbasis Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.

## Deskripsi Program
Program ini adalah implementasi permainan Flappy Bird dalam bahasa Java dengan menggunakan library Swing untuk antarmuka grafis. Flappy Bird adalah permainan arcade side-scroller di mana pemain mengendalikan seekor burung yang harus melewati serangkaian pipa tanpa menabrak. Fitur utama program meliputi:

1. Kontrol burung dengan tombol spasi untuk terbang
2. Sistem pipa yang bergerak dari kanan ke kiri
3. Mekanisme skor yang bertambah setiap kali pemain berhasil melewati pipa
4. Kondisi game over ketika burung menabrak pipa atau jatuh ke bawah layar
5. Opsi restart permainan dengan menekan tombol "R" setelah game over
6. Tampilan skor yang diperbarui secara real-time

## Desain Program
App.java:
- Kelas utama yang berisi metode main()
- Membuat JFrame sebagai container utama permainan
- Menginisialisasi dan menampilkan permainan


FlappyBird.java:
- Kelas utama permainan yang mewarisi JPanel
- Mengimplementasikan ActionListener untuk update game loop
- Mengimplementasikan KeyListener untuk menangani input pemain
- Mengelola logika permainan, termasuk pergerakan objek, deteksi tabrakan, sistem skor, rendering grafis, dan mengelola status permainan (berjalan/game over)

Player.java:
- Kelas yang mewakili objek burung yang dikendalikan pemain
- Menyimpan informasi posisi, dimensi, dan gambar
- Mengelola kecepatan vertikal untuk efek gravitasi dan lompatan

Pipe.java:
- Kelas yang mewakili objek pipa (rintangan)
- Menyimpan informasi posisi, dimensi, dan gambar
- Mengelola kecepatan horizontal dan status (sudah dilewati atau belum)

## Penjelasan Alur
1. Inisialisasi Game
   Metode startGame() dijalankan untuk:
   - Menginisialisasi posisi pemain
   - Membuat ArrayList untuk pipa
   - Memulai timer untuk pemunculan pipa (setiap 1.5 detik)
   - Memulai game loop (60 fps)
2. Game Loop
   Game loop berjalan pada 60 frame per detik melalui Timer
3. Pergerakan dan Interaksi
   - Pemain (Burung):
     - Dipengaruhi oleh gravitasi (velocityY bertambah 1 setiap tick)
     - Bisa "terbang" (velocityY = -10) saat tombol spasi ditekan
     - Jika menyentuh batas bawah layar, kondisi game over dipicu
   - Pipa:
     - Dipasang secara otomatis dengan timer terpisah
     - Bergerak dari kanan ke kiri dengan kecepatan konstan
     - Jarak antara pipa atas dan bawah diatur secara acak
     - Dihapus dari ArrayList setelah keluar dari layar sebelah kiri
   - Deteksi Tabrakan:
     - Metode checkCollision() memeriksa apakah pemain dan pipa bertabrakan
     - Jika tabrakan terdeteksi, kondisi game over dipicu
   - Sistem Skor:
     - Skor bertambah saat pemain berhasil melewati sepasang pipa
     - JLabel diperbarui untuk menampilkan skor terbaru

## Screenshot
1. Berhasil menjalankan game
   ![Screenshot 2025-04-13 102405](https://github.com/user-attachments/assets/64d8d4bd-b521-4a7c-89a0-e19e316c223a)

2. Game over
   ![Screenshot 2025-04-13 101755](https://github.com/user-attachments/assets/9fe63149-ffcc-48e3-bd40-f68c3c6b8a63)
