package com.waracle.cakemgr.exception;

public class CakeNotFoundException extends RuntimeException {
  public CakeNotFoundException(String message) {
    super(message);
  }
}
