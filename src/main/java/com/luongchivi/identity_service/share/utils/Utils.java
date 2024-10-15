package com.luongchivi.identity_service.share.utils;

import com.luongchivi.identity_service.entity.User;
import org.springframework.util.CollectionUtils;

import java.util.StringJoiner;

public class Utils {
    public static String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
                }
            });
        }

        return stringJoiner.toString();
    }

    public static String generateSlug(String text) {
        // Chuyển tiêu đề thành slug
        return text.toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9]+", "-")  // Thay thế các ký tự không phải chữ cái hoặc số bằng dấu "-"
                .replaceAll("-{2,}", "-")       // Xóa dấu "-" thừa
                .replaceAll("^-|-$", "");       // Xóa dấu "-" ở đầu và cuối
    }

}
