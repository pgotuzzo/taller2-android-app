package ar.uba.fi.tallerii.comprameli.data.files

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


class FilesDaoImpl : FilesDao {

    override fun uploadFile(uri: String, filePath: String): Single<String> {
        val storageRef = FirebaseStorage.getInstance().reference.child(filePath)

        val single: Single<String> = Single.create { emitter ->
            val uploadTask = storageRef.putFile(Uri.parse(uri))
            uploadTask.addOnFailureListener { uploadError ->
                emitter.onError(uploadError)
            }.addOnSuccessListener {
                storageRef.downloadUrl
                        .addOnSuccessListener { uri -> emitter.onSuccess(uri.toString()) }
                        .addOnFailureListener { uriError ->
                            Timber.e(uriError)
                            emitter.onError(uriError)
                        }
            }
        }
        return single.observeOn(Schedulers.io())
    }

}