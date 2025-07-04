# BE_UngDungQuetBienSoXe

# 📄 Tài liệu API

## 1. Xác thực người dùng (Authentication)

### ➡️ Đăng ký tài khoản (Sign Up)  
*(Admin sẽ tự cập nhật thêm vào database)*

**POST** `/quet/api/auth/signup`
```json
{
  "id": "123456789000",
  "username": "userA",
  "email": "userA@example.com",
  "password": "123456"
}
```

### ➡️ Đăng nhập (Sign In)

**POST** `/quet/api/auth/signin`
```json
{
  "username": "minh",
  "password": "123456"
}
```

### ➡️ Đặt lại mật khẩu / Cập nhật tài khoản

**PUT** `/quet/api/account/reset_password`
```json
{
  "id": "123456789000",
  "username": "userA",
  "email": "userA@example.com",
  "password": "newpassword123"
}
```

---

## 2. API Quản lý người (Person)

### ➡️ Tạo mới người dùng

**POST** `/quet/api/person`
```json
{
  "id": "123456789000",
  "fullName": "Nguyễn Văn A",
  "birthDate": "1985-05-05",
  "gender": "MALE",
  "address": "123 Đường ABC, Quận 1, TP.HCM",
  "phoneNumber": "0123456789"
}
```

### ➡️ Lấy thông tin người dùng

**GET** `/quet/api/person`  
**GET** `/quet/api/person/{id}`

---

## 3. API Quản lý giấy phép lái xe

### ➡️ Lấy thông tin giấy phép lái xe theo số giấy phép

**GET** `/quet/api/driving-license/license/{licenseNumber}`

**Response (Thành công - 200 OK)**:
```json
{
  "id": "DL12345",
  "licenseNumber": "BKS-12345",
  "personId": "123456789000",
  "issueDate": "2023-01-15",
  "expiryDate": "2028-01-15",
  "licenseClass": "B2",
  "issueAuthority": "Sở GTVT TP.HCM"
}
```

**Response (Không tìm thấy - 404 Not Found)**

### ➡️ Lấy thông tin giấy phép lái xe theo ID người dùng

**GET** `/quet/api/driving-license/person/{personId}`

**Response (Thành công - 200 OK)**:
```json
{
  "id": "DL12345",
  "licenseNumber": "BKS-12345",
  "personId": "123456789000",
  "issueDate": "2023-01-15",
  "expiryDate": "2028-01-15",
  "licenseClass": "B2",
  "issueAuthority": "Sở GTVT TP.HCM"
}
```

**Response (Không tìm thấy - 404 Not Found)**

---

## 4. API Quản lý phương tiện

### 🚗 Ô tô (Car)

#### ➡️ Thêm mới ô tô

**POST** `/quet/api/vehicles/cars`
```json
{
  "licensePlate": "29A12345",
  "brand": "Toyota",
  "color": "Red",
  "ownerId": "123456789000"
}
```

#### ➡️ Lấy thông tin ô tô

**GET** `/quet/api/vehicles/cars`  
**GET** `/quet/api/vehicles/cars/{licensePlate}`

#### ➡️ Cập nhật thông tin ô tô

**PUT** `/quet/api/vehicles/cars/{licensePlate}`
```json
{
  "licensePlate": "29A12345",
  "brand": "Toyota",
  "color": "Blue",
  "ownerId": "123456789000"
}
```

#### ➡️ Xóa ô tô

**DELETE** `/quet/api/vehicles/cars/{licensePlate}`

---

### 🏍️ Xe máy (Motorcycle)

#### ➡️ Thêm mới xe máy

**POST** `/quet/api/vehicles/motorcycles`
```json
{
  "licensePlate": "47C23456",
  "brand": "Yamaha",
  "color": "Black",
  "ownerId": "987654321098"
}
```

#### ➡️ Lấy danh sách xe máy

**GET** `/quet/api/vehicles/motorcycles`

---

## 5. API Xử lý vi phạm (Violation)

### 🚗 Vi phạm ô tô

#### ➡️ Tạo vi phạm cho ô tô

**POST** `/quet/api/violations/car`
```json
{
  "violationTime": "2025-04-08T22:30:00",
  "violationType": "Vượt đèn đỏ",
  "description": "Xe vượt đèn đỏ tại giao lộ 1",
  "penaltyType": "Phạt tiền",
  "fineAmount": "150.00",
  "violatorId": "123456789000",
  "officerId": "111111111111",
  "licensePlate": "29A12345"
}
```

### 🏍️ Vi phạm xe máy

#### ➡️ Tạo vi phạm cho xe máy

**POST** `/quet/api/violations/motorcycle`
```json
{
  "violationTime": "2025-04-08T23:00:00",
  "violationType": "Lái quá tốc độ",
  "description": "Xe máy chạy quá tốc độ tại ngã tư 2",
  "penaltyType": "Phạt tiền",
  "fineAmount": "100.00",
  "violatorId": "123456789000",
  "officerId": "111111111111",
  "licensePlate": "47C23456"
}
```

---

## 6. API Nhật ký quét biển số (Scan Log)

### 🚗 Quét ô tô

**POST** `/quet/api/scan_logs/cars`
```json
{
  "licensePlate": "29A12345",
  "operatorId": "123456789000"
}
```

### 🏍️ Quét xe máy

**POST** `/quet/api/scan_logs/motorcycles`
```json
{
  "licensePlate": "47C23456",
  "operatorId": "987654321098"
}
```

---

## 7. API khác

### ➡️ Đăng xuất tài khoản

**POST** `/quet/api/auth/logout/:accountId`  
- **Ghi chú:** Gửi kèm `accountId` trong URL để đăng xuất tài khoản.

### ➡️ Lưu lịch sử đăng nhập

**POST** `/quet/api/auth/login-history`
```json
{
  "accountId": "058205002155",
  "ipAddress": "192.168.1.1",
  "deviceInfo": "Chrome - Windows",
  "loginStatus": "SUCCESS"
}
```

## Lưu ý sử dụng API

- Tất cả các API ngoại trừ đăng nhập/đăng ký đều yêu cầu xác thực JWT token
- Gửi token trong header của request: `Authorization: Bearer {token}`
- Nếu token hết hạn hoặc không hợp lệ, API sẽ trả về lỗi 401 Unauthorized