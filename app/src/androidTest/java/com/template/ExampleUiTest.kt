package com.template

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.template.screens.home.HomeScreen
import com.template.screens.home.HomeUi
import com.template.ui.theme.TemplateTheme
import org.junit.Rule
import org.junit.Test

class ExampleUiTest {
    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun homeScreenDisplaysText() {
        composeTestRule.setContent { TemplateTheme { HomeUi(state = HomeScreen.State {}) } }

        composeTestRule.onNodeWithText("Home Screen").assertExists()
    }
}
