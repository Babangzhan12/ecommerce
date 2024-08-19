package com.zan.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadFileResponses {
    
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
}
