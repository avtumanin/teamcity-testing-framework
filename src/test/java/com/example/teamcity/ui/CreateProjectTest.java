package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.ui.pages.ProjectPage;
import com.example.teamcity.ui.pages.ProjectsPage;
import com.example.teamcity.ui.pages.admin.CreateProjectPage;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static com.example.teamcity.api.enums.Endpoint.PROJECTS;


@Test(groups = {"Regression"})
public class CreateProjectTest extends BaseUiTest {
    private static final String REPO_URL = "https://github.com/avtumanin/teamcity-testing-framework";

    @Test(description = "User should be able to create project", groups = {"Positive"})
    public void userCreatesProject() {
        loginAs(testData.getUser());

        CreateProjectPage.open("_Root")
                .createForm(REPO_URL)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());

        var createdProject = superUserCheckRequests.<Project>getRequest(PROJECTS)
                .read("name:" + testData.getProject().getName());

        softy.assertNotNull(createdProject);

        ProjectPage.open(createdProject.getId())
                .title.shouldHave(Condition.exactText(testData.getProject().getName()));

        var foundProjects = ProjectsPage.open()
                .getProjects().stream()
                .anyMatch(project -> project.getName().text().equals(testData.getProject().getName()));

        softy.assertTrue(foundProjects);
    }

    @Test(description = "User should not be able to create project without name", groups = {"Negative"})
    public void userCreatesProjectWithoutName() {
        loginAs(testData.getUser());

        CreateProjectPage.open("_Root")
                .createForm(REPO_URL)
                .setupProject("", testData.getBuildType().getName())
                .shouldHaveErrorMessage("Project name must not be empty");

        superUserUnCheckedRequests.getRequest(PROJECTS)
                .read("name:" + testData.getProject().getName())
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }
}