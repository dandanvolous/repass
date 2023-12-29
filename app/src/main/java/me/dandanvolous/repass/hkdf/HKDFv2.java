/**
 * Copyright (C) 2014-2016 Open Whisper Systems
 *
 * Licensed according to the LICENSE file in this repository.
 */
package me.dandanvolous.repass.hkdf;

public class HKDFv2 extends HKDF {
  @Override
  protected int getIterationStartOffset() {
    return 0;
  }
}
