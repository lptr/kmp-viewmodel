package com.hoc081098.kmp.viewmodel.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import com.hoc081098.kmp.viewmodel.CreationExtras
import com.hoc081098.kmp.viewmodel.MainThread
import com.hoc081098.kmp.viewmodel.SavedStateHandleFactory
import com.hoc081098.kmp.viewmodel.ViewModel
import com.hoc081098.kmp.viewmodel.ViewModelFactory
import com.hoc081098.kmp.viewmodel.ViewModelStoreOwner
import kotlin.jvm.JvmSynthetic

@MainThread
@Composable
public expect inline fun <reified VM : ViewModel> kmpViewModel(
  factory: ViewModelFactory<VM>,
  viewModelStoreOwner: ViewModelStoreOwner = defaultViewModelStoreOwner(),
  key: String? = null,
  extras: CreationExtras = defaultPlatformCreationExtras(),
  savedStateHandleFactory: SavedStateHandleFactory? = LocalSavedStateHandleFactory.current,
): VM

@MainThread
@Composable
public inline fun <reified VM : ViewModel> kmpViewModel(
  key: String? = null,
  viewModelStoreOwner: ViewModelStoreOwner = defaultViewModelStoreOwner(),
  extras: CreationExtras = defaultPlatformCreationExtras(),
  savedStateHandleFactory: SavedStateHandleFactory? = LocalSavedStateHandleFactory.current,
  crossinline factory: @DisallowComposableCalls CreationExtras.() -> VM,
): VM = kmpViewModel(
  savedStateHandleFactory = savedStateHandleFactory,
  key = key,
  extras = extras,
  viewModelStoreOwner = viewModelStoreOwner,
  factory = rememberViewModelFactory(factory),
)

@JvmSynthetic
@PublishedApi
@MainThread
@Composable
internal inline fun defaultViewModelStoreOwner(): ViewModelStoreOwner =
  LocalViewModelStoreOwner.current
    ?: defaultPlatformViewModelStoreOwner()
