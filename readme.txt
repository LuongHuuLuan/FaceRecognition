- add thư viện từ file libs trước khi sử dụng
- file createDataset để tạo data set từ 1 thư mục gốc chứa ảnh: 
  mỗi người được lưu trong một thư mục với cú pháp
  id_tên ví dụ 1_SonTung, 2_TranThanh
- file buildModel dùng để train model khi chưa có model nào được tạo
- project sử dụng database access gồm 1 bản NAME(id int, Name vachar255) có thể đổi cơ sở dữ liệu trong file Connect