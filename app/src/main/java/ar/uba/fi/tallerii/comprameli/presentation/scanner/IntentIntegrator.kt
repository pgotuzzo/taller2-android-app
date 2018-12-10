/*
* Copyright 2009 ZXing authors
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package ar.uba.fi.tallerii.comprameli.presentation.scanner


import android.app.Activity
import android.app.AlertDialog
import android.app.Fragment
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import timber.log.Timber
import java.util.*

/**
 * <p>A utility class which helps ease integration with Barcode Scanner via {@link Intent}s. This is a simple
 * way to invoke barcode scanning and receive the result, without any need to integrate, modify, or learn the
 * project's source code.</p>
 *
 * <h2>Initiating a barcode scan</h2>
 *
 * <p>To integrate, create an instance of {@code IntentIntegrator} and call {@link #initiateScan()} and wait
 * for the result in your app.</p>
 *
 * <p>It does require that the Barcode Scanner (or work-alike) application is installed. The
 * {@link #initiateScan()} method will prompt the user to download the application, if needed.</p>
 *
 * <p>There are a few steps to using this integration. First, your {@link Activity} must implement
 * the method {@link Activity#onActivityResult(int, int, Intent)} and include a line of code like this:</p>
 *
 * <pre>{@code
 * public void onActivityResult(int requestCode, int resultCode, Intent intent) {
 *   IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
 *   if (scanResult != null) {
 *     // handle scan result
 *   }
 *   // else continue with any other code you need in the method
 *   ...
 * }
 * }</pre>
 *
 * <p>This is where you will handle a scan result.</p>
 *
 * <p>Second, just call this in response to a user action somewhere to begin the scan process:</p>
 *
 * <pre>{@code
 * IntentIntegrator integrator = new IntentIntegrator(yourActivity);
 * integrator.initiateScan();
 * }</pre>
 *
 * <p>Note that {@link #initiateScan()} returns an {@link AlertDialog} which is non-null if the
 * user was prompted to download the application. This lets the calling app potentially manage the dialog.
 * In particular, ideally, the app dismisses the dialog if it's still active in its {@link Activity#onPause()}
 * method.</p>
 *
 * <p>You can use {@link #setTitle(String)} to customize the title of this download prompt dialog (or, use
 * {@link #setTitleByID(int)} to set the title by string resource ID.) Likewise, the prompt message, and
 * yes/no button labels can be changed.</p>
 *
 * <p>Finally, you can use {@link #addExtra(String, Object)} to add more parameters to the Intent used
 * to invoke the scanner. This can be used to set additional options not directly exposed by this
 * simplified API.</p>
 *
 * <p>By default, this will only allow applications that are known to respond to this intent correctly
 * do so. The apps that are allowed to response can be set with {@link #setTargetApplications(List)}.
 * For example, set to {@link #TARGET_BARCODE_SCANNER_ONLY} to only target the Barcode Scanner app itself.</p>
 *
 * <h2>Sharing text via barcode</h2>
 *
 * <p>To share text, encoded as a QR Code on-screen, similarly, see {@link #shareText(CharSequence)}.</p>
 *
 * <p>Some code, particularly download integration, was contributed from the Anobiit application.</p>
 *
 * <h2>Enabling experimental barcode formats</h2>
 *
 * <p>Some formats are not enabled by default even when scanning with {@link #ALL_CODE_TYPES}, such as
 * PDF417. Use {@link #initiateScan(Collection)} with
 * a collection containing the names of formats to scan for explicitly, like "PDF_417", to use such
 * formats.</p>
 *
 * @author Sean Owen
 * @author Fred Lin
 * @author Isaac Potoczny-Jones
 * @author Brad Drehmer
 * @author gcstang
 */
class IntentIntegrator {

    private val activity: Activity
    private val fragment: Fragment?

    var title: String? = null
    var message: String? = null
    var buttonYes: String? = null
    var buttonNo: String? = null
    private var targetApplications: List<String>? = null
    private val moreExtras = HashMap<String, Any>(3)

    /**
     * @param activity [Activity] invoking the integration
     */
    constructor(activity: Activity) {
        this.activity = activity
        this.fragment = null
        initializeConfiguration()
    }

    /**
     * @param fragment [Fragment] invoking the integration.
     * [.startActivityForResult] will be called on the [Fragment] instead
     * of an [Activity]
     */
    constructor(fragment: Fragment) {
        this.activity = fragment.activity
        this.fragment = fragment
        initializeConfiguration()
    }

    private fun initializeConfiguration() {
        title = DEFAULT_TITLE
        message = DEFAULT_MESSAGE
        buttonYes = DEFAULT_YES
        buttonNo = DEFAULT_NO
        targetApplications = TARGET_ALL_KNOWN
    }

    fun setTitleByID(titleID: Int) {
        title = activity.getString(titleID)
    }

    fun setMessageByID(messageID: Int) {
        message = activity.getString(messageID)
    }

    fun setButtonYesByID(buttonYesID: Int) {
        buttonYes = activity.getString(buttonYesID)
    }

