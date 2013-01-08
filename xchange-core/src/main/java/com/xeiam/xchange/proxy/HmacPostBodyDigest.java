/**
 * Copyright (C) 2013 Matija Mazi
 * Copyright (C) 2013 Xeiam LLC http://xeiam.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.xeiam.xchange.proxy;

import java.security.GeneralSecurityException;

import com.xeiam.xchange.ExchangeException;
import com.xeiam.xchange.utils.CryptoUtils;

/**
 * This may be used as the value of a @HeaderParam, @QueryParam or @PathParam to create a digest of the post body (composed of @FormParam's). Don't use as the value of a @FormParam, it will probably
 * cause an infinite loop.
 *
 * This may be used for REST APIs where some parameters' values must be digests of other parameters.
 * An example is the MtGox API v1, where the Rest-Sign header parameter must be a digest of the request body
 * (which is composed of @FormParams).
 */
public class HmacPostBodyDigest implements ParamsDigest {

  private String secretKey;

  /**
   * Constructor
   * 
   * @param secretKey
   */
  public HmacPostBodyDigest(String secretKey) {

    this.secretKey = secretKey;
  }

  @Override
  public String digestParams(AllParams allParams) {

    return createSignature(secretKey, allParams.getPostBody());
  }

  public static String createSignature(String secretKey, String postBody) {

    try {
      return CryptoUtils.computeSignature("HmacSHA512", postBody, secretKey);
    } catch (GeneralSecurityException e) {
      throw new ExchangeException("Security exception creating signature for request", e);
    }
  }
}