# Lab 9 — Bai 4 Performance

## Cach do

Luu y truoc khi chay: can co credential (APP_USERNAME/APP_PASSWORD) hoac dien `username/password` trong `src/test/resources/config-dev.properties`.

### 1) Local tuan tu (thread = 1)
Chay (vi du):

- `mvn test -Dheadless=true -Denv=dev -DsuiteXmlFile=testng-smoke.xml`

Ghi lai thoi gian chay tong (T1).

### 2) Grid song song 2 thread
- Bat Grid: `docker-compose up -d`
- Chay (PowerShell/Windows nen quote tham so co URL): `mvn test "-Dgrid.url=http://localhost:4444" -DsuiteXmlFile=testng-grid.xml -Dheadless=true`
- Chinh `thread-count` trong `testng-grid.xml` = 2

Ghi lai T2.

### 3) Grid song song 4 thread
- Chinh `thread-count` trong `testng-grid.xml` = 4
- Chay lai lenh Grid

Ghi lai T3.

## Bang ket qua

| Cau hinh | So thread | Thoi gian chay (giay) | He so tang toc |
|---|---:|---:|---:|
| Tuan tu (local) | 1 | T1 | 1.0x |
| Song song Grid | 2 | T2 | T1/T2 |
| Song song Grid | 4 | T3 | T1/T3 |

## Anh can chup
- Grid Console: http://localhost:4444 (thay 3 node)
- Luc test dang chay: Grid Console co nhieu session hoat dong
