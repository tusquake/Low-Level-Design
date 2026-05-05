package com.amazon.locker.model;

import com.amazon.locker.model.enums.LockerSize;
import com.amazon.locker.model.enums.PackageStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Package {
    private String packageId;
    private String customerId;
    private LockerSize size;
    private PackageStatus status;
}
