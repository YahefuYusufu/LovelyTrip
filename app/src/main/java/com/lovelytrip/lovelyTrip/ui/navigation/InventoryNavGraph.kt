
package com.lovelytrip.lovelyTrip.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lovelytrip.lovelyTrip.ui.screens.HomeDestination
import com.lovelytrip.lovelyTrip.ui.screens.HomeScreen
import com.lovelytrip.lovelyTrip.ui.screens.ItemDetailsDestination
import com.lovelytrip.lovelyTrip.ui.screens.ItemDetailsScreen
import com.lovelytrip.lovelyTrip.ui.screens.ItemEditDestination
import com.lovelytrip.lovelyTrip.ui.screens.ItemEditScreen
import com.lovelytrip.lovelyTrip.ui.screens.ItemEntryDestination
import com.lovelytrip.lovelyTrip.ui.screens.ItemEntryScreen


@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun NavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToItemEntry = { navController.navigate(ItemEntryDestination.route) },
                navigateToItemUpdate = {
                    navController.navigate("${ItemDetailsDestination.route}/${it}")
                }
            )
        }
        composable(route = ItemEntryDestination.route) {
            ItemEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = ItemDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemDetailsDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
           ItemDetailsScreen(navigateToEditItem = { navController.navigate("${ItemEditDestination.route}/$it")},
               navigateBack = { navController.navigateUp()})
        }
        composable(
            route = ItemEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemEditDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ItemEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
