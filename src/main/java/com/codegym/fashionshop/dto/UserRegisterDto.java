package com.codegym.fashionshop.dto;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class UserRegisterDto {
    @NotEmpty(message = "Họ và tên không được để trống")
    private String fullName;
    @NotEmpty(message = "Tên đăng nhập không được để trống")
    private String username;
    @NotEmpty(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;
    private String confirmPassword;
    @NotNull(message = "Ngày sinh không được để trống")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    @NotEmpty(message = "Số điện thoại không được để trống")
    private String phone;
    @Email(message = "Email không đúng định dạng")
    @NotEmpty(message = "Email không được để trống")
    private String email;
    @NotEmpty(message = "Địa chỉ không được để trống")
    private String address;
}