    fun setButtonNoByID(buttonNoID: Int) {
        buttonNo = activity.getString(buttonNoID)
    }

    fun getTargetApplications(): Collection<String>? {
        return targetApplications
    }

    fun setTargetApplications(targetApplications: List<String>) {
        if (targetApplications.isEmpty()) {
            throw IllegalArgumentException("No target applications")
        }
        this.targetApplications = targetApplications
    }

    fun setSingleTargetApplication(targetApplication: String) {
        this.targetApplications = listOf(targetApplication)
    }

    fun getMoreExtras(): Map<String, *> {
        return moreExtras
    }

    fun addExtra(key: String, value: Any) {
        moreExtras[key] = value
    }

    /**
     * Initiates a scan for all known barcode types with the specified camera.
     *
     * @param cameraId camera ID of the camera to use. A negative value means "no preference".
     * @return the [AlertDialog] that was shown to the user prompting them to download the app
     * if a prompt was needed, or null otherwise.
     */
    fun initiateScan(cameraId: Int): AlertDialog? {
        return initiateScan(ALL_CODE_TYPES, cameraId)
    }

    /**
     * Initiates a scan, using the specified camera, only for a certain set of barcode types, given as strings corresponding
     * to their names in ZXing's `BarcodeFormat` class like "UPC_A". You can supply constants
     * like [.PRODUCT_CODE_TYPES] for example.
     *
     * @param desiredBarcodeFormats names of `BarcodeFormat`s to scan for
     * @param cameraId camera ID of the camera to use. A negative value means "no preference".
     * @return the [AlertDialog] that was shown to the user prompting them to download the app
     * if a prompt was needed, or null otherwise
     */
    @JvmOverloads
    fun initiateScan(desiredBarcodeFormats: Collection<String>? = ALL_CODE_TYPES, cameraId: Int = -1): AlertDialog? {
        val intentScan = Intent("$BS_PACKAGE.SCAN")
        intentScan.addCategory(Intent.CATEGORY_DEFAULT)

        // check which types of codes to scan for
        if (desiredBarcodeFormats != null) {
            // set the desired barcode types
            val joinedByComma = StringBuilder()
            for (format in desiredBarcodeFormats) {
                if (joinedByComma.length > 0) {
                    joinedByComma.append(',')
                }
                joinedByComma.append(format)
            }
            intentScan.putExtra("SCAN_FORMATS", joinedByComma.toString())
        }

        // check requested camera ID
        if (cameraId >= 0) {
            intentScan.putExtra("SCAN_CAMERA_ID", cameraId)
        }

        val targetAppPackage = findTargetAppPackage(intentScan) ?: return showDownloadDialog()
        intentScan.setPackage(targetAppPackage)
        intentScan.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intentScan.addFlags(FLAG_NEW_DOC)
        attachMoreExtras(intentScan)
        startActivityForResult(intentScan, REQUEST_CODE)
        return null
    }

    /**
     * Start an activity. This method is defined to allow different methods of activity starting for
     * newer versions of Android and for compatibility library.
     *
     * @param intent Intent to start.
     * @param code Request code for the activity
     * @see Activity.startActivityForResult
     * @see Fragment.startActivityForResult
     */
    protected fun startActivityForResult(intent: Intent, code: Int) {
        if (fragment == null) {
            activity.startActivityForResult(intent, code)
        } else {
            fragment.startActivityForResult(intent, code)
        }
    }

    private fun findTargetAppPackage(intent: Intent): String? {
        val pm = activity.packageManager
        val availableApps = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (availableApps != null) {
            for (targetApp in targetApplications!!) {
                if (contains(availableApps, targetApp)) {
                    return targetApp
                }
            }
        }
        return null
    }

    private fun showDownloadDialog(): AlertDialog {
        val downloadDialog = AlertDialog.Builder(activity)
        downloadDialog.setTitle(title)
        downloadDialog.setMessage(message)
        downloadDialog.setPositiveButton(buttonYes) { _, _ ->
            val packageName: String
            if (targetApplications!!.contains(BS_PACKAGE)) {
                // Prefer to suggest download of BS if it's anywhere in the list
                packageName = BS_PACKAGE
            } else {
                // Otherwise, first option:
                packageName = targetApplications!!.get(0)
            }
            val uri = Uri.parse("market://details?id=$packageName")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            try {
                if (fragment == null) {
                    activity.startActivity(intent)
                } else {
                    fragment.startActivity(intent)
                }
            } catch (anfe: ActivityNotFoundException) {
                // Hmm, market is not installed
                Timber.w("Google Play is not installed; cannot install $packageName")
            }
        }
        downloadDialog.setNegativeButton(buttonNo, null)
        downloadDialog.setCancelable(true)
        return downloadDialog.show()
    }

