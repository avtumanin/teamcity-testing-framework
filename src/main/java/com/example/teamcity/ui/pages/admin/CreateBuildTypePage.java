package com.example.teamcity.ui.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class CreateBuildTypePage extends CreateBasePage{
    public static final String BUILD_TYPE_SHOW_MODE = "createBuildTypeMenu";

    private final SelenideElement buildTypeNameInput = $("#buildTypeName");
    public static SelenideElement buildTypeError = $("[class=error]");

    public static CreateBuildTypePage open(String projectId) {
        return Selenide.open(CREATE_URL.formatted(projectId, BUILD_TYPE_SHOW_MODE), CreateBuildTypePage.class);
    }

    public CreateBuildTypePage createForm(String url) {
        baseCreateForm(url);
        return this;
    }

    public CreateBuildTypePage setupBuildType(String buildTypeMame) {
        buildTypeNameInput.val(buildTypeMame);
        submitButton.click();
        return this;
    }

    public void shouldHaveErrorMessage(String ErrorMessage) {
        buildTypeError.shouldHave(Condition.text(ErrorMessage));
    }
}