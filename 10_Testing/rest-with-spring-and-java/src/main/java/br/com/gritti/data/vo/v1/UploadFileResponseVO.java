package br.com.gritti.data.vo.v1;

import java.io.Serializable;

public class UploadFileResponseVO implements Serializable {
  private static final long serialVersionUID = 1L;

  private String fileName;
  private String fileDownloadUri;
  private String fileType;
  private Long fileSize;

  public UploadFileResponseVO() {
  }

  public UploadFileResponseVO(String fileName, String fileDownloadUri, String fileType, Long fileSize) {
    this.fileName = fileName;
    this.fileDownloadUri = fileDownloadUri;
    this.fileType = fileType;
    this.fileSize = fileSize;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFileDownloadUri() {
    return fileDownloadUri;
  }

  public void setFileDownloadUri(String fileDownloadUri) {
    this.fileDownloadUri = fileDownloadUri;
  }

  public String getFileType() {
    return fileType;
  }

  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  public Long getFileSize() {
    return fileSize;
  }

  public void setFileSize(Long fileSize) {
    this.fileSize = fileSize;
  }
}
