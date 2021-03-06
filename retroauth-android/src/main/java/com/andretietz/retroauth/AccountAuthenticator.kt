/*
 * Copyright (c) 2015 Andre Tietz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.andretietz.retroauth

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle

/**
 * This AccountAuthenticator is a very basic implementation of Android's
 * [AbstractAccountAuthenticator]. This implementation is intentional as empty as it is.
 * Cause of this is, that it's executed in a different process,
 * which makes it difficult to provide login endpoints from the app process in here.
 *
 *
 * NOTE: This class cannot be replaced with a kotlin version yet, since Android cannot load
 * Authenticators that are non java once.
 */
class AccountAuthenticator(
  context: Context,
  val action: String
) : AbstractAccountAuthenticator(context) {

  override fun addAccount(
    response: AccountAuthenticatorResponse?,
    accountType: String?,
    authTokenType: String?,
    requiredFeatures: Array<out String>?,
    options: Bundle?
  ) = createAuthBundle(response, accountType, authTokenType, null)

  override fun getAuthToken(
    response: AccountAuthenticatorResponse,
    account: Account,
    authTokenType: String,
    options: Bundle
  ) = createAuthBundle(response, account.type, authTokenType, account.name)

  private fun createAuthBundle(
    response: AccountAuthenticatorResponse?,
    accountType: String?,
    tokenType: String?,
    accountName: String?
  ): Bundle {
    val intent = Intent(action)
    intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
    intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType)
    intent.putExtra(KEY_TOKEN_TYPE, tokenType)
    if (null != accountName) {
      intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, accountName)
    }
    val bundle = Bundle()
    bundle.putParcelable(AccountManager.KEY_INTENT, intent)
    return bundle
  }

  override fun confirmCredentials(
    response: AccountAuthenticatorResponse,
    account: Account,
    options: Bundle
  ) = null

  override fun editProperties(
    response: AccountAuthenticatorResponse,
    accountType: String
  ) = null

  override fun getAuthTokenLabel(authTokenType: String) = null

  override fun updateCredentials(
    response: AccountAuthenticatorResponse,
    account: Account,
    authTokenType: String,
    options: Bundle
  ) = null

  override fun hasFeatures(
    response: AccountAuthenticatorResponse,
    account: Account,
    features: Array<String>
  ) = null

  companion object {
    const val KEY_TOKEN_TYPE = "account_token_type"
  }
}
