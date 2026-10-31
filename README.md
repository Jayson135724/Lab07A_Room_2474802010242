# Lab 07A – Room Entity & DAO Construction

**Course:** Mobile Application Programming · **MSSV:** 2474802010242

## Nội dung

| File | Mô tả |
|------|-------|
| `app/build.gradle.kts`, `gradle/libs.versions.toml` | Plugin KSP + Room (`room-runtime`, `room-ktx`, `ksp(room-compiler)`) + `lifecycle-livedata-ktx` |
| `data/local/ProductEntity.kt` | `@Entity(tableName = "products")`: `id` (PK, autoGenerate), `product_name`, `product_price`, `stock_quantity` |
| `data/local/ProductDao.kt` | `@Dao`: `insertProduct` (REPLACE, suspend), `deleteProduct` (suspend), `getAllProducts` (`LiveData`, `ORDER BY id DESC`), `getProductById` (suspend, nullable) |
| `data/local/AppDatabase.kt` | `@Database` singleton để Room sinh code `ProductDao_Impl` |
| `MainActivity.kt` | Màn hình demo: thêm sản phẩm, xóa sản phẩm mới nhất, danh sách tự cập nhật qua `LiveData` |

Bảng được sinh ra:

```sql
CREATE TABLE `products` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  `product_name` TEXT NOT NULL, `product_price` REAL NOT NULL, `stock_quantity` INTEGER NOT NULL)
```

## Ghi chú phiên bản

Project dùng AGP 9.4.1 (Kotlin tích hợp sẵn), nên không khai báo plugin `kotlin.android` riêng. KSP 2.3.x chỉ hỗ trợ KSP2, Room 2.6.1 không tương thích tốt với KSP2, nên dùng Room **2.8.5**. Các API `@Entity/@Dao/@Insert/@Delete/@Query` giống hệt bản 2.6.1.

## Build

```bash
./gradlew assembleDebug
```
