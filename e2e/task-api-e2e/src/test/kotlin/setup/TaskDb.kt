package setup

import com.thoughtworks.gauge.Step
import com.thoughtworks.gauge.datastore.ScenarioDataStore
import org.assertj.core.api.Condition
import org.assertj.db.api.Assertions.*
import org.assertj.db.type.Changes
import org.assertj.db.type.Request
import org.assertj.db.type.Source
import org.dbunit.Assertion
import org.dbunit.JdbcDatabaseTester
import org.dbunit.database.DatabaseConfig
import org.dbunit.dataset.IDataSet
import org.dbunit.dataset.ReplacementDataSet
import org.dbunit.dataset.csv.CsvDataSet
import org.dbunit.dataset.filter.DefaultColumnFilter
import org.dbunit.operation.DatabaseOperation
import setup.FileOperator.Companion.getDir
import setup.FileOperator.Companion.getDirOrThrow
import java.io.File
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime

class TaskDb {
    private val allTables = arrayOf("tasks")
    private val databaseTester by lazy {
        JdbcDatabaseTester(
            Config.config[Config.taskDb.driverClass],
            Config.config[Config.taskDb.url],
            Config.config[Config.taskDb.user],
            Config.config[Config.taskDb.password],
            Config.config[Config.taskDb.schema],
        )
    }
    private val connection by lazy {
        databaseTester.connection.apply {
            config.setProperty(
                DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS,
                true
            )
        }
    }
    private val schema by lazy { Config.config[Config.taskDb.schema] }
    private val source by lazy {
        Source(
            Config.config[Config.taskDb.url],
            Config.config[Config.taskDb.user],
            Config.config[Config.taskDb.password]
        )
    }

    private fun changes() = ScenarioDataStore.get("db_changes") as Changes

