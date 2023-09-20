package com.hoc08198.common.navigation.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.SaveableStateHolder
import com.hoc08198.common.navigation.Navigator
import com.hoc08198.common.navigation.Route
import com.hoc08198.common.navigation.RouteContent

internal class DefaultNavigator(
  private val stack: NavStack,
  private val contents: List<RouteContent<*>>,
) : Navigator {
  internal val visibleEntryState get() = stack.visibleEntryState

  override fun navigateTo(screen: Route) {
    stack.push(
      NavEntry.create(
        route = screen,
        contents = contents,
      )
    )
  }

  override fun navigateBack() {
    stack.pop()
  }
}

@Composable
internal fun rememberDefaultNavigator(
  initialRoute: Route,
  contents: List<RouteContent<*>>,
  saveableStateHolder: SaveableStateHolder,
): DefaultNavigator = remember(initialRoute, contents, saveableStateHolder) {
  DefaultNavigator(
    stack = NavStack(
      initial = NavEntry.create(
        route = initialRoute,
        contents = contents,
      ),
      onStackEntryRemoved = { entry ->
        saveableStateHolder.removeState(entry.id)
      }
    ),
    contents = contents,
  )
}