    /**
     * Shares the given text by encoding it as a barcode, such that another user can
     * scan the text off the screen of the device.
     *
     * @param text the text string to encode as a barcode
     * @param type type of data to encode. See `com.google.zxing.client.android.Contents.Type` constants.
     * @return the [AlertDialog] that was shown to the user prompting them to download the app
     * if a prompt was needed, or null otherwise
     */
    @JvmOverloads
    fun shareText(text: CharSequence, type: CharSequence = "TEXT_TYPE"): AlertDialog? {
        val intent = Intent()
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.action = "$BS_PACKAGE.ENCODE"
        intent.putExtra("ENCODE_TYPE", type)
        intent.putExtra("ENCODE_DATA", text)
        val targetAppPackage = findTargetAppPackage(intent) ?: return showDownloadDialog()
        intent.setPackage(targetAppPackage)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(FLAG_NEW_DOC)
        attachMoreExtras(intent)
        if (fragment == null) {
            activity.startActivity(intent)
        } else {
            fragment.startActivity(intent)
        }
        return null
    }

    private fun attachMoreExtras(intent: Intent) {
        for ((key, value) in moreExtras) {
// Kind of hacky
            if (value is Int) {
                intent.putExtra(key, value)
            } else if (value is Long) {
                intent.putExtra(key, value)
            } else if (value is Boolean) {
                intent.putExtra(key, value)
            } else if (value is Double) {
                intent.putExtra(key, value)
            } else if (value is Float) {
                intent.putExtra(key, value)
            } else if (value is Bundle) {
                intent.putExtra(key, value)
            } else {
                intent.putExtra(key, value.toString())
            }
        }
    }

    companion object {

        val REQUEST_CODE = 0x0000c0de // Only use bottom 16 bits
        private val TAG = IntentIntegrator::class.java.simpleName

        val DEFAULT_TITLE = "Install Barcode Scanner?"
        val DEFAULT_MESSAGE = "This application requires Barcode Scanner. Would you like to install it?"
        val DEFAULT_YES = "Yes"
        val DEFAULT_NO = "No"

        private val BS_PACKAGE = "com.google.zxing.client.android"
        private val BSPLUS_PACKAGE = "com.srowen.bs.android"

        // supported barcode formats
        val PRODUCT_CODE_TYPES: Collection<String> = list("UPC_A", "UPC_E", "EAN_8", "EAN_13", "RSS_14")
        val ONE_D_CODE_TYPES: Collection<String> = list("UPC_A", "UPC_E", "EAN_8", "EAN_13", "CODE_39", "CODE_93", "CODE_128",
                "ITF", "RSS_14", "RSS_EXPANDED")
        val QR_CODE_TYPES: Collection<String> = setOf("QR_CODE")
        val DATA_MATRIX_TYPES: Collection<String> = setOf("DATA_MATRIX")

        val ALL_CODE_TYPES: Collection<String>? = null

        val TARGET_BARCODE_SCANNER_ONLY = listOf(BS_PACKAGE)
        val TARGET_ALL_KNOWN = list(
                BSPLUS_PACKAGE, // Barcode Scanner+
                "$BSPLUS_PACKAGE.simple", // Barcode Scanner+ Simple
                BS_PACKAGE                  // Barcode Scanner
                // What else supports this intent?
        )

        // Should be FLAG_ACTIVITY_NEW_DOCUMENT in API 21+.
        // Defined once here because the current value is deprecated, so generates just one warning
        private val FLAG_NEW_DOC = Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET

        private fun contains(availableApps: Iterable<ResolveInfo>, targetApp: String): Boolean {
            for (availableApp in availableApps) {
                val packageName = availableApp.activityInfo.packageName
                if (targetApp == packageName) {
                    return true
                }
            }
            return false
        }


        /**
         *
         * Call this from your [Activity]'s
         * [Activity.onActivityResult] method.
         *
         * @param requestCode request code from `onActivityResult()`
         * @param resultCode result code from `onActivityResult()`
         * @param intent [Intent] from `onActivityResult()`
         * @return null if the event handled here was not related to this class, or
         * else an [IntentResult] containing the result of the scan. If the user cancelled scanning,
         * the fields will be null.
         */
        fun parseActivityResult(requestCode: Int, resultCode: Int, intent: Intent): IntentResult? {
            if (requestCode == REQUEST_CODE) {
                if (resultCode == Activity.RESULT_OK) {
                    val contents = intent.getStringExtra("SCAN_RESULT")
                    val formatName = intent.getStringExtra("SCAN_RESULT_FORMAT")
                    val rawBytes = intent.getByteArrayExtra("SCAN_RESULT_BYTES")
                    val intentOrientation = intent.getIntExtra("SCAN_RESULT_ORIENTATION", Integer.MIN_VALUE)
                    val orientation = if (intentOrientation == Integer.MIN_VALUE) null else intentOrientation
                    val errorCorrectionLevel = intent.getStringExtra("SCAN_RESULT_ERROR_CORRECTION_LEVEL")
                    return IntentResult(contents,
                            formatName,
                            rawBytes,
                            orientation,
                            errorCorrectionLevel)
                }
                return IntentResult()
            }
            return null
        }

        private fun list(vararg values: String): List<String> {
            return Collections.unmodifiableList(Arrays.asList(*values))
        }
    }

}