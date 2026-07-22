package nacholab.frame.ui.model

sealed class LoadingState {

    object Loading: LoadingState()

    object Success: LoadingState()

    data class NetworkError(val exception: Exception): LoadingState()

    data class ServerError(
        val code: Int,
        val message: String
    ): LoadingState()


}