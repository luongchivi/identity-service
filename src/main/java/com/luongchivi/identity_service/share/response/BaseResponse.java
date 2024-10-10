package com.luongchivi.identity_service.share.response;



import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseResponse {
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
