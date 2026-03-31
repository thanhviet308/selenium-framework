# Selenium Framework

Project kiểm thử tự động bằng Selenium + TestNG + Maven.

## Cách chạy local

### 1) Cấu hình credential (bắt buộc)
Test sẽ đọc username/password theo thứ tự ưu tiên:

- Environment variables: `APP_USERNAME`, `APP_PASSWORD`
- Hoặc file cấu hình: `src/test/resources/config-<env>.properties` (keys `username`, `password`)

Ví dụ PowerShell:

`$env:APP_USERNAME = "standard_user"; $env:APP_PASSWORD = "secret_sauce"`

### 2) Chạy local
`mvn clean test -Dbrowser=chrome -Denv=dev -DsuiteXmlFile=testng-smoke.xml`

Ghi chú (PowerShell/Windows): nếu truyền URL trong `-D...` (vd `grid.url=http://...`) thì nên quote nguyên tham số `-D...` để tránh Maven hiểu nhầm thành plugin goal.