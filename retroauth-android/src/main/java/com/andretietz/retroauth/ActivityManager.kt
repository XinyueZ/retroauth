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

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.util.Log

/**
 * The [ActivityManager] provides an application [android.content.Context] as well as an [Activity] if
 * this was not stopped already. It registers [ActivityLifecycleCallbacks] to be able to know if there's an active
 * [Activity] or not. The [Activity] is required in case the user calls an
 * [com.andretietz.retroauth.Authenticated] request and there are not tokens provided, to be able to open the
 * [Activity] for login, using the
 * [android.accounts.AccountManager.getAuthToken]. If you don't provide an [Activity] there, the
 * login screen wont open. So in case you're calling an [com.andretietz.retroauth.Authenticated] request from a
 * [android.app.Service] there will be no Login if required.
 */
internal class ActivityManager private constructor(application: Application) {
  private val handler: LifecycleHandler

  /**
   * @return an [Activity] if there's one available.
   */
  val activity: Activity? get() = handler.current

  init {
    handler = LifecycleHandler()
    application.registerActivityLifecycleCallbacks(handler)
  }

  fun register(activity: Activity) {
    handler.register(activity.hashCode())
  }

  /**
   * An implementation of [ActivityLifecycleCallbacks] which stores a reference to the [Activity] as long as
   * it is not stopped. If the [Activity] is stopped, the reference will be removed.
   */
  private class LifecycleHandler internal constructor() : ActivityLifecycleCallbacks {

    var hash: Int? = null

    fun register(hash: Int) {
      this.hash = hash
    }

    private val activityStack = WeakActivityStack()

    internal val current: Activity? get() = activityStack.peek()

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
      Log.e("Manager", "Activity created: " + activity.toString())
    }

    override fun onActivityStarted(activity: Activity) = activityStack.push(activity)

    override fun onActivityStopped(activity: Activity) = activityStack.remove(activity)

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}

    override fun onActivityDestroyed(activity: Activity) {
      if (activity.hashCode() == hash) {
        Log.e("Manager", "Emergency unlock!")
        CredentialInterceptor.emergencyUnlock()
      }
      Log.e("Manager", "Activity destroyed: " + activity.toString())

    }
  }

  companion object {
    @Volatile
    private var instance: ActivityManager? = null

    /**
     * @param application some [Activity] to be able to create the instance
     * @return a singleton instance of the [ActivityManager].
     */
    @JvmStatic
    operator fun get(application: Application): ActivityManager {
      val i = instance
      i?.let {
        return i
      }
      return synchronized(this) {
        val i2 = instance
        i2?.let {
          i2
        }
        val created = ActivityManager(application)
        instance = created
        created
      }
    }
  }
}
