package setup

import java.io.File
import java.io.FileNotFoundException

class FileOperator {
    companion object {
        fun getDir(dir: String): File? =
            this::class.java.classLoader.getResource("$dir/task_db")?.let { File(it.toURI()) }

        fun getDirOrThrow(dir: String): File =
            this::class.java.classLoader.getResource("$dir/task_db")?.let { File(it.toURI()) }
                ?: throw FileNotFoundException("[${dir}]ディレクトリが見つかりません")
    }
}