    fun setup(dir: String) {
        getDir(dir)?.let {
            truncate()
            DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet(it))
        }
    }

    fun startWatchingChangedDb() {
        val changes = Changes(source)
        changes.setStartPointNow()
        ScenarioDataStore.put("db_changes", changes)
    }

    private fun truncate() {
        DatabaseOperation.TRUNCATE_TABLE.execute(connection, connection.createDataSet(allTables))
    }

    fun teardown() {
        connection?.close()
    }

    private fun dataSet(dir: File): IDataSet {
        // <now>を現在日時にしているがDBUnitの標準で[now]が使える。[now-2h]とかできるので標準を使うことを推奨
        // https://oita.oika.me/2021/10/21/dbunit-relative-date
        val replacedDataMap = mapOf(
            "" to null, "<null>" to null,
            "<null-string>" to "null", "<empty>" to "",
            "<now>" to Timestamp(System.currentTimeMillis())
        )
        val partialReplacedDataMap = mapOf("<new_line>" to "\n", "<comma>" to ",")

        return ReplacementDataSet(CsvDataSet(dir), replacedDataMap, partialReplacedDataMap)
    }

    private fun dataSetOrThrow(dirPath: String): IDataSet = dataSet(getDirOrThrow(dirPath))

    @Step("<table>テーブルを条件<where>で取得したレコード数が<count>であること")
    fun assertRecordCount(table: String, where: String, count: Int) {
        val request = Request(source, "select * from ${schema}.${table} where $where")
        assertThat(request).hasNumberOfRows(count)
    }

    @Step("<table>テーブルを条件<where>で取得した<column>が<value>であること")
    fun assertRecordValue(table: String, where: String, column: String, value: String) {
        val request = Request(source, "select * from ${schema}.${table} where $where")
        assertThat(request).row().value(column).isEqualTo(value)
    }

    @Step("<table>テーブルを条件<where>で取得した<column>がNullであること")
    fun assertRecordValueIsNull(table: String, where: String, column: String) {
        val request = Request(source, "select * from ${schema}.${table} where $where")
        assertThat(request).row().value(column).isNull
    }

    @Step("<table>テーブルを条件<where>で取得した<column>がNullではないこと")
    fun assertRecordValueIsNotNull(table: String, where: String, column: String) {
        val request = Request(source, "select * from ${schema}.${table} where $where")
        assertThat(request).row().value(column).isNotNull
    }

    @Step("<table>テーブルを条件<where>で取得した<column>が<year>/<month>/<day>であること")
    fun assertRecordDateValue(table: String, where: String, column: String, year: Int, month: Int, day: Int) {
        val request = Request(source, "select * from ${schema}.${table} where $where")
        assertThat(request).row().value(column).isEqualTo(LocalDate.of(year, month, day))
    }

    @Step("<table>テーブルを条件<where>で取得した<column>が現在日付であること")
    fun assertRecordDateValueNow(table: String, where: String, column: String) {
        LocalDate.now().apply {
            assertRecordDateValue(table, where, column, year, monthValue, dayOfMonth)
        }
    }

    @Step("<table>テーブルを条件<where>で取得した<column>が現在日時のプラスマイナス<second>秒以内で一致していること")
    fun assertRecordDateTimeValueBetweenNow(table: String, where: String, column: String, second: Long) {
        val request = Request(source, "select * from ${schema}.${table} where $where")
        val now = LocalDateTime.now()
        assertThat(request).row().value(column)
            .isBeforeOrEqualTo(now.plusSeconds(second))
            .isAfterOrEqualTo(now.minusSeconds(second))
    }

    @Step("<table>テーブルを条件<where>で取得した<column>が正規表現<pattern>にマッチすること")
    fun assertRecord(table: String, where: String, column: String, pattern: String) {
        val request = Request(source, "select * from ${schema}.${table} where $where")
        val regex = Regex(pattern)
        assertThat(request).row().value(column)
            .`is`(Condition({ value: String -> regex.matches(value) }, "正規表現とマッチしていません。: $regex"))
    }

    @Step("<table>テーブルに作成されたレコード数が<count>であること")
    fun assertCreatedCount(table: String, count: Int) {
        val changes = changes()
        changes.setEndPointNow()
        assertThat(changes).ofCreationOnTable(table).hasNumberOfChanges(count)
            .changeOnTable(table).isCreation
    }

    @Step("<table>テーブルに更新されたレコード数が<count>であること")
    fun assertUpdatedCount(table: String, count: Int) {
        val changes = changes()
        changes.setEndPointNow()
        assertThat(changes).ofModificationOnTable(table).hasNumberOfChanges(count)
            .changeOnTable(table).isModification
    }

    @Step("<table>テーブルに削除されたレコード数が<count>であること")
    fun assertDeletedCount(table: String, count: Int) {
        val changes = changes()
        changes.setEndPointNow()
        assertThat(changes).ofDeletionOnTable(table).hasNumberOfChanges(count)
            .changeOnTable(table).isDeletion
    }

    @Deprecated(
        "「<table>テーブルを条件<where>で取得した<column>が〇〇であること」系のテストを推奨。理由は別ファイルに期待値を見に行かなくていいので可読性が上がる.",
        replaceWith = ReplaceWith("tableテーブルを条件whereで取得したcolumnがvalueであること")
    )
    @Step("DBが<dirPath>通りになっていること")
    fun assertTable(dirPath: String) {
        val expectedDataSet = dataSetOrThrow(dirPath)
        val actualDataSet = connection.createDataSet()

        val iterator = expectedDataSet.iterator()
        while (iterator.next()) {
            val expectedTable = iterator.table

            val expectedTableMeta = expectedTable.tableMetaData
            val actualTable = DefaultColumnFilter.includedColumnsTable(
                actualDataSet.getTable(expectedTableMeta.tableName),
                expectedTableMeta.columns
            )

            Assertion.assertEquals(expectedTable, actualTable)
        }
    }

}