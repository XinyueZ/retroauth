package com.andretietz.retroauth

import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.annotation.SuppressLint
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController

@RunWith(RobolectricTestRunner::class)
class AuthenticationActivityTest {

    private var activityController: ActivityController<RetroauthTestLoginActivity> =
            Robolectric.buildActivity(RetroauthTestLoginActivity::class.java,
                    AuthenticationActivity.createLoginIntent("action", "account", "token"))
                    .setup()


    @Test
    fun createLoginIntent() {
        val intent = AuthenticationActivity.createLoginIntent("action", "account", "token")
        assertEquals("action", intent.action)
        assertEquals("account", intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE))
        assertEquals("token", intent.getStringExtra(AccountAuthenticator.KEY_TOKEN_TYPE))
    }


    @Test(expected = IllegalStateException::class)
    fun startActivityFailing() {
        Robolectric.buildActivity(RetroauthTestLoginActivity::class.java).setup()
    }

    @Test
    fun startActivitySuccess() {
        val intent = AuthenticationActivity.createLoginIntent("action", "account", "token")
        Robolectric.buildActivity(RetroauthTestLoginActivity::class.java, intent).setup()
    }

    @Test
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun storeTokenWithoutRefreshToken() {
        val account = mock(Account::class.java)
        val accountManager = mock(AccountManager::class.java)
        val activity = activityController.get()
        activity.setTestAccountManager(accountManager)
        activity.storeToken(account, "token-type", "token")

        verify(accountManager, times(1)).setAuthToken(any(Account::class.java), anyString(), anyString())
    }

    @Test
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun storeTokenWithRefreshToken() {
        val account = mock(Account::class.java)
        val accountManager = mock(AccountManager::class.java)
        val activity = activityController.get()
        activity.setTestAccountManager(accountManager)

        activity.storeToken(account, "token-type", "token", "refreshToken")

        verify(accountManager, times(2)).setAuthToken(any(Account::class.java), anyString(), anyString())
    }

    @Test
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun setUserData() {
        val account = mock(Account::class.java)
        val accountManager = mock(AccountManager::class.java)
        val activity = activityController.get()
        activity.setTestAccountManager(accountManager)

        activity.storeUserData(account, "data-key", "data-value")

        verify(accountManager, times(1)).setUserData(any(Account::class.java), anyString(), anyString())
    }

    @Test
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun createOrGetAccountWhenNoAccountExists() {
        val accountManager = mock(AccountManager::class.java)
        val activity = activityController.get()
        activity.setTestAccountManager(accountManager)
        activity.setTestAccountType("accountType")
        `when`(accountManager.getAccountsByType(anyString())).thenReturn(arrayOf())


        val account = activity.createOrGetAccount("accountName")
        assertEquals(account.name, "accountName")
        assertEquals(account.type, "accountType")

        verify(accountManager, times(1)).addAccountExplicitly(account, null, null)
    }

    @Test
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun createOrGetAccountWhenAccountExists() {
        val accountManager = mock(AccountManager::class.java)
        val activity = activityController.get()
        activity.setTestAccountManager(accountManager)
        activity.setTestAccountType("accountType")
        `when`(accountManager.getAccountsByType(anyString()))
                .thenReturn(arrayOf(Account("accountName", "accountType")))


        val account = activity.createOrGetAccount("accountName")
        assertEquals(account.name, "accountName")
        assertEquals(account.type, "accountType")

        verify(accountManager, never()).addAccountExplicitly(account, null, null)
    }

    @SuppressLint("NewApi")
    @Test
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun removeAccount() {
        val accountManager = mock(AccountManager::class.java)
        val activity = activityController.get()
        activity.setTestAccountManager(accountManager)
        val account = mock(Account::class.java)

        activity.removeAccount(account)

        verify(accountManager, times(1)).removeAccount(account, null, null, null)
    }

    @Test
    fun finalizeAuthenticationWithClosingActivity() {
        val activity = activityController.get()
        val account = mock(Account::class.java)
        activity.finalizeAuthentication(account)
    }

    @Test
    fun finalizeAuthenticationWithoutClosingActivity() {
        val activity = activityController.get()
        val account = mock(Account::class.java)
        activity.finalizeAuthentication(account, false)
    }


    @Test
    fun finish() {
        val activity = activityController.get()
        activity.finish()
    }

    @Test
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun finishFromAuthenticator() {
        val activity = activityController.get()
        activity.setTestResponse(mock(AccountAuthenticatorResponse::class.java))
        activity.finish()
    }

    @Test
    fun getRequestedAccountType() {
        val activity = activityController.get()
        assertNotNull(activity.getRequestedAccountType())
    }

    @Test
    fun getRequestedTokenType() {
        val activity = activityController.get()
        // not null since the intent in {@link #setup()} is providing it
        assertNotNull(activity.getRequestedTokenType())
    }


}