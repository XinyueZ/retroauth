package com.andretietz.retroauth

import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.accounts.NetworkErrorException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.mockito.Mockito.mock

@RunWith(RobolectricTestRunner::class)
class AccountAuthenticatorTest {

    private val authenticator: AccountAuthenticator = AccountAuthenticator(mock(Context::class.java), "some-action")

    @Before
    fun setup() {
        ActivityManager[RuntimeEnvironment.application]
    }

    @Test
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun constructor() {
        val authenticator = AccountAuthenticator(mock(Context::class.java), "some-action")
        assertNotNull(authenticator.action)
        assertEquals("some-action", authenticator.action)
    }

    @Test
    @Throws(NetworkErrorException::class)
    fun addAccount() {
        val response = mock(AccountAuthenticatorResponse::class.java)
        val bundle = authenticator!!.addAccount(response, "accountType", "tokenType",
                arrayOf(), mock(Bundle::class.java))

        assertNotNull(bundle)
        val intent = bundle.getParcelable<Intent>(AccountManager.KEY_INTENT)
        assertNotNull(intent)

        assertEquals(
                response,
                intent!!.getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE))
        assertEquals("accountType", intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE))
        assertEquals("tokenType", intent.getStringExtra(AccountAuthenticator.KEY_TOKEN_TYPE))
    }

    @Test
    @Throws(NetworkErrorException::class)
    fun getAuthToken() {
        val response = mock(AccountAuthenticatorResponse::class.java)
        val account = Account("accountName", "accountType")
        val bundle = authenticator!!.getAuthToken(response, account, "tokenType", mock(Bundle::class.java))

        assertNotNull(bundle)
        val intent = bundle.getParcelable<Intent>(AccountManager.KEY_INTENT)
        assertNotNull(intent)

        assertEquals(
                response,
                intent!!.getParcelableExtra<Parcelable>(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE))
        assertEquals("accountType", intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE))
        assertEquals("tokenType", intent.getStringExtra(AccountAuthenticator.KEY_TOKEN_TYPE))
        assertEquals("accountName", intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME))
    }

    @Test
    @Throws(NetworkErrorException::class)
    fun hasFeatures() {
        val bundle = authenticator!!
                .hasFeatures(mock(AccountAuthenticatorResponse::class.java), mock(Account::class.java), arrayOf())
        assertNull(bundle)
    }

    @Test
    @Throws(NetworkErrorException::class)
    fun updateCredentials() {
        val bundle = authenticator!!
                .updateCredentials(mock(AccountAuthenticatorResponse::class.java), mock(Account::class.java), "token-type",
                        mock(Bundle::class.java))
        assertNull(bundle)
    }

    @Test
    @Throws(NetworkErrorException::class)
    fun getAuthTokenLabel() {
        val label = authenticator!!.getAuthTokenLabel("token-type")
        assertNull(label)
    }

    @Test
    @Throws(NetworkErrorException::class)
    fun editProperties() {
        val bundle = authenticator!!
                .editProperties(mock(AccountAuthenticatorResponse::class.java), "accountType")
        assertNull(bundle)
    }

    @Test
    @Throws(NetworkErrorException::class)
    fun confirmCredentials() {
        val bundle = authenticator!!
                .confirmCredentials(mock(AccountAuthenticatorResponse::class.java), mock(Account::class.java), mock(Bundle::class.java))
        assertNull(bundle)
    }
}