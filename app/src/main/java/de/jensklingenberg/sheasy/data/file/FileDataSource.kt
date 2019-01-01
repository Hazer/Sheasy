package de.jensklingenberg.sheasy.data.file

import io.reactivex.Maybe
import io.reactivex.Single
import de.jensklingenberg.sheasy.web.model.AppInfo
import de.jensklingenberg.sheasy.web.model.FileResponse
import java.io.InputStream


interface FileDataSource {
    fun getFiles(folderPath: String): Single<List<FileResponse>>
    fun returnAssetFile(filePath: String): Single<InputStream>
    fun getApps(): Single<List<AppInfo>>
    fun getApplicationInfo(apkPackageName: String): Maybe<AppInfo>
}
