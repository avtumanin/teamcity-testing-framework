package com.example.teamcity.ui;

import com.example.teamcity.api.enums.Endpoint;
import com.example.teamcity.ui.pages.ProjectPage;
import org.testng.annotations.Test;

import com.example.teamcity.ui.pages.admin.CreateBuildTypePage;
import org.apache.http.HttpStatus;



@Test(groups = {"Regression"})
public class CreateBuildTypeTest extends BaseUiTest {
    private static final String REPO_URL = "https://github.com/avtumanin/teamcity-testing-framework";

    @Test(description = "User should be able to create build type", groups = {"Positive"})
    public void userCreatesBuildType() {
        superUserCheckRequests.getRequest(Endpoint.PROJECTS).create(testData.getProject());
        loginAs(testData.getUser());

        CreateBuildTypePage.open(testData.getProject().getId())
                .createForm(REPO_URL)
                .setupBuildType(testData.getBuildType().getName());

        var createdBuildType= superUserCheckRequests.getRequest(Endpoint.BUILD_TYPES)
                .read("name:" + testData.getBuildType().getName());

        softy.assertNotNull(createdBuildType);

        var foundBuildType = ProjectPage.open(testData.getProject().getId())
                .getBuildTypes().stream()
                .anyMatch(Element -> Element.getName().text().equals(testData.getBuildType().getName()));

        softy.assertTrue(foundBuildType);
    }

    @Test(description = "User should not na able to create build type without name", groups = {"Negative"})
    public void userCreatesProjectWithoutName() {
        superUserCheckRequests.getRequest(Endpoint.PROJECTS).create(testData.getProject());
        loginAs(testData.getUser());

        CreateBuildTypePage.open(testData.getProject().getId())
                .createForm(REPO_URL)
                .setupBuildType("")
                .shouldHaveErrorMessage("Build configuration name must not be empty");

        superUserUnCheckedRequests.getRequest(Endpoint.BUILD_TYPES)
                .read("name:" + testData.getBuildType().getName())
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }
}