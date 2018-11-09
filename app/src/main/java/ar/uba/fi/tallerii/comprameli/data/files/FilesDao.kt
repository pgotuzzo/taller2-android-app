package ar.uba.fi.tallerii.comprameli.data.files

import io.reactivex.Single

interface FilesDao {

    fun uploadFile(uri: String, filePath: String): Single<String>

}