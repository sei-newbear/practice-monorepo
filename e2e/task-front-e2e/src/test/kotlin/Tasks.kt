import com.codeborne.selenide.Condition.*
import com.codeborne.selenide.Selenide.*
import com.thoughtworks.gauge.Step

class Tasks {
    @Step("タスク一覧画面を開く")
    fun openTasksPage(){
        open("/")
    }

    @Step("ページタイトル<title>が表示されていること")
    fun displayPageTitle(title: String){
        `$`("h1").shouldHave(exactText(title))
    }

    @Step("タスク一覧<index>番目のIDに<id>と表示されていること")
    fun displayTasksId(index: Int, id: String){
        `$$`(".task")[index -1].`$`(".id").shouldHave(exactText(id))
    }

    @Step("タスク一覧<index>番目のタイトルに<title>と表示されていること")
    fun displayTasksTitle(index: Int, title: String){
        `$$`(".task")[index -1].`$`(".title").shouldHave(exactText(title))
    }

    @Step("タイトルに<title>と入力する")
    fun inputTitle(title: String){
        val titleInput = `$`(".title")
        titleInput.clear()
        titleInput.sendKeys(title)
    }

    @Step("送信ボタンを押下する")
    fun clickSubmitButton() {
        `$`(".submit").click()
    }
}