/*
 * Copyright (c) 2016 Andre Tietz
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

/**
 * This is the interface of a token storage.
 */
interface TokenStorage<in OWNER, in TOKEN_TYPE, TOKEN> {

    /**
     * This method returns an authentication token. If there's no token, you should try
     * authenticating your user.
     *
     * @param owner The owner type of the token you want to get
     * @param type  the type of the token you want to get
     * @return the token to authenticate your request or `null`
     * @throws AuthenticationCanceledException when the user canceled the authentication
     */
    @Throws(AuthenticationCanceledException::class)
    fun getToken(owner: OWNER, type: TOKEN_TYPE): TOKEN

    /**
     * Removes the token of a specific type and owner from the token storage.
     *
     * @param owner Owner of the token
     * @param type  Type of the token
     * @param token Token to remove
     */
    fun removeToken(owner: OWNER, type: TOKEN_TYPE, token: TOKEN)

    /**
     * Stores a token of a specific type and owner to the token storage.
     *
     * @param owner Owner of the token
     * @param type  Type of the token
     * @param token Token to store
     */
    fun storeToken(owner: OWNER, type: TOKEN_TYPE, token: TOKEN)
}