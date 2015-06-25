package eu.unicate.retroauth;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AccountAuthenticator extends AbstractAccountAuthenticator {

	public static final String KEY_TOKEN_TYPE = "token_type";

	private final String action;

	private AccountManager manager;

	public AccountAuthenticator(Context context, String action) {
		super(context);
		manager = AccountManager.get(context);
		this.action = action;
	}


	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
		return createAuthBundle(response, accountType, authTokenType, null);
	}

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
		return createAuthBundle(response, account.type, authTokenType, account.name);
	}

	private Bundle createAuthBundle(AccountAuthenticatorResponse response, String accountType, String tokenType, String accountName) {
		Intent intent = new Intent(action);
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
		intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
		intent.putExtra(AccountAuthenticator.KEY_TOKEN_TYPE, tokenType);
		if(null != accountName) {
			intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, accountName);
		}

		final Bundle bundle = new Bundle();
		bundle.putParcelable(AccountManager.KEY_INTENT, intent);
		return bundle;
	}

	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
		return null;
	}

	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
		return null;
	}

	@Override
	public String getAuthTokenLabel(String authTokenType) {
		return null;
	}

	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
		return null;
	}

	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
		return null;
	}
}