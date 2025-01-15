package org.akshar.camera.screens.camera_permission

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CameraPermissionViewModel(
    private val permissionsController: PermissionsController
) : ScreenModel {

    private val _state = MutableStateFlow(CameraPermissionState(UiPermissionState.INITIAL))
    val state = _state.asStateFlow()

    fun requestPermission() {
        screenModelScope.launch {
            try {
                permissionsController.providePermission(Permission.CAMERA)
                _state.update { it.copy(uiPermissionState = UiPermissionState.GRANTED) }
            } catch (deniedAlways: DeniedAlwaysException) {
                _state.update {
                    it.copy(
                        uiPermissionState = UiPermissionState.ALWAYS_DENIED,
                        isAlwaysDeniedDialogVisible = true
                    )
                }
            } catch (denied: DeniedException) {
                _state.update { it.copy(uiPermissionState = UiPermissionState.DENIED_ONCE) }
            }
        }
    }

    fun openSettings() {
        permissionsController.openAppSettings()
    }

    fun onDismissDialog() {
        _state.update { it.copy(isAlwaysDeniedDialogVisible = false) }
    }

    suspend fun checkIfHavePermission(): Boolean {
        return permissionsController.isPermissionGranted(Permission.CAMERA)
    }
}

data class CameraPermissionState(
    val uiPermissionState: UiPermissionState,
    val isAlwaysDeniedDialogVisible: Boolean = false
)

/**
 * UI layer version of Moko permission state
 * */
enum class UiPermissionState {
    GRANTED,
    ALWAYS_DENIED,
    DENIED_ONCE,
    INITIAL
}