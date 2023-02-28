import SwiftUI
import shared
import Kingfisher
import Combine

struct ProductsView: View {
  @Environment(\.scenePhase) var scenePhase

  @ObservedObject var viewModel = IosProductsViewModel(commonVm: DIContainer.shared.get())

  var body: some View {
    let state = self.viewModel.state

    return VStack {
      ZStack(alignment: .center) {
        if state.isLoading {
          ProgressView("Loading...")
        }
        else if let error = state.error {
          ErrorView(
            error: error,
            onRetry: { self.viewModel.dispatch(action: ProductsActionLoad()) }
          )
        }
        else if state.products.isEmpty {
          EmptyProductsView()
        } else {
          List {
            ForEach(state.products, id: \.id) { item in
              ProductItemRow(item: item)
            }
          }.refreshable {
            self.viewModel.dispatch(action: ProductsActionRefresh())

            try? await Task.sleep(nanoseconds: 1_000_000) // 1ms

            // await the first state where isRefreshing is false.
            let _: ProductsState? = await self.viewModel
              .$state
              .first(where: { !$0.isRefreshing })
              .values
              .first(where: { _ in true })
          }
        }
      }.frame(maxHeight: .infinity)
    }.onAppear {
      self.viewModel.dispatch(action: ProductsActionLoad())
      self.viewModel.onActive()
    }
      .onChange(of: scenePhase) { newPhase in
      switch newPhase {
      case .inactive:
        self.viewModel.onInactive()
      case .active:
        self.viewModel.onActive()
      case .background:
        ()
      @unknown default:
        fatalError()
      }
    }
  }
}


struct ContentView_Previews: PreviewProvider {
  static var previews: some View {
    ProductsView()
  }
}
